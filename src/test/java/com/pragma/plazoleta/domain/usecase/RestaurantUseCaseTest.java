package com.pragma.plazoleta.domain.usecase;


import com.pragma.plazoleta.domain.model.Restaurant;
import com.pragma.plazoleta.domain.spi.IRestaurantPersistencePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantUseCaseTest {

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

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
        List<Restaurant> restaurants = List.of(new Restaurant());

        when(restaurantPersistencePort.listRestaurants(0, 10))
                .thenReturn(restaurants);

        List<Restaurant> result =
                restaurantUseCase.listRestaurants(0, 10);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(restaurantPersistencePort).listRestaurants(0, 10);
    }

}
