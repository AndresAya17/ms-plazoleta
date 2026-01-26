package com.pragma.plazoleta.domain.usecase;


import com.pragma.plazoleta.domain.model.Restaurant;
import com.pragma.plazoleta.domain.spi.IRestaurantPersistencePort;
import com.pragma.plazoleta.domain.spi.IUserOwnerValidationPort;
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
    private IUserOwnerValidationPort userOwnerValidationPort;

    @InjectMocks
    private RestaurantUseCase restaurantUseCase;

    @Test
    void shouldSaveRestaurantWhenUserIsOwner() {
        // arrange
        Restaurant restaurant = new Restaurant();
        restaurant.setOwnerId(1L);

        when(userOwnerValidationPort.isOwner(1L)).thenReturn(true);

        // act
        restaurantUseCase.saveRestaurant(restaurant);

        // assert
        verify(userOwnerValidationPort, times(1)).isOwner(1L);
        verify(restaurantPersistencePort, times(1)).saveRestaurant(restaurant);
        verifyNoMoreInteractions(userOwnerValidationPort, restaurantPersistencePort);
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotOwner() {
        // arrange
        Restaurant restaurant = new Restaurant();
        restaurant.setOwnerId(2L);

        when(userOwnerValidationPort.isOwner(2L)).thenReturn(false);

        // act & assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> restaurantUseCase.saveRestaurant(restaurant)
        );

        assertEquals("El usuario no es propietario", exception.getMessage());

        verify(userOwnerValidationPort, times(1)).isOwner(2L);
        verifyNoInteractions(restaurantPersistencePort);
    }
}
