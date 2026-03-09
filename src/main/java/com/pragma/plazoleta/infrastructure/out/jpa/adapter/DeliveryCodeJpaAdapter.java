package com.pragma.plazoleta.infrastructure.out.jpa.adapter;

import com.pragma.plazoleta.domain.model.DeliveryCode;
import com.pragma.plazoleta.domain.spi.IDeliveryCodePersistencePort;
import com.pragma.plazoleta.infrastructure.out.jpa.entity.DeliveryCodeEntity;
import com.pragma.plazoleta.infrastructure.out.jpa.mapper.IDeliveryCodeEntityMapper;
import com.pragma.plazoleta.infrastructure.out.jpa.repository.IDeliveryCodeRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class DeliveryCodeJpaAdapter implements IDeliveryCodePersistencePort {

    private final IDeliveryCodeRepository IDeliveryCodeRepository;
    private final IDeliveryCodeEntityMapper deliveryCodeEntityMapper;

    @Override
    public Optional<DeliveryCode> findByOrderId(Long orderId) {
        return IDeliveryCodeRepository.findByOrderId(orderId).map(deliveryCodeEntityMapper::toDomain);
    }

    @Override
    public DeliveryCode save(DeliveryCode deliveryCode) {
        DeliveryCodeEntity deliveryCodeEntity = deliveryCodeEntityMapper.toEntity(deliveryCode);
        return deliveryCodeEntityMapper.toDomain(IDeliveryCodeRepository.save(deliveryCodeEntity));
    }
}
