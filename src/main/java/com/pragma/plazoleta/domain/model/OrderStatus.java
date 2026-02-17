package com.pragma.plazoleta.domain.model;

import com.pragma.plazoleta.domain.exception.DomainException;
import com.pragma.plazoleta.domain.exception.ErrorCode;

public enum OrderStatus {
    PENDIENTE,
    EN_PREPARACION,
    LISTO,
    ENTREGADO,
    CANCELADO;

    public static OrderStatus from(String value) {
        try {
            return OrderStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new DomainException(
                    ErrorCode.INVALID_STATUS,
                    "Invalid order status: " + value
            );
        }
    }
}
