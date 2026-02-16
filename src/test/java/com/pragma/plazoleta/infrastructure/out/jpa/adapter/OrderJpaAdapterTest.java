package com.pragma.plazoleta.infrastructure.out.jpa.adapter;

import com.pragma.plazoleta.domain.model.Order;
import com.pragma.plazoleta.infrastructure.out.jpa.entity.OrderEntity;
import com.pragma.plazoleta.infrastructure.out.jpa.entity.OrderItemEntity;
import com.pragma.plazoleta.infrastructure.out.jpa.mapper.IOrderEntityMapper;
import com.pragma.plazoleta.infrastructure.out.jpa.repository.IOrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

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
}
