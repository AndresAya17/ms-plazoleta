package com.pragma.plazoleta.infrastructure.out.jpa.adapter;

import com.pragma.plazoleta.application.dto.response.ClientPhoneResponseDto;
import com.pragma.plazoleta.domain.exception.DomainException;
import com.pragma.plazoleta.domain.exception.ErrorCode;
import com.pragma.plazoleta.domain.model.Order;
import com.pragma.plazoleta.domain.model.OrderStatus;
import com.pragma.plazoleta.infrastructure.out.jpa.entity.OrderEntity;
import com.pragma.plazoleta.infrastructure.out.jpa.entity.OrderItemEntity;
import com.pragma.plazoleta.infrastructure.out.jpa.mapper.IOrderEntityMapper;
import com.pragma.plazoleta.infrastructure.out.jpa.repository.IOrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderJpaAdapterTest {
    @Mock
    private IOrderRepository orderRepository;

    @Mock
    private IOrderEntityMapper orderEntityMapper;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private UserJpaAdapter userJpaAdapter;

    @InjectMocks
    private OrderJpaAdapter orderJpaAdapter;

    @Test
    void shouldSaveOrderAndAssignOrderToItems() {
        Order order = mock(Order.class);

        OrderItemEntity item1 = new OrderItemEntity();
        OrderItemEntity item2 = new OrderItemEntity();

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setItems(List.of(item1, item2));

        OrderEntity savedEntity = new OrderEntity();
        Order expectedDomainOrder = mock(Order.class);

        when(orderEntityMapper.toEntity(order))
                .thenReturn(orderEntity);

        when(orderRepository.save(orderEntity))
                .thenReturn(savedEntity);

        when(orderEntityMapper.toDomain(savedEntity))
                .thenReturn(expectedDomainOrder);

        Order result = orderJpaAdapter.saveOrder(order);

        verify(orderEntityMapper).toEntity(order);
        verify(orderRepository).save(orderEntity);
        verify(orderEntityMapper).toDomain(savedEntity);

        assertSame(expectedDomainOrder, result);

        assertSame(orderEntity, item1.getOrder());
        assertSame(orderEntity, item2.getOrder());
    }

    @Test
    void shouldFindOrdersByRestaurantIdAndStatus() {

        long restaurantId = 1L;
        OrderStatus status = OrderStatus.PENDIENTE;
        int page = 0;
        int size = 5;

        Pageable pageable = PageRequest.of(page, size);

        OrderEntity entity = new OrderEntity();
        Order domainOrder = mock(Order.class);

        Page<OrderEntity> entityPage =
                new PageImpl<>(List.of(entity), pageable, 1);

        when(orderRepository.findByRestaurantIdAndStatus(
                restaurantId,
                status,
                pageable
        )).thenReturn(entityPage);

        when(orderEntityMapper.toDomain(entity))
                .thenReturn(domainOrder);

        Page<Order> result =
                orderJpaAdapter.findByRestaurantIdAndStatus(
                        restaurantId,
                        status,
                        page,
                        size
                );

        verify(orderRepository)
                .findByRestaurantIdAndStatus(
                        restaurantId,
                        status,
                        pageable
                );

        verify(orderEntityMapper)
                .toDomain(entity);

        assertEquals(1, result.getTotalElements());
        assertSame(domainOrder, result.getContent().get(0));
    }

    @Test
    void shouldReturnOrderWhenEntityExists() {

        Long orderId = 1L;

        OrderEntity entity = new OrderEntity();
        Order domainOrder = mock(Order.class);

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(entity));

        when(orderEntityMapper.toDomain(entity))
                .thenReturn(domainOrder);

        Optional<Order> result =
                orderJpaAdapter.findById(orderId);

        assertTrue(result.isPresent());
        assertSame(domainOrder, result.get());

        verify(orderRepository).findById(orderId);
        verify(orderEntityMapper).toDomain(entity);
    }

    @Test
    void shouldReturnPhoneWhenResponseIsValid() {

        Long userId = 5L;

        ClientPhoneResponseDto dto = new ClientPhoneResponseDto();
        dto.setPhoneNumber("+573001234567");

        ResponseEntity<ClientPhoneResponseDto> response =
                new ResponseEntity<>(dto, HttpStatus.OK);

        when(restTemplate.getForEntity(
                anyString(),
                eq(ClientPhoneResponseDto.class)
        )).thenReturn(response);

        String result = userJpaAdapter.getClientPhoneByUserId(userId);

        assertEquals("+573001234567", result);
    }

    @Test
    void shouldThrowWhenStatusIsNot2xx() {

        Long userId = 5L;

        ResponseEntity<ClientPhoneResponseDto> response =
                new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        when(restTemplate.getForEntity(
                anyString(),
                eq(ClientPhoneResponseDto.class)
        )).thenReturn(response);

        DomainException ex = assertThrows(
                DomainException.class,
                () -> userJpaAdapter.getClientPhoneByUserId(userId)
        );

        assertEquals(ErrorCode.EXTERNAL_SERVICE_ERROR, ex.getErrorCode());
        assertEquals("Invalid response from user service", ex.getMessage());
    }
    @Test
    void shouldThrowWhenRestClientExceptionOccurs() {

        Long userId = 5L;

        when(restTemplate.getForEntity(
                anyString(),
                eq(ClientPhoneResponseDto.class)
        )).thenThrow(new RestClientException("Connection error"));

        DomainException ex = assertThrows(
                DomainException.class,
                () -> userJpaAdapter.getClientPhoneByUserId(userId)
        );

        assertEquals(ErrorCode.EXTERNAL_SERVICE_ERROR, ex.getErrorCode());
        assertEquals("Error communicating with user service", ex.getMessage());
    }

}
