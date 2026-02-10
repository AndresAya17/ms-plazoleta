package com.pragma.plazoleta.application.handler;

import com.pragma.plazoleta.application.dto.request.CreateOrderRequestDto;
import com.pragma.plazoleta.application.dto.response.OrderResponseDto;
import com.pragma.plazoleta.application.handler.impl.OrderHandler;
import com.pragma.plazoleta.application.mapper.IOrderRequestMapper;
import com.pragma.plazoleta.application.mapper.IOrderResponseMapper;
import com.pragma.plazoleta.domain.api.IOrderServicePort;
import com.pragma.plazoleta.domain.model.Order;
import com.pragma.plazoleta.domain.model.OrderStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderHandlerTest {
    @Mock
    private IOrderServicePort orderServicePort;

    @Mock
    private IOrderRequestMapper requestMapper;

    @Mock
    private IOrderResponseMapper orderResponseMapper;

    @InjectMocks
    private OrderHandler orderHandler;

    @Test
    void shouldSaveOrderSuccessfully() {
        Long userId = 1L;

        CreateOrderRequestDto requestDto = new CreateOrderRequestDto();
        requestDto.setRestaurantId(1L);
        requestDto.setItems(List.of());

        Order order =
                new Order(
                        userId,
                        1L,
                        List.of()
                );

        Order savedOrder =
                new Order(
                        userId,
                        1L,
                        List.of()
                );
        savedOrder.setId(10L);
        savedOrder.setStatus(OrderStatus.PENDIENTE);

        OrderResponseDto responseDto = new OrderResponseDto();
        responseDto.setId(10L);
        responseDto.setStatus(OrderStatus.PENDIENTE);

        when(requestMapper.toOrder(requestDto))
                .thenReturn(order);

        when(orderServicePort.saveOrder(order, userId))
                .thenReturn(savedOrder);

        when(orderResponseMapper.toResponse(savedOrder))
                .thenReturn(responseDto);

        OrderResponseDto result =
                orderHandler.saveOrder(requestDto, userId);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals(OrderStatus.PENDIENTE, result.getStatus());

        verify(requestMapper).toOrder(requestDto);
        verify(orderServicePort).saveOrder(order, userId);
        verify(orderResponseMapper).toResponse(savedOrder);
        verifyNoMoreInteractions(
                requestMapper,
                orderServicePort,
                orderResponseMapper
        );
    }
}
