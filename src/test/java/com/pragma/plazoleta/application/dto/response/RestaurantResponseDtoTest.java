package com.pragma.plazoleta.application.dto.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RestaurantResponseDtoTest {

    @Test
    void shouldSetAndGetAllFieldsCorrectly() {
        RestaurantResponseDto dto = new RestaurantResponseDto();

        dto.setId(1L);
        dto.setName("Restaurante Test");
        dto.setNit("123456789");
        dto.setAddress("Calle 10 #20-30");
        dto.setPhoneNumber("+573001234567");
        dto.setLogoUrl("https://logo.com/logo.png");
        dto.setOwnerId(99L);

        assertEquals(1L, dto.getId());
        assertEquals("Restaurante Test", dto.getName());
        assertEquals("123456789", dto.getNit());
        assertEquals("Calle 10 #20-30", dto.getAddress());
        assertEquals("+573001234567", dto.getPhoneNumber());
        assertEquals("https://logo.com/logo.png", dto.getLogoUrl());
        assertEquals(99L, dto.getOwnerId());
    }

    @Test
    void shouldAllowNullValues() {
        RestaurantResponseDto dto = new RestaurantResponseDto();

        assertNull(dto.getId());
        assertNull(dto.getName());
        assertNull(dto.getNit());
        assertNull(dto.getAddress());
        assertNull(dto.getPhoneNumber());
        assertNull(dto.getLogoUrl());
        assertNull(dto.getOwnerId());
    }
}
