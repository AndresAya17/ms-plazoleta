package com.pragma.plazoleta.application.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.util.List;



@Getter
@Setter
public class CreateOrderRequestDto {
    @NotNull(message = "Restaurant id is required")
    private Long restaurantId;

    @NotEmpty(message = "Order must contain at least one item")
    private List<OrderItemRequestDto> items;
}
