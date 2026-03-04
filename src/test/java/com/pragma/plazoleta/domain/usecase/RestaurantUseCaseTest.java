package com.pragma.plazoleta.domain.usecase;


import com.pragma.plazoleta.domain.exception.DomainException;
import com.pragma.plazoleta.domain.exception.ErrorCode;
import com.pragma.plazoleta.domain.model.EmployeeRestaurant;
import com.pragma.plazoleta.domain.model.PageResult;
import com.pragma.plazoleta.domain.model.Restaurant;
import com.pragma.plazoleta.domain.spi.IEmployeeRestaurantPersistencePort;
import com.pragma.plazoleta.domain.spi.IRestaurantPersistencePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantUseCaseTest {

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @Mock
    private IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort;

    @InjectMocks
    private RestaurantUseCase restaurantUseCase;

    @Test
    void shouldSaveRestaurantWhenUserIsAdministrator() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Mi Restaurante");

        restaurantUseCase.saveRestaurant(restaurant);
        verify(restaurantPersistencePort).saveRestaurant(restaurant);
        verifyNoMoreInteractions(restaurantPersistencePort);
    }

    @Test
    void shouldListRestaurantsWhenRoleIsClient() {
        int page = 0;
        int size = 10;

        PageResult<Restaurant> restaurants =
                new PageResult<>(
                        List.of(new Restaurant()),
                        page,
                        size,
                        1L
                );

        when(restaurantPersistencePort.listRestaurants(page, size))
                .thenReturn(restaurants);

        PageResult<Restaurant> result =
                restaurantUseCase.listRestaurants(page, size);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());

        verify(restaurantPersistencePort)
                .listRestaurants(page, size);
    }
    @Test
    void shouldThrowExceptionWhenRestaurantNotFound() {

        Long restaurantId = 1L;
        Long userId = 10L;

        when(restaurantPersistencePort.findById(restaurantId))
                .thenReturn(Optional.empty());

        DomainException exception = assertThrows(
                DomainException.class,
                () -> restaurantUseCase.validateOwner(restaurantId, userId)
        );

        assertEquals(ErrorCode.DATA_NOT_FOUND, exception.getErrorCode());

        verify(restaurantPersistencePort).findById(restaurantId);
    }

    @Test
    void shouldThrowUnauthorizedWhenUserIsNotOwner() {

        Long restaurantId = 1L;
        Long ownerId = 5L;
        Long userId = 10L;

        Restaurant restaurant = new Restaurant();
        restaurant.setOwnerId(ownerId);

        when(restaurantPersistencePort.findById(restaurantId))
                .thenReturn(Optional.of(restaurant));

        DomainException exception = assertThrows(
                DomainException.class,
                () -> restaurantUseCase.validateOwner(restaurantId, userId)
        );

        assertEquals(ErrorCode.UNAUTHORIZED, exception.getErrorCode());

        verify(restaurantPersistencePort).findById(restaurantId);
    }

    @Test
    void shouldNotThrowExceptionWhenUserIsOwner() {

        Long restaurantId = 1L;
        Long userId = 10L;

        Restaurant restaurant = new Restaurant();
        restaurant.setOwnerId(userId);

        when(restaurantPersistencePort.findById(restaurantId))
                .thenReturn(Optional.of(restaurant));

        assertDoesNotThrow(() ->
                restaurantUseCase.validateOwner(restaurantId, userId)
        );

        verify(restaurantPersistencePort).findById(restaurantId);
    }
    @Test
    void shouldAssignEmployeeToRestaurant() {

        EmployeeRestaurant employeeRestaurant = new EmployeeRestaurant();

        restaurantUseCase.assignEmployeeToRestaurant(employeeRestaurant);

        verify(employeeRestaurantPersistencePort)
                .save(employeeRestaurant);

        verifyNoMoreInteractions(employeeRestaurantPersistencePort);
    }


}
