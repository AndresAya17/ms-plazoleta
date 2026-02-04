package com.pragma.plazoleta.domain.model;

import com.pragma.plazoleta.domain.exception.DomainException;
import com.pragma.plazoleta.domain.exception.ErrorCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class Order {
    private Long id;
    private Long clientId;
    private Long restaurantId;
    private OrderStatus status;
    private List<OrderItem> items;
    private LocalDateTime createdAt;

    public Order(
            Long clientId,
            Long restaurantId,
            List<OrderItem> items
    ) {
        this.clientId = clientId;
        this.restaurantId = restaurantId;
        this.items = items;
        this.status = OrderStatus.PENDIENTE;
        this.createdAt = LocalDateTime.now();
    }

    public void accept() {
        if (status != OrderStatus.PENDIENTE) {
            throw new DomainException(ErrorCode.INVALID_ORDER_STATE, "Order cannot be accepted");
        }
        status = OrderStatus.EN_PREPARACION;
    }

    public void markAsReady() {
        if (status != OrderStatus.EN_PREPARACION) {
            throw new DomainException(ErrorCode.INVALID_ORDER_STATE, "Order is not in preparation");
        }
        status = OrderStatus.LISTO;
    }

    public void deliver() {
        if (status != OrderStatus.LISTO) {
            throw new DomainException(ErrorCode.INVALID_ORDER_STATE, "Order is not ready");
        }
        status = OrderStatus.ENTREGADO;
    }

    public void cancel() {
        if (status != OrderStatus.PENDIENTE) {
            throw new DomainException(ErrorCode.INVALID_ORDER_STATE, "Order cannot be cancelled");
        }
        status = OrderStatus.CANCELADO;
    }
}
