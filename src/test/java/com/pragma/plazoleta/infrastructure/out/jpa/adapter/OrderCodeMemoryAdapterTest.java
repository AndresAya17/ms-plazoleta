package com.pragma.plazoleta.infrastructure.out.jpa.adapter;

import com.pragma.plazoleta.domain.model.DeliveryCode;
import com.pragma.plazoleta.infrastructure.out.jpa.entity.DeliveryCodeEntity;
import com.pragma.plazoleta.infrastructure.out.jpa.mapper.IDeliveryCodeEntityMapper;
import com.pragma.plazoleta.infrastructure.out.jpa.repository.IDeliveryCodeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class OrderCodeMemoryAdapterTest {

    @Mock
    private IDeliveryCodeRepository IDeliveryCodeRepository;

    @Mock
    private IDeliveryCodeEntityMapper deliveryCodeEntityMapper;

    @InjectMocks
    private OrderCodeMemoryAdapter adapter;

    @Test
    void shouldSaveCode() {

        DeliveryCode domain = new DeliveryCode(1L, "123456", null, true);
        DeliveryCodeEntity entity = new DeliveryCodeEntity();

        when(deliveryCodeEntityMapper.toEntity(domain))
                .thenReturn(entity);

        when(IDeliveryCodeRepository.save(entity))
                .thenReturn(entity);

        when(deliveryCodeEntityMapper.toDomain(entity))
                .thenReturn(domain);

        DeliveryCode result = adapter.saveCode(domain);

        assertNotNull(result);
        verify(deliveryCodeEntityMapper).toEntity(domain);
        verify(IDeliveryCodeRepository).save(entity);
        verify(deliveryCodeEntityMapper).toDomain(entity);
    }

    @Test
    void shouldReturnActiveCodeWhenFound() {

        Long orderId = 1L;

        DeliveryCodeEntity entity = new DeliveryCodeEntity();
        DeliveryCode domain = new DeliveryCode(orderId, "123456", null, true);

        when(IDeliveryCodeRepository.findByOrderIdAndActiveTrue(orderId))
                .thenReturn(Optional.of(entity));

        when(deliveryCodeEntityMapper.toDomain(entity))
                .thenReturn(domain);

        Optional<DeliveryCode> result =
                adapter.findActiveByOrderId(orderId);

        assertTrue(result.isPresent());
        assertEquals(domain, result.get());

        verify(IDeliveryCodeRepository)
                .findByOrderIdAndActiveTrue(orderId);
        verify(deliveryCodeEntityMapper)
                .toDomain(entity);
    }

    @Test
    void shouldReturnEmptyWhenNoActiveCode() {

        Long orderId = 1L;

        when(IDeliveryCodeRepository.findByOrderIdAndActiveTrue(orderId))
                .thenReturn(Optional.empty());

        Optional<DeliveryCode> result =
                adapter.findActiveByOrderId(orderId);

        assertTrue(result.isEmpty());

        verify(IDeliveryCodeRepository)
                .findByOrderIdAndActiveTrue(orderId);
        verifyNoInteractions(deliveryCodeEntityMapper);
    }

    @Test
    void shouldDeactivateByOrderId() {

        Long orderId = 1L;

        adapter.deactivateByOrderId(orderId);

        verify(IDeliveryCodeRepository)
                .deactivateByOrderId(orderId);
    }
}
