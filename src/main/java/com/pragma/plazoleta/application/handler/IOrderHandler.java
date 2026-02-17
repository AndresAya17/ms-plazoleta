package com.pragma.plazoleta.application.handler;

import com.pragma.plazoleta.application.dto.request.CreateOrderRequestDto;
import com.pragma.plazoleta.application.dto.response.ListOrderResponseDto;
import com.pragma.plazoleta.application.dto.response.OrderResponseDto;
import com.pragma.plazoleta.application.dto.response.PageResponseDto;

public interface IOrderHandler {
    OrderResponseDto saveOrder(CreateOrderRequestDto orderRequestDto, Long userId);
    PageResponseDto<ListOrderResponseDto> listOrderByStatus(Long userId, String status, int page, int size);
}
