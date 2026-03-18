package com.pragma.plazoleta.domain.usecase;

import com.pragma.plazoleta.domain.api.IOrderServicePort;
import com.pragma.plazoleta.domain.constants.DomainConstants;
import com.pragma.plazoleta.domain.exception.DomainException;
import com.pragma.plazoleta.domain.exception.ErrorCode;
import com.pragma.plazoleta.domain.model.*;
import com.pragma.plazoleta.domain.spi.*;
import com.pragma.plazoleta.domain.validator.OrderDomainValidator;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public class OrderUseCase implements IOrderServicePort {

    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IDishPersistencePort dishPersistencePort;
    private final IOrderPersistencePort orderPersistencePort;
    private final IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort;
    private final IOrderCodePersistencePort orderCodePersistencePort;
    private final ISmsPersistencePort smsPersistencePort;
    private final ICodeGeneratorPort codeGeneratorPort;
    private final IUserPersistencePort userPersistencePort;
    private final IDeliveryCodePersistencePort deliveryCodePersistencePort;
    private final ITrazabilidadPersistencePort trazabilidadPersistencePort;

    public OrderUseCase(IRestaurantPersistencePort restaurantPersistencePort, IDishPersistencePort dishPersistencePort, IOrderPersistencePort orderPersistencePort,
                        IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort, IOrderCodePersistencePort orderCodePersistencePort,
                        ISmsPersistencePort smsPersistencePort, ICodeGeneratorPort codeGeneratorPort, IUserPersistencePort userPersistencePort,
                        IDeliveryCodePersistencePort deliveryCodePersistencePort, ITrazabilidadPersistencePort trazabilidadPersistencePort){
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.dishPersistencePort = dishPersistencePort;
        this.orderPersistencePort = orderPersistencePort;
        this.employeeRestaurantPersistencePort = employeeRestaurantPersistencePort;
        this.orderCodePersistencePort = orderCodePersistencePort;
        this.smsPersistencePort = smsPersistencePort;
        this.codeGeneratorPort = codeGeneratorPort;
        this.userPersistencePort = userPersistencePort;
        this.deliveryCodePersistencePort = deliveryCodePersistencePort;
        this.trazabilidadPersistencePort = trazabilidadPersistencePort;
    }

    @Override
    public Order saveOrder(Order order, Long userId, String email) {
        restaurantPersistencePort.findById(order.getRestaurantId())
                .orElseThrow(() -> new DomainException(ErrorCode.DATA_NOT_FOUND, DomainConstants.RNF));

        boolean hasActiveOrder = orderPersistencePort.existsByClientIdAndStatusNotIn(
                userId,
                List.of(OrderStatus.CANCELADO, OrderStatus.ENTREGADO)
        );

        if (hasActiveOrder) {
            throw new DomainException(
                    ErrorCode.ORDER_ALREADY_EXISTS,
                    DomainConstants.CAE
            );
        }

        for (OrderItem item : order.getItems()) {

            Dish dish = dishPersistencePort.findById(item.getDishId())
                    .orElseThrow(() -> new DomainException(
                            ErrorCode.INVALID_DISH,
                            DomainConstants.DNF));

            if(!dish.isActive()){
                throw new DomainException(
                        ErrorCode.INVALID_DISH,
                        DomainConstants.DIA
                );
            }

            if (!dish.getRestaurantId().equals(order.getRestaurantId())) {
                throw new DomainException(
                        ErrorCode.DATA_NOT_FOUND,
                        DomainConstants.DNF
                );
            }
        }

        order.setClientId(userId);

        Order order1 = orderPersistencePort.saveOrder(order);

        Trazabilidad trazabilidad = new Trazabilidad();
        trazabilidad.setOrderId(order1.getId());
        trazabilidad.setClientId(String.valueOf(order1.getClientId()));
        trazabilidad.setClientEmail(email);
        trazabilidad.setDate(LocalDateTime.now());
        trazabilidad.setPreviousStatus(DomainConstants.CREATED);
        trazabilidad.setNewStatus(String.valueOf(order1.getStatus()));
        trazabilidad.setEmployeeId(null);
        trazabilidad.setEmployeeEmail(null);
        trazabilidadPersistencePort.saveLog(trazabilidad);
        return order1;
    }

    @Override
    public PageResult<Order> listOrderByStatus(Long userId, String status, int page, int size) {
        Long restaurantId = employeeRestaurantPersistencePort
                .findRestaurantIdByEmployeeUserId(userId)
                .orElseThrow(() -> new DomainException(
                        ErrorCode.DATA_NOT_FOUND, DomainConstants.ENF
                ));

        OrderStatus orderStatus = OrderStatus.from(status);

        return orderPersistencePort.findByRestaurantIdAndStatus(
                restaurantId,
                orderStatus,
                page,
                size
        );
    }

    @Override
    public Order updateStatus(Long userId, Long orderId) {
        Long restaurantId = employeeRestaurantPersistencePort
                .findRestaurantIdByEmployeeUserId(userId)
                .orElseThrow(() -> new DomainException(
                        ErrorCode.DATA_NOT_FOUND, DomainConstants.ENF
                ));

        Order order = orderPersistencePort.findById(orderId)
                .orElseThrow(() -> new DomainException(
                        ErrorCode.DATA_NOT_FOUND, DomainConstants.ONF
                ));

        if(!order.getRestaurantId().equals(restaurantId)){
            throw new DomainException(ErrorCode.UNAUTHORIZED, DomainConstants.NAE);
        }

        String clientEmail = userPersistencePort.getEmailByUserId(order.getClientId());

        Trazabilidad trazabilidad = new Trazabilidad();
        trazabilidad.setOrderId(order.getId());
        trazabilidad.setClientId(String.valueOf(order.getClientId()));
        trazabilidad.setClientEmail(clientEmail);
        trazabilidad.setDate(LocalDateTime.now());
        trazabilidad.setPreviousStatus(order.getStatus().toString());

        OrderDomainValidator.accept(order);
        order.setChefId(userId);

        Order order1 = orderPersistencePort.saveOrder(order);

        String employeeEmail = userPersistencePort.getEmailByUserId(order1.getChefId());

        trazabilidad.setNewStatus(String.valueOf(order1.getStatus()));
        trazabilidad.setEmployeeId(order1.getChefId());
        trazabilidad.setEmployeeEmail(employeeEmail);
        trazabilidadPersistencePort.saveLog(trazabilidad);

        return order1;
    }

    @Override
    @Transactional
    public Order updateStatusReady(Long userId, Long orderId) {
        //Validar que el empleado pertenece a un restaurante
        Long restaurantId = employeeRestaurantPersistencePort
                .findRestaurantIdByEmployeeUserId(userId)
                .orElseThrow(() -> new DomainException(
                        ErrorCode.DATA_NOT_FOUND,
                        DomainConstants.ENF
                ));

        //Buscar la orden
        Order order = orderPersistencePort.findById(orderId)
                .orElseThrow(() -> new DomainException(
                        ErrorCode.DATA_NOT_FOUND,
                        DomainConstants.ONF
                ));
        String previousStatus = order.getStatus().toString();


        //Validar que la orden pertenece al restaurante
        if (!order.getRestaurantId().equals(restaurantId)) {
            throw new DomainException(
                    ErrorCode.UNAUTHORIZED,
                    DomainConstants.NAE
            );
        }

        //Validar que el chef asignado es el mismo
        if (!order.getChefId().equals(userId)) {
            throw new DomainException(
                    ErrorCode.UNAUTHORIZED,
                    DomainConstants.NAE
            );
        }

        String phone = userPersistencePort.getClientPhoneByUserId(order.getClientId());

        //Cambiar estado a LISTO
        OrderDomainValidator.markAsReady(order);

        //Desactivar códigos anteriores activos
        orderCodePersistencePort.deactivateByOrderId(orderId);

        //Generar código OTP
        String rawCode = codeGeneratorPort.generateSixDigits();

        //Crear entidad de dominio DeliveryCode
        DeliveryCode deliveryCode = new DeliveryCode(
                orderId,
                rawCode,
                LocalDateTime.now().plusMinutes(5),
                true
        );

        //Persistir DeliveryCode
        orderCodePersistencePort.saveCode(deliveryCode);

        //Enviar SMS con código plano
        smsPersistencePort.sendSms(
                "+18777804236",
                "Your delivery code is: " + rawCode
        );

        String clientEmail = userPersistencePort.getEmailByUserId(order.getClientId());
        String employeeEmail = userPersistencePort.getEmailByUserId(order.getChefId());

        Trazabilidad trazabilidad = new Trazabilidad();
        trazabilidad.setOrderId(order.getId());
        trazabilidad.setClientId(String.valueOf(order.getClientId()));
        trazabilidad.setClientEmail(clientEmail);
        trazabilidad.setDate(LocalDateTime.now());
        trazabilidad.setPreviousStatus(previousStatus);
        trazabilidad.setNewStatus(order.getStatus().toString());
        trazabilidad.setEmployeeId(order.getChefId());
        trazabilidad.setEmployeeEmail(employeeEmail);
        trazabilidadPersistencePort.saveLog(trazabilidad);
        return orderPersistencePort.saveOrder(order);
    }

    @Override
    @Transactional
    public void updateStatusDelivery(String code, Long userId, Long orderId) {
        Order order = orderPersistencePort.findById(orderId)
                .orElseThrow(() -> new DomainException(
                        ErrorCode.DATA_NOT_FOUND,
                        DomainConstants.ONF
                ));

        String previousStatus = order.getStatus().toString();

        DeliveryCode deliveryCode = deliveryCodePersistencePort.findByOrderId(orderId)
                .orElseThrow(() -> new DomainException(
                        ErrorCode.DATA_NOT_FOUND,
                        DomainConstants.DCNF));

        if (order.getChefId() == null || !order.getChefId().equals(userId)) {
            throw new DomainException(
                    ErrorCode.INVALID_EMPLOYEE,
                    DomainConstants.NAE
            );
        }

        if (!deliveryCode.isActive()) {
            throw new DomainException(
                    ErrorCode.INVALID_CODE,
                    DomainConstants.IDC
            );
        }

        if (!deliveryCode.getExpirationDate().isAfter(LocalDateTime.now())) {
            throw new DomainException(
                    ErrorCode.INVALID_CODE,
                    DomainConstants.IDC
            );
        }

        if (!deliveryCode.getCodeHash().equals(code)) {
            throw new DomainException(
                    ErrorCode.INVALID_CODE,
                    DomainConstants.IDC
            );
        }

        OrderDomainValidator.deliver(order);
        deliveryCode.setActive(false);
        deliveryCodePersistencePort.save(deliveryCode);

        String clientEmail = userPersistencePort.getEmailByUserId(order.getClientId());
        String employeeEmail = userPersistencePort.getEmailByUserId(order.getChefId());

        Trazabilidad trazabilidad = new Trazabilidad();
        trazabilidad.setOrderId(order.getId());
        trazabilidad.setClientId(String.valueOf(order.getClientId()));
        trazabilidad.setClientEmail(clientEmail);
        trazabilidad.setDate(LocalDateTime.now());
        trazabilidad.setPreviousStatus(previousStatus);
        trazabilidad.setNewStatus(order.getStatus().toString());
        trazabilidad.setEmployeeId(order.getChefId());
        trazabilidad.setEmployeeEmail(employeeEmail);
        trazabilidadPersistencePort.saveLog(trazabilidad);

        orderPersistencePort.saveOrder(order);
    }

    @Override
    @Transactional
    public void updateStatusCanceled(Long userId, Long orderId) {
        Order order = orderPersistencePort.findById(orderId)
                .orElseThrow(() -> new DomainException(
                        ErrorCode.DATA_NOT_FOUND,
                        DomainConstants.ONF
                ));

        String previousStatus = order.getStatus().toString();

        if(!order.getClientId().equals(userId)){
            throw new DomainException(
                    ErrorCode.INVALID_CLIENT,
                    DomainConstants.ICP
            );
        }

        try {
            OrderDomainValidator.cancel(order);

            String clientEmail = userPersistencePort.getEmailByUserId(order.getClientId());
            String employeeEmail = userPersistencePort.getEmailByUserId(order.getChefId());

            Trazabilidad trazabilidad = new Trazabilidad();
            trazabilidad.setOrderId(order.getId());
            trazabilidad.setClientId(String.valueOf(order.getClientId()));
            trazabilidad.setClientEmail(clientEmail);
            trazabilidad.setDate(LocalDateTime.now());
            trazabilidad.setPreviousStatus(previousStatus);
            trazabilidad.setNewStatus(order.getStatus().toString());
            trazabilidad.setEmployeeId(order.getChefId());
            trazabilidad.setEmployeeEmail(employeeEmail);
            trazabilidadPersistencePort.saveLog(trazabilidad);

            orderPersistencePort.saveOrder(order);
        } catch (DomainException ex) {
            if (ErrorCode.INVALID_ORDER_STATE.equals(ex.getErrorCode())) {
                smsPersistencePort.sendSms("+18777804236", ex.getMessage());
            }
            throw ex;
        }

    }

    @Override
    public List<Long> getOrdersByRestaurantId(Long restaurantId) {
        restaurantPersistencePort.
                findById(restaurantId).orElseThrow(
                        () -> new DomainException(ErrorCode.DATA_NOT_FOUND, DomainConstants.RNF)
                );

        return orderPersistencePort.getOrdersByRestaurantId(restaurantId);
    }
}
