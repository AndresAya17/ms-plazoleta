package com.pragma.plazoleta.domain.model;


import lombok.Getter;

@Getter
public class OrderItem {
    private Long dishId;
    private Integer quantity;

    public OrderItem(Long dishId, Integer quantity) {
        this.dishId = dishId;
        this.quantity = quantity;
    }
}
