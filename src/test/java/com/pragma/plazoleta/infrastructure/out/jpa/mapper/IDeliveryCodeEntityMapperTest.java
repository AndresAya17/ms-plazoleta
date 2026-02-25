package com.pragma.plazoleta.infrastructure.out.jpa.mapper;

import com.pragma.plazoleta.domain.model.DeliveryCode;
import com.pragma.plazoleta.infrastructure.out.jpa.entity.DeliveryCodeEntity;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class IDeliveryCodeEntityMapperTest {

    private final IDeliveryCodeEntityMapper mapper =
            Mappers.getMapper(IDeliveryCodeEntityMapper.class);

    @Test
    void shouldMapDomainToEntity() {

        LocalDateTime expiration = LocalDateTime.now().plusMinutes(5);

        DeliveryCode domain = new DeliveryCode(
                1L,
                "ABC123",
                expiration,
                true
        );

        DeliveryCodeEntity entity = mapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals(domain.getOrderId(), entity.getOrderId());
        assertEquals(domain.getCodeHash(), entity.getCodeHash());
        assertEquals(domain.getExpirationDate(), entity.getExpirationDate());
        assertEquals(domain.isActive(), entity.isActive());
    }

    @Test
    void shouldMapEntityToDomain() {

        LocalDateTime expiration = LocalDateTime.now().plusMinutes(10);

        DeliveryCodeEntity entity = new DeliveryCodeEntity();
        entity.setId(99L);
        entity.setOrderId(5L);
        entity.setCodeHash("XYZ789");
        entity.setExpirationDate(expiration);
        entity.setActive(true);

        DeliveryCode domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(entity.getOrderId(), domain.getOrderId());
        assertEquals(entity.getCodeHash(), domain.getCodeHash());
        assertEquals(entity.getExpirationDate(), domain.getExpirationDate());
        assertEquals(entity.isActive(), domain.isActive());
    }
}
