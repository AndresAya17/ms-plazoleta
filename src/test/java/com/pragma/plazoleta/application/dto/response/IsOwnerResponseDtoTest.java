package com.pragma.plazoleta.application.dto.response;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IsOwnerResponseDtoTest {
    @Test
    void shouldCreateDtoWithNoArgsConstructor() {
        IsOwnerResponseDto dto = new IsOwnerResponseDto();

        assertNotNull(dto);
        assertNull(dto.getIsOwner());
    }

    @Test
    void shouldCreateDtoWithAllArgsConstructor() {
        IsOwnerResponseDto dto = new IsOwnerResponseDto(true);

        assertNotNull(dto);
        assertTrue(dto.getIsOwner());
    }

}
