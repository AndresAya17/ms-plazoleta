package com.pragma.plazoleta.application.handler.impl;

import com.pragma.plazoleta.application.dto.request.CreateOrderRequestDto;
import com.pragma.plazoleta.application.dto.response.OrderResponseDto;
import com.pragma.plazoleta.application.handler.IOrderHandler;
import com.pragma.plazoleta.application.mapper.IOrderRequestMapper;
import com.pragma.plazoleta.application.mapper.IOrderResponseMapper;
import com.pragma.plazoleta.domain.api.IOrderServicePort;
import com.pragma.plazoleta.domain.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderHandler implements IOrderHandler {

    private final IOrderServicePort orderServicePort;
    private final IOrderRequestMapper requestMapper;
    private final IOrderResponseMapper orderResponseMapper;


    @Override
    public OrderResponseDto saveOrder(CreateOrderRequestDto orderRequestDto, Long userId, String rol) {
        Order order = requestMapper.toOrder(orderRequestDto);
        Order orderSaved = orderServicePort.saveOrder(order, userId, rol);
        return orderResponseMapper.toResponse(orderSaved);
    }
}
