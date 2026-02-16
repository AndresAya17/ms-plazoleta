package com.pragma.plazoleta.application.dto.response;

import com.pragma.plazoleta.domain.model.OrderStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrderResponseDtoTest {
    @Test
    void shouldSetAndGetFieldsCorrectly() {
        OrderResponseDto dto = new OrderResponseDto();
        Long id = 1L;
        OrderStatus status = OrderStatus.PENDIENTE;

        dto.setId(id);
        dto.setStatus(status);

        assertNotNull(dto);
        assertEquals(id, dto.getId());
        assertEquals(status, dto.getStatus());
    }
}
