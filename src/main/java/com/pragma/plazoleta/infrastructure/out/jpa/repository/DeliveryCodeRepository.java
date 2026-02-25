package com.pragma.plazoleta.infrastructure.out.jpa.repository;

import com.pragma.plazoleta.infrastructure.out.jpa.entity.DeliveryCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DeliveryCodeRepository extends JpaRepository<DeliveryCodeEntity, Long> {

    Optional<DeliveryCodeEntity>
    findByOrderIdAndActiveTrue(Long orderId);

    @Modifying
    @Query("UPDATE DeliveryCodeEntity d SET d.active = false WHERE d.orderId = :orderId AND d.active = true")
    void deactivateByOrderId(Long orderId);
}
