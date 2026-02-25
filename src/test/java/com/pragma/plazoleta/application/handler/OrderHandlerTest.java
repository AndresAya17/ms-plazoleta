package com.pragma.plazoleta.application.handler;

import com.pragma.plazoleta.application.dto.request.CreateOrderRequestDto;
import com.pragma.plazoleta.application.dto.response.ListOrderResponseDto;
import com.pragma.plazoleta.application.dto.response.OrderResponseDto;
import com.pragma.plazoleta.application.dto.response.PageResponseDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

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

    @Test
    void shouldListOrdersByStatusSuccessfully() {

        Long userId = 1L;
        String status = "PENDIENTE";
        int page = 0;
        int size = 5;

        Order order = new Order(userId, 1L, List.of());
        order.setId(10L);
        order.setStatus(OrderStatus.PENDIENTE);

        Page<Order> orderPage = new PageImpl<>(
                List.of(order),
                PageRequest.of(page, size),
                1
        );

        ListOrderResponseDto listDto = new ListOrderResponseDto();
        listDto.setId(10L);
        listDto.setStatus("PENDIENTE");

        when(orderServicePort.listOrderByStatus(userId, status, page, size))
                .thenReturn(orderPage);

        when(orderResponseMapper.listToResponse(order))
                .thenReturn(listDto);

        PageResponseDto<ListOrderResponseDto> result =
                orderHandler.listOrderByStatus(userId, status, page, size);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(10L, result.getContent().get(0).getId());
        assertEquals("PENDIENTE",
                result.getContent().get(0).getStatus());

        verify(orderServicePort)
                .listOrderByStatus(userId, status, page, size);

        verify(orderResponseMapper)
                .listToResponse(order);

        verifyNoMoreInteractions(orderServicePort, orderResponseMapper);
    }

    @Test
    void shouldUpdateStatusOrderSuccessfully() {

        Long userId = 5L;
        Long orderId = 1L;

        Order order = new Order(userId, 1L, List.of());
        order.setId(orderId);
        order.setStatus(OrderStatus.EN_PREPARACION);

        OrderResponseDto responseDto = new OrderResponseDto();
        responseDto.setId(orderId);
        responseDto.setStatus(OrderStatus.EN_PREPARACION);

        when(orderServicePort.updateStatus(userId, orderId))
                .thenReturn(order);

        when(orderResponseMapper.toResponse(order))
                .thenReturn(responseDto);

        OrderResponseDto result =
                orderHandler.updateStatusOrder(userId, orderId);

        assertNotNull(result);
        assertEquals(orderId, result.getId());
        assertEquals(OrderStatus.EN_PREPARACION, result.getStatus());

        verify(orderServicePort).updateStatus(userId, orderId);
        verify(orderResponseMapper).toResponse(order);
        verifyNoMoreInteractions(orderServicePort, orderResponseMapper);
    }

    @Test
    void shouldUpdateStatusOrderReadySuccessfully() {

        Long userId = 5L;
        Long orderId = 1L;

        Order order = new Order(userId, 1L, List.of());
        order.setId(orderId);
        order.setStatus(OrderStatus.LISTO);

        OrderResponseDto responseDto = new OrderResponseDto();
        responseDto.setId(orderId);
        responseDto.setStatus(OrderStatus.LISTO);

        when(orderServicePort.updateStatusReady(userId, orderId))
                .thenReturn(order);

        when(orderResponseMapper.toResponse(order))
                .thenReturn(responseDto);

        OrderResponseDto result =
                orderHandler.updateStatusOrderReady(userId, orderId);

        assertNotNull(result);
        assertEquals(orderId, result.getId());
        assertEquals(OrderStatus.LISTO, result.getStatus());

        verify(orderServicePort)
                .updateStatusReady(userId, orderId);

        verify(orderResponseMapper)
                .toResponse(order);

        verifyNoMoreInteractions(orderServicePort, orderResponseMapper);
    }
}
