package com.pragma.plazoleta.infrastructure.out.jpa.mapper;

import com.pragma.plazoleta.domain.model.Restaurant;
import com.pragma.plazoleta.infrastructure.out.jpa.entity.RestaurantEntity;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;
public class IRestaurantEntityMapperTest {

    private final IRestaurantEntityMapper mapper =
            Mappers.getMapper(IRestaurantEntityMapper.class);

    @Test
    void shouldMapRestaurantToRestaurantEntityCorrectly() {
        // arrange
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Restaurante Test");
        restaurant.setNit("123456789");
        restaurant.setAddress("Calle 123");
        restaurant.setPhoneNumber("+573001234567");
        restaurant.setLogoUrl("https://logo.com/logo.png");
        restaurant.setOwnerId(10L);

        // act
        RestaurantEntity entity = mapper.toEntity(restaurant);

        // assert
        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals("Restaurante Test", entity.getName());
        assertEquals("123456789", entity.getNit());
        assertEquals("Calle 123", entity.getAddress());
        assertEquals("+573001234567", entity.getPhoneNumber());
        assertEquals("https://logo.com/logo.png", entity.getLogoUrl());
        assertEquals(10L, entity.getOwnerId());
    }

    @Test
    void shouldMapRestaurantEntityToRestaurantCorrectly() {
        // arrange
        RestaurantEntity entity = new RestaurantEntity();
        entity.setId(2L);
        entity.setName("Restaurante Entity");
        entity.setNit("987654321");
        entity.setAddress("Carrera 45");
        entity.setPhoneNumber("+573009876543");
        entity.setLogoUrl("https://logo.com/entity.png");
        entity.setOwnerId(20L);

        // act
        Restaurant restaurant = mapper.toRestaurant(entity);

        // assert
        assertNotNull(restaurant);
        assertEquals(2L, restaurant.getId());
        assertEquals("Restaurante Entity", restaurant.getName());
        assertEquals("987654321", restaurant.getNit());
        assertEquals("Carrera 45", restaurant.getAddress());
        assertEquals("+573009876543", restaurant.getPhoneNumber());
        assertEquals("https://logo.com/entity.png", restaurant.getLogoUrl());
        assertEquals(20L, restaurant.getOwnerId());
    }

    @Test
    void shouldReturnNullWhenRestaurantIsNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void shouldReturnNullWhenRestaurantEntityIsNull() {
        assertNull(mapper.toRestaurant(null));
    }
}
