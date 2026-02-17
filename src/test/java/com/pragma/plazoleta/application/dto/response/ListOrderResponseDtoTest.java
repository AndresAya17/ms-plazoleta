package com.pragma.plazoleta.application.dto.response;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
class ListOrderResponseDtoTest {

    @Test
    void shouldSetAndGetAllFieldsCorrectly() {
        ListOrderResponseDto dto = new ListOrderResponseDto();
        LocalDateTime now = LocalDateTime.now();

        OrderItemResponseDto item = new OrderItemResponseDto();
        item.setDishId(1L);
        item.setQuantity(2);

        dto.setId(10L);
        dto.setClientId(20L);
        dto.setCreatedAt(now);
        dto.setRestaurantId(30L);
        dto.setStatus("PENDING");
        dto.setChefId(40L);
        dto.setItems(Collections.singletonList(item));

        assertEquals(10L, dto.getId());
        assertEquals(20L, dto.getClientId());
        assertEquals(now, dto.getCreatedAt());
        assertEquals(30L, dto.getRestaurantId());
        assertEquals("PENDING", dto.getStatus());
        assertEquals(40L, dto.getChefId());
        assertNotNull(dto.getItems());
        assertEquals(1, dto.getItems().size());
        assertEquals(1L, dto.getItems().get(0).getDishId());
        assertEquals(2, dto.getItems().get(0).getQuantity());
    }

    @Test
    void shouldAllowNullValues() {
        ListOrderResponseDto dto = new ListOrderResponseDto();

        dto.setChefId(null);
        dto.setItems(null);

        assertNull(dto.getChefId());
        assertNull(dto.getItems());
    }
}
