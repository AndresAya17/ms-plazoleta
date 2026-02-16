package com.pragma.plazoleta.domain.model;


import com.pragma.plazoleta.domain.exception.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class RestaurantTest {

    @Test
    void shouldSetAndGetAllFieldsCorrectly() {
        Restaurant restaurant = new Restaurant();

        restaurant.setId(1L);
        restaurant.setName("Restaurante Test");
        restaurant.setNit("123456789");
        restaurant.setAddress("Calle 123");
        restaurant.setPhoneNumber("+573001234567");
        restaurant.setLogoUrl("https://logo.com/logo.png");
        restaurant.setOwnerId(10L);

        assertEquals(1L, restaurant.getId());
        assertEquals("Restaurante Test", restaurant.getName());
        assertEquals("123456789", restaurant.getNit());
        assertEquals("Calle 123", restaurant.getAddress());
        assertEquals("+573001234567", restaurant.getPhoneNumber());
        assertEquals("https://logo.com/logo.png", restaurant.getLogoUrl());
        assertEquals(10L, restaurant.getOwnerId());
    }

    @Test
    void shouldAllowNullValues() {
        Restaurant restaurant = new Restaurant();

        assertNull(restaurant.getId());
        assertNull(restaurant.getName());
        assertNull(restaurant.getNit());
        assertNull(restaurant.getAddress());
        assertNull(restaurant.getPhoneNumber());
        assertNull(restaurant.getLogoUrl());
        assertNull(restaurant.getOwnerId());
    }
}
