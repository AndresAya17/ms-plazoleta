package com.pragma.plazoleta.infrastructure.out.jpa.mapper;

import com.pragma.plazoleta.domain.model.Order;
import com.pragma.plazoleta.domain.model.OrderItem;
import com.pragma.plazoleta.domain.model.OrderStatus;
import com.pragma.plazoleta.infrastructure.out.jpa.entity.OrderEntity;
import com.pragma.plazoleta.infrastructure.out.jpa.entity.OrderItemEntity;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class IOrderEntityMapperTest {

    private final IOrderEntityMapper mapper =
            Mappers.getMapper(IOrderEntityMapper.class);

    @Test
    void shouldMapOrderToOrderEntity() {
        OrderItem item = new OrderItem(
                1L,
                2
        );

        Order order = new Order(
                10L,
                20L,
                List.of(item)
        );
        order.setId(5L);
        order.setStatus(OrderStatus.PENDIENTE);
        order.setCreatedAt(LocalDateTime.now());

        OrderEntity entity = mapper.toEntity(order);

        assertNotNull(entity);
        assertEquals(order.getId(), entity.getId());
        assertEquals(order.getClientId(), entity.getClientId());
        assertEquals(order.getRestaurantId(), entity.getRestaurantId());
        assertEquals(order.getStatus(), entity.getStatus());
        assertEquals(order.getCreatedAt(), entity.getCreatedAt());
        assertNotNull(entity.getItems());
        assertEquals(1, entity.getItems().size());
    }

    @Test
    void shouldMapOrderEntityToOrder() {
        OrderItemEntity itemEntity = new OrderItemEntity();
        itemEntity.setDishId(1L);
        itemEntity.setQuantity(2);

        OrderEntity entity = OrderEntity.builder()
                .id(5L)
                .clientId(10L)
                .restaurantId(20L)
                .status(OrderStatus.EN_PREPARACION)
                .createdAt(LocalDateTime.now())
                .items(List.of(itemEntity))
                .build();

        Order order = mapper.toDomain(entity);

        assertNotNull(order);
        assertEquals(entity.getId(), order.getId());
        assertEquals(entity.getClientId(), order.getClientId());
        assertEquals(entity.getRestaurantId(), order.getRestaurantId());
        assertEquals(entity.getStatus(), order.getStatus());
        assertEquals(entity.getCreatedAt(), order.getCreatedAt());
        assertNotNull(order.getItems());
        assertEquals(1, order.getItems().size());
    }

    @Test
    void shouldReturnNullWhenOrderIsNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void shouldReturnNullWhenOrderEntityIsNull() {
        assertNull(mapper.toDomain(null));
    }
}
