package com.pragma.plazoleta.application.dto.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemResponseDto {

    private Long dishId;
    private Integer quantity;
}
