package com.pragma.plazoleta.domain.usecase;


import com.pragma.plazoleta.domain.constants.DomainConstants;
import com.pragma.plazoleta.domain.exception.DomainException;
import com.pragma.plazoleta.domain.exception.ErrorCode;
import com.pragma.plazoleta.domain.model.EmployeeRestaurant;
import com.pragma.plazoleta.domain.model.PageResult;
import com.pragma.plazoleta.domain.model.Restaurant;
import com.pragma.plazoleta.domain.spi.IEmployeeRestaurantPersistencePort;
import com.pragma.plazoleta.domain.spi.IRestaurantPersistencePort;
import org.junit.jupiter.api.DisplayName;
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

    @Test
    @DisplayName("Debe retornar la lista de empleados cuando el restaurante existe y el usuario es el dueño")
    void getEmployeeRestaurant_Success() {
        // GIVEN
        Long restaurantId = 1L;
        Long userId = 2L;

        Restaurant restaurant = new Restaurant();
        restaurant.setOwnerId(userId); // El dueño coincide con el userId

        List<Long> expectedEmployees = List.of(10L, 11L);

        when(restaurantPersistencePort.findById(restaurantId))
                .thenReturn(Optional.of(restaurant));

        when(employeeRestaurantPersistencePort.findEmployeeByRestaurantId(restaurantId))
                .thenReturn(expectedEmployees);

        // WHEN
        List<Long> result = restaurantUseCase.getEmployeeRestaurant(restaurantId, userId);

        // THEN
        assertNotNull(result);
        assertEquals(expectedEmployees, result);

        verify(restaurantPersistencePort).findById(restaurantId);
        verify(employeeRestaurantPersistencePort).findEmployeeByRestaurantId(restaurantId);
    }

    @Test
    @DisplayName("Debe lanzar DomainException cuando el restaurante no existe")
    void getEmployeeRestaurant_NotFound() {
        // GIVEN
        Long restaurantId = 1L;
        Long userId = 2L;

        when(restaurantPersistencePort.findById(restaurantId))
                .thenReturn(Optional.empty());

        // WHEN & THEN
        DomainException exception = assertThrows(DomainException.class, () ->
                restaurantUseCase.getEmployeeRestaurant(restaurantId, userId)
        );

        assertEquals(ErrorCode.DATA_NOT_FOUND, exception.getErrorCode());
        assertEquals(DomainConstants.RNF, exception.getMessage());

        verify(employeeRestaurantPersistencePort, never()).findEmployeeByRestaurantId(anyLong());
    }
    @Test
    @DisplayName("Debe lanzar DomainException cuando el userId no es el dueño del restaurante")
    void getEmployeeRestaurant_Unauthorized() {
        // GIVEN
        Long restaurantId = 1L;
        Long userId = 2L; // Usuario que hace la petición
        Long actualOwnerId = 5L; // Dueño real diferente

        Restaurant restaurant = new Restaurant();
        restaurant.setOwnerId(actualOwnerId);

        when(restaurantPersistencePort.findById(restaurantId))
                .thenReturn(Optional.of(restaurant));

        // WHEN & THEN
        DomainException exception = assertThrows(DomainException.class, () ->
                restaurantUseCase.getEmployeeRestaurant(restaurantId, userId)
        );

        // Validamos el error de autorización según tu código
        assertEquals(ErrorCode.UNAUTHORIZED, exception.getErrorCode());
        assertEquals(DomainConstants.UNO, exception.getMessage());

        verify(employeeRestaurantPersistencePort, never()).findEmployeeByRestaurantId(anyLong());
    }


}
