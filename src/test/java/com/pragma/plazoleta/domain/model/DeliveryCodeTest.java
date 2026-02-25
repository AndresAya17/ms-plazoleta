package com.pragma.plazoleta.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryCodeTest {

    @Test
    void shouldCreateDeliveryCodeUsingConstructor() {

        Long orderId = 10L;
        String codeHash = "abc123hash";
        LocalDateTime expirationDate = LocalDateTime.now().plusMinutes(10);
        boolean active = true;

        DeliveryCode deliveryCode =
                new DeliveryCode(orderId, codeHash, expirationDate, active);

        assertEquals(orderId, deliveryCode.getOrderId());
        assertEquals(codeHash, deliveryCode.getCodeHash());
        assertEquals(expirationDate, deliveryCode.getExpirationDate());
        assertTrue(deliveryCode.isActive());
    }

    @Test
    void shouldSetAndGetAllFields() {

        DeliveryCode deliveryCode = new DeliveryCode(
                1L,
                "hash",
                LocalDateTime.now(),
                false
        );

        deliveryCode.setId(99L);
        deliveryCode.setOrderId(20L);
        deliveryCode.setCodeHash("newHash");
        deliveryCode.setExpirationDate(LocalDateTime.now().plusHours(1));
        deliveryCode.setActive(true);

        assertEquals(99L, deliveryCode.getId());
        assertEquals(20L, deliveryCode.getOrderId());
        assertEquals("newHash", deliveryCode.getCodeHash());
        assertTrue(deliveryCode.isActive());
    }
}
