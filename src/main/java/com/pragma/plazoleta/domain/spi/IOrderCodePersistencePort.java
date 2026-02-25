package com.pragma.plazoleta.domain.spi;

import com.pragma.plazoleta.domain.model.DeliveryCode;

import java.util.Optional;

public interface IOrderCodePersistencePort {

    DeliveryCode saveCode(DeliveryCode deliveryCode);

    Optional<DeliveryCode> findActiveByOrderId(Long orderId);

    void deactivateByOrderId(Long orderId);
}
