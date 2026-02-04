package com.pragma.plazoleta.application.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemRequestDto {
    @NotNull(message = "Dish id is required")
    private Long dishId;

    @NotNull
    @Min(value = 1, message = "Quantity must be greater than zero")
    private Integer quantity;
}
