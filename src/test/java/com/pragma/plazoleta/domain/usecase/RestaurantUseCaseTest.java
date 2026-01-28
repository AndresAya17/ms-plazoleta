package com.pragma.plazoleta.domain.usecase;


import com.pragma.plazoleta.domain.exception.UserNotRolException;
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

    @Mock
    private IUserValidationPort userOwnerValidationPort;

    @InjectMocks
    private RestaurantUseCase restaurantUseCase;

    @Test
    void shouldSaveRestaurantWhenUserIsAdministrator() {
        // arrange
        Restaurant restaurant = new Restaurant();
        restaurant.setOwnerId(1L);

        when(userOwnerValidationPort.getUserRol(1L))
                .thenReturn(Rol.ADMINISTRADOR);

        // act
        restaurantUseCase.saveRestaurant(restaurant);

        // assert
        verify(userOwnerValidationPort).getUserRol(1L);
        verify(restaurantPersistencePort).saveRestaurant(restaurant);
        verifyNoMoreInteractions(userOwnerValidationPort, restaurantPersistencePort);
    }

    @Test
    void shouldThrowUserNotRolExceptionWhenUserIsNotAdministrator() {
        // arrange
        Restaurant restaurant = new Restaurant();
        restaurant.setOwnerId(2L);

        when(userOwnerValidationPort.getUserRol(2L))
                .thenReturn(Rol.PROPIETARIO);

        // act & assert
        assertThrows(
                UserNotRolException.class,
                () -> restaurantUseCase.saveRestaurant(restaurant)
        );

        verify(userOwnerValidationPort).getUserRol(2L);
        verifyNoInteractions(restaurantPersistencePort);
    }
}
