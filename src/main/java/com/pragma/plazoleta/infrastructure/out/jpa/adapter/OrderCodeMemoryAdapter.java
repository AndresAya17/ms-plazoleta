package com.pragma.plazoleta.infrastructure.out.jpa.adapter;

import com.pragma.plazoleta.domain.model.DeliveryCode;
import com.pragma.plazoleta.domain.spi.IOrderCodePersistencePort;
import com.pragma.plazoleta.infrastructure.out.jpa.entity.DeliveryCodeEntity;
import com.pragma.plazoleta.infrastructure.out.jpa.mapper.IDeliveryCodeEntityMapper;
import com.pragma.plazoleta.infrastructure.out.jpa.repository.IDeliveryCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderCodeMemoryAdapter implements IOrderCodePersistencePort {

    private final IDeliveryCodeRepository IDeliveryCodeRepository;
    private final IDeliveryCodeEntityMapper deliveryCodeEntityMapper;


    @Override
    public DeliveryCode saveCode(DeliveryCode deliveryCode) {
        DeliveryCodeEntity entity = deliveryCodeEntityMapper.toEntity(deliveryCode);
        return deliveryCodeEntityMapper.toDomain(IDeliveryCodeRepository.save(entity));
    }

    @Override
    public Optional<DeliveryCode> findActiveByOrderId(Long orderId) {
        return IDeliveryCodeRepository.findByOrderIdAndActiveTrue(orderId)
                .map(deliveryCodeEntityMapper::toDomain);
    }

    @Override
    public void deactivateByOrderId(Long orderId) {
        IDeliveryCodeRepository.deactivateByOrderId(orderId);
    }

}
