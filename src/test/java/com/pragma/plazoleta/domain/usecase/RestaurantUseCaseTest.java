package com.pragma.plazoleta.domain.usecase;


import com.pragma.plazoleta.domain.exception.DomainException;
import com.pragma.plazoleta.domain.exception.ErrorCode;
import com.pragma.plazoleta.domain.model.EmployeeForRestaurantCommand;
import com.pragma.plazoleta.domain.model.Restaurant;
import com.pragma.plazoleta.domain.model.Rol;
import com.pragma.plazoleta.domain.spi.IEmployeeRestaurantPersistencePort;
import com.pragma.plazoleta.domain.spi.IRestaurantPersistencePort;
import com.pragma.plazoleta.domain.spi.IUserPersistencePort;
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

    @InjectMocks
    private RestaurantUseCase restaurantUseCase;

    @Mock
    private IUserPersistencePort userServicePort;

    @Mock
    private IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort;

    @Test
    void shouldSaveRestaurantWhenUserIsAdministrator() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Mi Restaurante");

        Long userId = 1L;
        String rol = Rol.ADMINISTRADOR.name();

        restaurantUseCase.saveRestaurant(restaurant, userId, rol);
        verify(restaurantPersistencePort).saveRestaurant(restaurant);
        verifyNoMoreInteractions(restaurantPersistencePort);
    }

    @Test
    void shouldThrowUserNotRolExceptionWhenUserIsNotAdministrator() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Mi Restaurante");

        Long userId = 2L;
        String rol = Rol.PROPIETARIO.name();

        DomainException exception = assertThrows(
                DomainException.class,
                () -> restaurantUseCase.saveRestaurant(restaurant, userId, rol)
        );

        assertEquals(ErrorCode.UNAUTHORIZED, exception.getErrorCode());
        assertEquals(
                "Only a admin can create dishes",
                exception.getMessage()
        );


        verifyNoInteractions(restaurantPersistencePort);
    }

    @Test
    void shouldThrowUnauthorizedWhenRoleIsNotOwner() {
        EmployeeForRestaurantCommand employee = buildValidEmployee();

        DomainException exception = assertThrows(
                DomainException.class,
                () -> restaurantUseCase.saveEmployee(
                        employee,
                        Rol.ADMINISTRADOR.name()
                )
        );

        assertEquals(ErrorCode.UNAUTHORIZED, exception.getErrorCode());
        assertEquals(
                "Only a restaurant owner can create employees",
                exception.getMessage()
        );

        verifyNoInteractions(
                restaurantPersistencePort,
                userServicePort,
                employeeRestaurantPersistencePort
        );
    }

    @Test
    void shouldThrowExceptionWhenRestaurantNotFound() {
        EmployeeForRestaurantCommand employee = buildValidEmployee();

        when(restaurantPersistencePort.findById(employee.getRestaurantId()))
                .thenReturn(Optional.empty());

        DomainException exception = assertThrows(
                DomainException.class,
                () -> restaurantUseCase.saveEmployee(
                        employee,
                        Rol.PROPIETARIO.name()
                )
        );

        assertEquals(ErrorCode.DATA_NOT_FOUND, exception.getErrorCode());
        assertEquals("Restaurant not found", exception.getMessage());

        verify(restaurantPersistencePort).findById(employee.getRestaurantId());
        verifyNoInteractions(userServicePort, employeeRestaurantPersistencePort);
    }

    @Test
    void shouldThrowForbiddenWhenRestaurantDoesNotBelongToOwner() {
        EmployeeForRestaurantCommand employee = buildValidEmployee();

        Restaurant restaurant = new Restaurant();
        restaurant.setId(employee.getRestaurantId());
        restaurant.setOwnerId(99L);

        when(restaurantPersistencePort.findById(employee.getRestaurantId()))
                .thenReturn(Optional.of(restaurant));

        DomainException exception = assertThrows(
                DomainException.class,
                () -> restaurantUseCase.saveEmployee(
                        employee,
                        Rol.PROPIETARIO.name()
                )
        );

        assertEquals(ErrorCode.FORBIDDEN, exception.getErrorCode());
        assertEquals(
                "User role is not authorized to create employees",
                exception.getMessage()
        );

        verify(restaurantPersistencePort).findById(employee.getRestaurantId());
        verifyNoInteractions(userServicePort, employeeRestaurantPersistencePort);
    }

    @Test
    void shouldCreateEmployeeSuccessfullyWhenOwnerIsValid() {
        EmployeeForRestaurantCommand employee = buildValidEmployee();

        Restaurant restaurant = new Restaurant();
        restaurant.setId(employee.getRestaurantId());
        restaurant.setOwnerId(employee.getOwnerId());

        when(restaurantPersistencePort.findById(employee.getRestaurantId()))
                .thenReturn(Optional.of(restaurant));

        when(userServicePort.createEmployee(any()))
                .thenReturn(100L);

        restaurantUseCase.saveEmployee(
                employee,
                Rol.PROPIETARIO.name()
        );

        verify(userServicePort).createEmployee(any());
        verify(employeeRestaurantPersistencePort)
                .save(any());

        verify(restaurantPersistencePort)
                .findById(employee.getRestaurantId());
    }

    @Test
    void shouldThrowUnauthorizedWhenRoleIsNotClient() {
        DomainException exception = assertThrows(
                DomainException.class,
                () -> restaurantUseCase.listRestaurants(
                        0,
                        10,
                        Rol.PROPIETARIO.name()
                )
        );

        assertEquals(ErrorCode.UNAUTHORIZED, exception.getErrorCode());
        verifyNoInteractions(restaurantPersistencePort);
    }

    @Test
    void shouldListRestaurantsWhenRoleIsClient() {
        List<Restaurant> restaurants = List.of(new Restaurant());

        when(restaurantPersistencePort.listRestaurants(0, 10))
                .thenReturn(restaurants);

        List<Restaurant> result =
                restaurantUseCase.listRestaurants(0, 10, Rol.CLIENTE.name());

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(restaurantPersistencePort).listRestaurants(0, 10);
    }


    private EmployeeForRestaurantCommand buildValidEmployee() {
        return new EmployeeForRestaurantCommand(
                10L,
                5L,
                "Juan",
                "Perez",
                "123456789",
                "+573001234567",
                "juan@mail.com",
                "password123"
        );
    }
}
