package com.pragma.plazoleta.application.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ListOrderResponseDto {

    private Long id;
    private Long clientId;
    private LocalDateTime createdAt;
    private Long restaurantId;
    private String status;
    private Long chefId;

    private List<OrderItemResponseDto> items;
}
