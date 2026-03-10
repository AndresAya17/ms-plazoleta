package com.pragma.plazoleta.application.handler.impl;

import com.pragma.plazoleta.application.dto.request.CreateOrderRequestDto;
import com.pragma.plazoleta.application.dto.response.ListOrderResponseDto;
import com.pragma.plazoleta.application.dto.response.OrderResponseDto;
import com.pragma.plazoleta.application.dto.response.PageResponseDto;
import com.pragma.plazoleta.application.handler.IOrderHandler;
import com.pragma.plazoleta.application.mapper.IOrderRequestMapper;
import com.pragma.plazoleta.application.mapper.IOrderResponseMapper;
import com.pragma.plazoleta.domain.api.IOrderServicePort;
import com.pragma.plazoleta.domain.model.Order;
import com.pragma.plazoleta.domain.model.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderHandler implements IOrderHandler {

    private final IOrderServicePort orderServicePort;
    private final IOrderRequestMapper requestMapper;
    private final IOrderResponseMapper orderResponseMapper;


    @Override
    public OrderResponseDto saveOrder(CreateOrderRequestDto orderRequestDto, Long userId) {
        Order order = requestMapper.toOrder(orderRequestDto);
        Order orderSaved = orderServicePort.saveOrder(order, userId);
        return orderResponseMapper.toResponse(orderSaved);
    }

    @Override
    public PageResponseDto<ListOrderResponseDto> listOrderByStatus(Long userId, String status, int page, int size) {
        PageResult<Order> orders = orderServicePort.listOrderByStatus(userId, status, page, size);
        return orderResponseMapper.listToResponse(orders);
    }

    @Override
    public OrderResponseDto updateStatusOrder(Long userId, Long orderId) {
        Order orderSaved = orderServicePort.updateStatus(userId, orderId);
        return orderResponseMapper.toResponse(orderSaved);
    }

    @Override
    public OrderResponseDto updateStatusOrderReady(Long userId, Long orderId) {
        Order orderSaved = orderServicePort.updateStatusReady(userId, orderId);
        return orderResponseMapper.toResponse(orderSaved);
    }

    @Override
    public void updateStatusOrderDelivery(String code, Long userId, Long orderId) {
        orderServicePort.updateStatusDelivery(code,userId,orderId);
    }

    @Override
    public void updateStatusOrderCanceled(Long userId, Long orderId) {
       orderServicePort.updateStatusCanceled(userId, orderId);
    }
}
