package com.pragma.plazoleta.domain.spi;

import com.pragma.plazoleta.domain.model.DeliveryCode;

import java.util.Optional;

public interface IDeliveryCodePersistencePort {
    Optional<DeliveryCode> findByOrderId(Long orderId);
    DeliveryCode save(DeliveryCode deliveryCode);
}
