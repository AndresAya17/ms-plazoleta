package com.pragma.plazoleta.infrastructure.out.jpa.adapter;

import com.pragma.plazoleta.domain.model.DeliveryCode;
import com.pragma.plazoleta.infrastructure.out.jpa.entity.DeliveryCodeEntity;
import com.pragma.plazoleta.infrastructure.out.jpa.mapper.IDeliveryCodeEntityMapper;
import com.pragma.plazoleta.infrastructure.out.jpa.repository.IDeliveryCodeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeliveryCodeJpaAdapterTest {

    @Mock
    private IDeliveryCodeRepository deliveryCodeRepository;

    @Mock
    private IDeliveryCodeEntityMapper deliveryCodeEntityMapper;

    @InjectMocks
    private DeliveryCodeJpaAdapter deliveryCodeJpaAdapter;

    @Test
    void shouldFindDeliveryCodeByOrderId() {
        Long orderId = 1L;

        DeliveryCodeEntity deliveryCodeEntity = new DeliveryCodeEntity();
        deliveryCodeEntity.setId(100L);
        deliveryCodeEntity.setOrderId(orderId);
        deliveryCodeEntity.setCodeHash("123456");
        deliveryCodeEntity.setExpirationDate(LocalDateTime.now().plusMinutes(10));
        deliveryCodeEntity.setActive(true);

        DeliveryCode deliveryCode = new DeliveryCode(
                orderId,
                "123456",
                deliveryCodeEntity.getExpirationDate(),
                true
        );

        when(deliveryCodeRepository.findByOrderId(orderId))
                .thenReturn(Optional.of(deliveryCodeEntity));

        when(deliveryCodeEntityMapper.toDomain(deliveryCodeEntity))
                .thenReturn(deliveryCode);

        Optional<DeliveryCode> result =
                deliveryCodeJpaAdapter.findByOrderId(orderId);

        assertTrue(result.isPresent());
        assertEquals(deliveryCode, result.get());

        verify(deliveryCodeRepository).findByOrderId(orderId);
        verify(deliveryCodeEntityMapper).toDomain(deliveryCodeEntity);
    }

    @Test
    void shouldReturnEmptyWhenDeliveryCodeByOrderIdNotFound() {
        Long orderId = 1L;

        when(deliveryCodeRepository.findByOrderId(orderId))
                .thenReturn(Optional.empty());

        Optional<DeliveryCode> result =
                deliveryCodeJpaAdapter.findByOrderId(orderId);

        assertTrue(result.isEmpty());

        verify(deliveryCodeRepository).findByOrderId(orderId);
        verify(deliveryCodeEntityMapper, never()).toDomain(any(DeliveryCodeEntity.class));
    }

    @Test
    void shouldSaveDeliveryCode() {
        DeliveryCode deliveryCode = new DeliveryCode(
                1L,
                "123456",
                LocalDateTime.now().plusMinutes(10),
                true
        );

        DeliveryCodeEntity entityToSave = new DeliveryCodeEntity();
        entityToSave.setOrderId(1L);
        entityToSave.setCodeHash("123456");
        entityToSave.setExpirationDate(deliveryCode.getExpirationDate());
        entityToSave.setActive(true);

        DeliveryCodeEntity savedEntity = new DeliveryCodeEntity();
        savedEntity.setId(200L);
        savedEntity.setOrderId(1L);
        savedEntity.setCodeHash("123456");
        savedEntity.setExpirationDate(deliveryCode.getExpirationDate());
        savedEntity.setActive(true);

        DeliveryCode savedDomain = new DeliveryCode(
                1L,
                "123456",
                savedEntity.getExpirationDate(),
                true
        );

        when(deliveryCodeEntityMapper.toEntity(deliveryCode))
                .thenReturn(entityToSave);

        when(deliveryCodeRepository.save(entityToSave))
                .thenReturn(savedEntity);

        when(deliveryCodeEntityMapper.toDomain(savedEntity))
                .thenReturn(savedDomain);

        DeliveryCode result = deliveryCodeJpaAdapter.save(deliveryCode);

        assertNotNull(result);
        assertEquals(savedDomain, result);

        verify(deliveryCodeEntityMapper).toEntity(deliveryCode);
        verify(deliveryCodeRepository).save(entityToSave);
        verify(deliveryCodeEntityMapper).toDomain(savedEntity);
    }

}
