package com.pragma.plazoleta.domain.usecase;


import com.pragma.plazoleta.domain.exception.DomainException;
import com.pragma.plazoleta.domain.exception.ErrorCode;
import com.pragma.plazoleta.domain.model.Restaurant;
import com.pragma.plazoleta.domain.model.Rol;
import com.pragma.plazoleta.domain.spi.IRestaurantPersistencePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RestaurantUseCaseTest {

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @InjectMocks
    private RestaurantUseCase restaurantUseCase;

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
}
