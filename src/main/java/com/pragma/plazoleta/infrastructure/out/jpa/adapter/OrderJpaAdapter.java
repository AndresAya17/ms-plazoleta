package com.pragma.plazoleta.infrastructure.out.jpa.adapter;

import com.pragma.plazoleta.domain.model.Order;
import com.pragma.plazoleta.domain.spi.IOrderPersistencePort;
import com.pragma.plazoleta.infrastructure.out.jpa.entity.OrderEntity;
import com.pragma.plazoleta.infrastructure.out.jpa.mapper.IOrderEntityMapper;
import com.pragma.plazoleta.infrastructure.out.jpa.repository.IOrderRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderJpaAdapter implements IOrderPersistencePort {
    private final IOrderRepository orderRepository;
    private final IOrderEntityMapper orderEntityMapper;


    @Override
    public Order saveOrder(Order order) {
        OrderEntity orderEntity = orderEntityMapper.toEntity(order);

        orderEntity.getItems()
                .forEach(item -> item.setOrder(orderEntity));

        OrderEntity savedEntity = orderRepository.save(orderEntity);

        return orderEntityMapper.toDomain(savedEntity);
    }
}
