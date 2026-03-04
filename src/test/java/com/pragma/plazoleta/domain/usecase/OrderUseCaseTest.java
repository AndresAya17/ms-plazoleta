package com.pragma.plazoleta.domain.usecase;

import com.pragma.plazoleta.domain.constants.DomainConstants;
import com.pragma.plazoleta.domain.exception.DomainException;
import com.pragma.plazoleta.domain.exception.ErrorCode;
import com.pragma.plazoleta.domain.model.*;
import com.pragma.plazoleta.domain.spi.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class OrderUseCaseTest {
    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @Mock
    private IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort;

    @Mock
    private IDishPersistencePort dishPersistencePort;

    @Mock
    private IOrderPersistencePort orderPersistencePort;

    @Mock
    private IUserPersistencePort userPersistencePort;

    @Mock
    private ISmsPersistencePort smsPersistencePort;

    @Mock
    private ICodeGeneratorPort codeGeneratorPort;

    @Mock
    private IOrderCodePersistencePort orderCodePersistencePort;

    @InjectMocks
    private OrderUseCase orderUseCase;

    @Test
    void shouldSaveOrderSuccessfully() {
        Long userId = 10L;
        Long restaurantId = 1L;
        Long dishId = 5L;

        OrderItem item = new OrderItem(dishId, 1);

        Order order = new Order(
                null,
                restaurantId,
                List.of(item)
        );

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);

        Dish dish = new Dish();
        dish.setId(dishId);
        dish.setActive(true);
        dish.setRestaurantId(restaurantId);

        when(restaurantPersistencePort.findById(restaurantId))
                .thenReturn(Optional.of(restaurant));

        when(dishPersistencePort.findById(dishId))
                .thenReturn(Optional.of(dish));

        when(orderPersistencePort.saveOrder(order))
                .thenReturn(order);

        Order result = orderUseCase.saveOrder(order, userId);

        assertNotNull(result);
        assertEquals(userId, order.getClientId());

        verify(orderPersistencePort).saveOrder(order);
    }

    @Test
    void shouldThrowExceptionWhenRestaurantNotFound() {
        Long userId = 10L;
        Long restaurantId = 1L;

        OrderItem item = new OrderItem(5L, 1);
        Order order = new Order(null, restaurantId, List.of(item));

        when(restaurantPersistencePort.findById(restaurantId))
                .thenReturn(Optional.empty());

        DomainException exception = assertThrows(
                DomainException.class,
                () -> orderUseCase.saveOrder(order, userId)
        );

        assertEquals(ErrorCode.DATA_NOT_FOUND, exception.getErrorCode());
        assertEquals("Restaurant not found", exception.getMessage());

        verify(restaurantPersistencePort).findById(restaurantId);
        verifyNoInteractions(dishPersistencePort, orderPersistencePort);
    }
    @Test
    void shouldThrowExceptionWhenDishNotFound() {
        Long userId = 10L;
        Long restaurantId = 1L;
        Long dishId = 5L;

        OrderItem item = new OrderItem(dishId, 1);
        Order order = new Order(null, restaurantId, List.of(item));

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);

        when(restaurantPersistencePort.findById(restaurantId))
                .thenReturn(Optional.of(restaurant));

        when(dishPersistencePort.findById(dishId))
                .thenReturn(Optional.empty());

        DomainException exception = assertThrows(
                DomainException.class,
                () -> orderUseCase.saveOrder(order, userId)
        );

        assertEquals(ErrorCode.INVALID_DISH, exception.getErrorCode());
        assertEquals("Dish not found", exception.getMessage());

        verify(dishPersistencePort).findById(dishId);
        verify(orderPersistencePort, never()).saveOrder(any());
    }
    @Test
    void shouldThrowExceptionWhenDishIsInactive() {
        Long userId = 10L;
        Long restaurantId = 1L;
        Long dishId = 5L;

        OrderItem item = new OrderItem(dishId, 1);
        Order order = new Order(null, restaurantId, List.of(item));

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);

        Dish dish = new Dish();
        dish.setId(dishId);
        dish.setActive(false);
        dish.setRestaurantId(restaurantId);

        when(restaurantPersistencePort.findById(restaurantId))
                .thenReturn(Optional.of(restaurant));

        when(dishPersistencePort.findById(dishId))
                .thenReturn(Optional.of(dish));

        DomainException exception = assertThrows(
                DomainException.class,
                () -> orderUseCase.saveOrder(order, userId)
        );

        assertEquals(ErrorCode.INVALID_DISH, exception.getErrorCode());
        assertEquals("Dish is inactive", exception.getMessage());

        verify(orderPersistencePort, never()).saveOrder(any());
    }
    @Test
    void shouldThrowExceptionWhenDishDoesNotBelongToRestaurant() {
        Long userId = 10L;
        Long restaurantId = 1L;
        Long dishRestaurantId = 2L;
        Long dishId = 5L;

        OrderItem item = new OrderItem(dishId, 1);
        Order order = new Order(null, restaurantId, List.of(item));

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);

        Dish dish = new Dish();
        dish.setId(dishId);
        dish.setActive(true);
        dish.setRestaurantId(dishRestaurantId);

        when(restaurantPersistencePort.findById(restaurantId))
                .thenReturn(Optional.of(restaurant));

        when(dishPersistencePort.findById(dishId))
                .thenReturn(Optional.of(dish));

        DomainException exception = assertThrows(
                DomainException.class,
                () -> orderUseCase.saveOrder(order, userId)
        );

        assertEquals(ErrorCode.DATA_NOT_FOUND, exception.getErrorCode());
        assertEquals("Dish not found", exception.getMessage());

        verify(orderPersistencePort, never()).saveOrder(any());
    }
    @Test
    void shouldThrowExceptionWhenEmployeeHasNoRestaurant() {

        Long userId = 10L;

        when(employeeRestaurantPersistencePort
                .findRestaurantIdByEmployeeUserId(userId))
                .thenReturn(Optional.empty());

        DomainException exception = assertThrows(
                DomainException.class,
                () -> orderUseCase.listOrderByStatus(userId, "PENDIENTE", 0, 10)
        );

        assertEquals(ErrorCode.DATA_NOT_FOUND, exception.getErrorCode());
        assertEquals(DomainConstants.ENF, exception.getMessage());

        verify(employeeRestaurantPersistencePort)
                .findRestaurantIdByEmployeeUserId(userId);

        verifyNoInteractions(orderPersistencePort);
    }

    @Test
    void shouldThrowExceptionWhenStatusIsInvalid() {

        Long userId = 10L;
        Long restaurantId = 1L;

        when(employeeRestaurantPersistencePort
                .findRestaurantIdByEmployeeUserId(userId))
                .thenReturn(Optional.of(restaurantId));

        DomainException exception = assertThrows(
                DomainException.class,
                () -> orderUseCase.listOrderByStatus(userId, "INVALID", 0, 10)
        );

        assertEquals(ErrorCode.INVALID_STATUS, exception.getErrorCode());

        verify(employeeRestaurantPersistencePort)
                .findRestaurantIdByEmployeeUserId(userId);

        verifyNoInteractions(orderPersistencePort);
    }

    @Test
    void shouldReturnOrdersWhenDataIsValid() {

        Long userId = 10L;
        Long restaurantId = 1L;
        String status = "PENDIENTE";
        int page = 0;
        int size = 5;

        Order order = mock(Order.class);
        PageResult<Order> pageResult =
                new PageResult<>(
                        List.of(order),
                        page,
                        size,
                        1L
                );

        when(employeeRestaurantPersistencePort
                .findRestaurantIdByEmployeeUserId(userId))
                .thenReturn(Optional.of(restaurantId));

        when(orderPersistencePort
                .findByRestaurantIdAndStatus(
                        restaurantId,
                        OrderStatus.PENDIENTE,
                        page,
                        size
                ))
                .thenReturn(pageResult);

        PageResult<Order> result =
                orderUseCase.listOrderByStatus(userId, status, page, size);

        assertEquals(1, result.getTotalElements());

        verify(employeeRestaurantPersistencePort)
                .findRestaurantIdByEmployeeUserId(userId);

        verify(orderPersistencePort)
                .findByRestaurantIdAndStatus(
                        restaurantId,
                        OrderStatus.PENDIENTE,
                        page,
                        size
                );
    }

    @Test
    void shouldThrowWhenEmployeeDoesNotBelongToAnyRestaurant() {

        Long userId = 10L;
        Long orderId = 1L;

        when(employeeRestaurantPersistencePort
                .findRestaurantIdByEmployeeUserId(userId))
                .thenReturn(Optional.empty());

        DomainException exception = assertThrows(
                DomainException.class,
                () -> orderUseCase.updateStatus(userId, orderId)
        );

        assertEquals(ErrorCode.DATA_NOT_FOUND, exception.getErrorCode());

        verify(employeeRestaurantPersistencePort)
                .findRestaurantIdByEmployeeUserId(userId);

        verifyNoMoreInteractions(orderPersistencePort);
    }

    @Test
    void shouldThrowWhenOrderNotFound() {

        Long userId = 10L;
        Long orderId = 1L;
        Long restaurantId = 5L;

        when(employeeRestaurantPersistencePort
                .findRestaurantIdByEmployeeUserId(userId))
                .thenReturn(Optional.of(restaurantId));

        when(orderPersistencePort.findById(orderId))
                .thenReturn(Optional.empty());

        DomainException exception = assertThrows(
                DomainException.class,
                () -> orderUseCase.updateStatus(userId, orderId)
        );

        assertEquals(ErrorCode.DATA_NOT_FOUND, exception.getErrorCode());

        verify(employeeRestaurantPersistencePort)
                .findRestaurantIdByEmployeeUserId(userId);

        verify(orderPersistencePort)
                .findById(orderId);
    }

    @Test
    void shouldThrowWhenOrderBelongsToAnotherRestaurant() {

        Long userId = 10L;
        Long orderId = 1L;
        Long employeeRestaurantId = 5L;
        Long orderRestaurantId = 99L;

        Order order = mock(Order.class);

        when(employeeRestaurantPersistencePort
                .findRestaurantIdByEmployeeUserId(userId))
                .thenReturn(Optional.of(employeeRestaurantId));

        when(orderPersistencePort.findById(orderId))
                .thenReturn(Optional.of(order));

        when(order.getRestaurantId())
                .thenReturn(orderRestaurantId);

        DomainException exception = assertThrows(
                DomainException.class,
                () -> orderUseCase.updateStatus(userId, orderId)
        );

        assertEquals(ErrorCode.UNAUTHORIZED, exception.getErrorCode());

        verify(order).getRestaurantId();
    }

    @Test
    void shouldUpdateOrderWhenDataIsValid() {

        Long userId = 10L;
        Long orderId = 1L;
        Long restaurantId = 5L;

        Order order = new Order(
                20L,
                restaurantId,
                List.of()
        );

        when(employeeRestaurantPersistencePort
                .findRestaurantIdByEmployeeUserId(userId))
                .thenReturn(Optional.of(restaurantId));

        when(orderPersistencePort.findById(orderId))
                .thenReturn(Optional.of(order));

        when(orderPersistencePort.saveOrder(order))
                .thenReturn(order);

        Order result = orderUseCase.updateStatus(userId, orderId);

        assertNotNull(result);

        verify(employeeRestaurantPersistencePort)
                .findRestaurantIdByEmployeeUserId(userId);

        verify(orderPersistencePort)
                .findById(orderId);

        verify(orderPersistencePort)
                .saveOrder(order);
    }

    @Test
    void shouldUpdateStatusReadySuccessfully() {

        Long userId = 5L;
        Long orderId = 1L;
        Long restaurantId = 100L;
        Long clientId = 20L;

        Order order = new Order(
                clientId,
                restaurantId,
                List.of()
        );

        order.setId(orderId);
        order.setChefId(userId);
        order.setStatus(OrderStatus.EN_PREPARACION);


        when(employeeRestaurantPersistencePort.findRestaurantIdByEmployeeUserId(userId))
                .thenReturn(Optional.of(restaurantId));

        when(orderPersistencePort.findById(orderId))
                .thenReturn(Optional.of(order));

        when(userPersistencePort.getClientPhoneByUserId(clientId))
                .thenReturn("+573001234567");

        when(codeGeneratorPort.generateSixDigits())
                .thenReturn("123456");

        when(orderPersistencePort.saveOrder(order))
                .thenReturn(order);

        Order result = orderUseCase.updateStatusReady(userId, orderId);

        assertEquals(OrderStatus.LISTO, result.getStatus());

        verify(orderCodePersistencePort).deactivateByOrderId(orderId);
        verify(orderCodePersistencePort).saveCode(any(DeliveryCode.class));
        verify(smsPersistencePort)
                .sendSms(eq("+18777804236"), contains("123456"));
        verify(orderPersistencePort).saveOrder(order);
    }


    @Test
    void shouldThrowWhenOrderNotFromRestaurant() {

        Long userId = 5L;
        Long orderId = 1L;

        Order order = new Order(20L, 999L, List.of()); // restaurant distinto
        order.setChefId(userId);

        when(employeeRestaurantPersistencePort.findRestaurantIdByEmployeeUserId(userId))
                .thenReturn(Optional.of(100L));

        when(orderPersistencePort.findById(orderId))
                .thenReturn(Optional.of(order));

        DomainException ex = assertThrows(
                DomainException.class,
                () -> orderUseCase.updateStatusReady(userId, orderId)
        );

        assertEquals(ErrorCode.UNAUTHORIZED, ex.getErrorCode());
        assertEquals("The employee is not allowed to manage this order", ex.getMessage());
    }

    @Test
    void shouldThrowWhenEmployeeIsNotAssignedChef() {

        Long userId = 5L;
        Long orderId = 1L;

        Order order = new Order(20L, 100L, List.of());
        order.setChefId(99L); // diferente

        when(employeeRestaurantPersistencePort.findRestaurantIdByEmployeeUserId(userId))
                .thenReturn(Optional.of(100L));

        when(orderPersistencePort.findById(orderId))
                .thenReturn(Optional.of(order));

        DomainException ex = assertThrows(
                DomainException.class,
                () -> orderUseCase.updateStatusReady(userId, orderId)
        );

        assertEquals(ErrorCode.UNAUTHORIZED, ex.getErrorCode());
        assertEquals("The employee is not allowed to manage this order", ex.getMessage());
    }


    @Test
    @DisplayName("Debe lanzar excepción cuando el empleado no pertenece a un restaurante")
    void shouldThrowExceptionWhenEmployeeNotBelongToRestaurant() {

        Long userId = 1L;
        Long orderId = 10L;

        when(employeeRestaurantPersistencePort
                .findRestaurantIdByEmployeeUserId(userId))
                .thenReturn(Optional.empty());

        DomainException exception = assertThrows(
                DomainException.class,
                () -> orderUseCase.updateStatusReady(userId, orderId)
        );

        assertEquals(ErrorCode.DATA_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando la orden no existe")
    void shouldThrowExceptionWhenOrderNotFound() {

        Long userId = 1L;
        Long orderId = 10L;

        when(employeeRestaurantPersistencePort
                .findRestaurantIdByEmployeeUserId(userId))
                .thenReturn(Optional.of(1L));

        when(orderPersistencePort.findById(orderId))
                .thenReturn(Optional.empty());

        DomainException exception = assertThrows(
                DomainException.class,
                () -> orderUseCase.updateStatusReady(userId, orderId)
        );

        assertEquals(ErrorCode.DATA_NOT_FOUND, exception.getErrorCode());
    }





}
