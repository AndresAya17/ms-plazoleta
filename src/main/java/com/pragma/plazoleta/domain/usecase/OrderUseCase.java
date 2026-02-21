package com.pragma.plazoleta.domain.usecase;

import com.pragma.plazoleta.domain.api.IOrderServicePort;
import com.pragma.plazoleta.domain.exception.DomainException;
import com.pragma.plazoleta.domain.exception.ErrorCode;
import com.pragma.plazoleta.domain.model.Dish;
import com.pragma.plazoleta.domain.model.Order;
import com.pragma.plazoleta.domain.model.OrderItem;
import com.pragma.plazoleta.domain.model.OrderStatus;
import com.pragma.plazoleta.domain.spi.IDishPersistencePort;
import com.pragma.plazoleta.domain.spi.IEmployeeRestaurantPersistencePort;
import com.pragma.plazoleta.domain.spi.IOrderPersistencePort;
import com.pragma.plazoleta.domain.spi.IRestaurantPersistencePort;
import com.pragma.plazoleta.domain.validator.OrderDomainValidator;
import org.springframework.data.domain.Page;

public class OrderUseCase implements IOrderServicePort {

    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IDishPersistencePort dishPersistencePort;
    private final IOrderPersistencePort orderPersistencePort;
    private final IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort;

    public OrderUseCase(IRestaurantPersistencePort restaurantPersistencePort, IDishPersistencePort dishPersistencePort, IOrderPersistencePort orderPersistencePort,
                        IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort){
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.dishPersistencePort = dishPersistencePort;
        this.orderPersistencePort = orderPersistencePort;
        this.employeeRestaurantPersistencePort = employeeRestaurantPersistencePort;
    }

    @Override
    public Order saveOrder(Order order, Long userId) {
        restaurantPersistencePort.findById(order.getRestaurantId())
                .orElseThrow(() -> new DomainException(ErrorCode.DATA_NOT_FOUND, "Restaurant not found"));
        for (OrderItem item : order.getItems()) {

            Dish dish = dishPersistencePort.findById(item.getDishId())
                    .orElseThrow(() -> new DomainException(
                            ErrorCode.INVALID_DISH,
                            "Dish not found"));

            if(!dish.isActive()){
                throw new DomainException(
                        ErrorCode.INVALID_DISH,
                        "Dish is inactive"
                );
            }

            if (!dish.getRestaurantId().equals(order.getRestaurantId())) {
                throw new DomainException(
                        ErrorCode.INVALID_DISH,
                        "Dish does not belong to the restaurant"
                );
            }
        }
        order.setClientId(userId);
        return orderPersistencePort.saveOrder(order);
    }

    @Override
    public Page<Order> listOrderByStatus(Long userId, String status, int page, int size) {
        Long restaurantId = employeeRestaurantPersistencePort
                .findRestaurantIdByEmployeeUserId(userId)
                .orElseThrow(() -> new DomainException(
                        ErrorCode.DATA_NOT_FOUND, "Employee does not belong to any restaurant"
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
                        ErrorCode.DATA_NOT_FOUND, "Employee does not belong to any restaurant"
                ));

        Order order = orderPersistencePort.findById(orderId)
                .orElseThrow(() -> new DomainException(
                        ErrorCode.DATA_NOT_FOUND, "Order not found"
                ));

        if(!order.getRestaurantId().equals(restaurantId)){
            throw new DomainException(ErrorCode.UNAUTHORIZED, "The employee is not authorized to manage this order");
        }
        OrderDomainValidator.accept(order);
        order.setChefId(userId);
        return orderPersistencePort.saveOrder(order);
    }

    @Override
    public Order updateStatusReady(Long userId, Long orderId) {
        Long restaurantId = employeeRestaurantPersistencePort
                .findRestaurantIdByEmployeeUserId(userId)
                .orElseThrow(() -> new DomainException(
                        ErrorCode.DATA_NOT_FOUND, "Employee does not belong to any restaurant"
                ));

        Order order = orderPersistencePort.findById(orderId)
                .orElseThrow(() -> new DomainException(
                        ErrorCode.DATA_NOT_FOUND, "Order not found"
                ));

        if(!order.getRestaurantId().equals(restaurantId)){
            throw new DomainException(ErrorCode.UNAUTHORIZED, "The employee is not authorized to manage this order");
        }

        if(!order.getChefId().equals(userId)){
            throw new DomainException(ErrorCode.UNAUTHORIZED, "Employee is not assigned to this order");
        }
        OrderDomainValidator.markAsReady(order);
        //Consumir api de twlio
        return orderPersistencePort.saveOrder(order);
    }
}
