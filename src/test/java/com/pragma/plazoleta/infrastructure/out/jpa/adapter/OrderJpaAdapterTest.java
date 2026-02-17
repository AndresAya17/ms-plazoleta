package com.pragma.plazoleta.infrastructure.out.jpa.adapter;

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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderJpaAdapterTest {
    @Mock
    private IOrderRepository orderRepository;

    @Mock
    private IOrderEntityMapper orderEntityMapper;

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
}
