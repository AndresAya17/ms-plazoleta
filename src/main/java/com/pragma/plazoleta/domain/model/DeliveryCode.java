package com.pragma.plazoleta.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DeliveryCode {

    private Long id;
    private Long orderId;
    private String codeHash;
    private LocalDateTime expirationDate;
    private boolean active;

    public DeliveryCode(Long orderId, String codeHash, LocalDateTime expirationDate, boolean active) {
        this.orderId = orderId;
        this.codeHash = codeHash;
        this.expirationDate = expirationDate;
        this.active = active;
    }
}
