package com.pragma.plazoleta.domain.usecase;

import com.pragma.plazoleta.domain.exception.DomainException;
import com.pragma.plazoleta.domain.exception.ErrorCode;
import com.pragma.plazoleta.domain.model.*;
import com.pragma.plazoleta.domain.spi.IDishPersistencePort;
import com.pragma.plazoleta.domain.spi.IEmployeeRestaurantPersistencePort;
import com.pragma.plazoleta.domain.spi.IOrderPersistencePort;
import com.pragma.plazoleta.domain.spi.IRestaurantPersistencePort;
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

        assertEquals(ErrorCode.INVALID_DISH, exception.getErrorCode());
        assertEquals("Dish does not belong to the restaurant", exception.getMessage());

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
        assertEquals("Employee does not belong to any restaurant", exception.getMessage());

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
        Page<Order> pageResult =
                new PageImpl<>(List.of(order));

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

        Page<Order> result =
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



}
