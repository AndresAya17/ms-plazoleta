package com.pragma.plazoleta.application.dto.response;

import com.pragma.plazoleta.domain.model.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderResponseDto {
    private Long id;
    private OrderStatus status;
}
