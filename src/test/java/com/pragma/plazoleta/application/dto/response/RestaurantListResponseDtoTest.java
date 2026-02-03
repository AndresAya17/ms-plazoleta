package com.pragma.plazoleta.application.dto.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
class RestaurantListResponseDtoTest {

    @Test
    void shouldSetAndGetName() {
        RestaurantListResponseDto dto = new RestaurantListResponseDto();
        String expectedName = "Pollos Popeye";

        dto.setName(expectedName);

        assertEquals(expectedName, dto.getName());
    }

    @Test
    void shouldSetAndGetLogoUrl() {
        RestaurantListResponseDto dto = new RestaurantListResponseDto();
        String expectedLogoUrl = "https://logopoll";

        dto.setLogoUrl(expectedLogoUrl);

        assertEquals(expectedLogoUrl, dto.getLogoUrl());
    }
}
