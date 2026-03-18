package com.pragma.plazoleta.application.handler;

import com.pragma.plazoleta.application.dto.request.CreateOrderRequestDto;
import com.pragma.plazoleta.application.dto.response.ListOrderResponseDto;
import com.pragma.plazoleta.application.dto.response.OrderResponseDto;
import com.pragma.plazoleta.application.dto.response.PageResponseDto;

import java.util.List;

public interface IOrderHandler {
    OrderResponseDto saveOrder(CreateOrderRequestDto orderRequestDto, Long userId, String email);
    PageResponseDto<ListOrderResponseDto> listOrderByStatus(Long userId, String status, int page, int size);
    OrderResponseDto updateStatusOrder(Long userId, Long orderId);
    OrderResponseDto updateStatusOrderReady(Long userId, Long orderId);
    void updateStatusOrderDelivery(String code, Long userId, Long orderId);
    void updateStatusOrderCanceled(Long userId, Long orderId);
    List<Long> getOrdersByRestaurantId(Long restaurantId);
}
