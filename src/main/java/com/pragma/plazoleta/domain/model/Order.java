package com.pragma.plazoleta.domain.model;

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
}
