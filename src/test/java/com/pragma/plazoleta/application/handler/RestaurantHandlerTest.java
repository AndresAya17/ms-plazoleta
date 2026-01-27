package com.pragma.plazoleta.application.handler;


import com.pragma.plazoleta.application.dto.request.RestaurantRequestDto;
import com.pragma.plazoleta.application.handler.impl.RestaurantHandler;
import com.pragma.plazoleta.application.mapper.IRestaurantRequestMapper;
import com.pragma.plazoleta.domain.api.IRestaurantServicePort;
import com.pragma.plazoleta.domain.model.Restaurant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RestaurantHandlerTest {
    @Mock
    private IRestaurantServicePort restaurantServicePort;

    @Mock
    private IRestaurantRequestMapper restaurantRequestMapper;

    @InjectMocks
    private RestaurantHandler restaurantHandler;

    @Test
    void shouldMapDtoAndCallServiceWhenSaveRestaurantIsInvoked() {
        // arrange
        RestaurantRequestDto dto = new RestaurantRequestDto();
        dto.setName("Restaurante Test");

        Restaurant restaurant = new Restaurant();
        restaurant.setName("Restaurante Test");

        when(restaurantRequestMapper.toRestaurant(dto)).thenReturn(restaurant);

        // act
        restaurantHandler.saveRestaurant(dto);

        // assert
        verify(restaurantRequestMapper, times(1)).toRestaurant(dto);
        verify(restaurantServicePort, times(1)).saveRestaurant(restaurant);
        verifyNoMoreInteractions(restaurantRequestMapper, restaurantServicePort);
    }
}
