package com.pragma.plazoleta.application.mapper;


import com.pragma.plazoleta.application.dto.request.RestaurantRequestDto;
import com.pragma.plazoleta.domain.model.Restaurant;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

public class IRestaurantRequestMapperTest {
    private final IRestaurantRequestMapper mapper =
            Mappers.getMapper(IRestaurantRequestMapper.class);

    @Test
    void shouldMapRestaurantRequestDtoToRestaurantCorrectly() {
        // arrange
        RestaurantRequestDto dto = new RestaurantRequestDto();
        dto.setName("Restaurante Test");
        dto.setNit("123456789");
        dto.setAddress("Calle 123");
        dto.setPhoneNumber("+573001234567");
        dto.setLogoUrl("https://logo.com/logo.png");
        dto.setOwnerId(10L);

        // act
        Restaurant restaurant = mapper.toRestaurant(dto);

        // assert
        assertNotNull(restaurant);
        assertEquals("Restaurante Test", restaurant.getName());
        assertEquals("123456789", restaurant.getNit());
        assertEquals("Calle 123", restaurant.getAddress());
        assertEquals("+573001234567", restaurant.getPhoneNumber());
        assertEquals("https://logo.com/logo.png", restaurant.getLogoUrl());
        assertEquals(10L, restaurant.getOwnerId());
    }

    @Test
    void shouldReturnNullWhenDtoIsNull() {
        Restaurant restaurant = mapper.toRestaurant(null);
        assertNull(restaurant);
    }
}
