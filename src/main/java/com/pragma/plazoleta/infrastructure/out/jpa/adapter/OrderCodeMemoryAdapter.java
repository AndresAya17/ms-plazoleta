package com.pragma.plazoleta.infrastructure.out.jpa.adapter;

import com.pragma.plazoleta.domain.model.DeliveryCode;
import com.pragma.plazoleta.domain.spi.IOrderCodePersistencePort;
import com.pragma.plazoleta.infrastructure.out.jpa.entity.DeliveryCodeEntity;
import com.pragma.plazoleta.infrastructure.out.jpa.mapper.IDeliveryCodeEntityMapper;
import com.pragma.plazoleta.infrastructure.out.jpa.repository.DeliveryCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderCodeMemoryAdapter implements IOrderCodePersistencePort {

    private final DeliveryCodeRepository deliveryCodeRepository;
    private final IDeliveryCodeEntityMapper deliveryCodeEntityMapper;


    @Override
    public DeliveryCode saveCode(DeliveryCode deliveryCode) {
        DeliveryCodeEntity entity = deliveryCodeEntityMapper.toEntity(deliveryCode);
        return deliveryCodeEntityMapper.toDomain(deliveryCodeRepository.save(entity));
    }

    @Override
    public Optional<DeliveryCode> findActiveByOrderId(Long orderId) {
        return deliveryCodeRepository.findByOrderIdAndActiveTrue(orderId)
                .map(deliveryCodeEntityMapper::toDomain);
    }

    @Override
    public void deactivateByOrderId(Long orderId) {
        deliveryCodeRepository.deactivateByOrderId(orderId);
    }

}
