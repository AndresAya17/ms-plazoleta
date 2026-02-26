package com.pragma.plazoleta.infrastructure.out.jpa.adapter;

import com.pragma.plazoleta.domain.model.Order;
import com.pragma.plazoleta.domain.model.OrderStatus;
import com.pragma.plazoleta.domain.model.PageResult;
import com.pragma.plazoleta.domain.spi.IOrderPersistencePort;
import com.pragma.plazoleta.infrastructure.out.jpa.entity.OrderEntity;
import com.pragma.plazoleta.infrastructure.out.jpa.mapper.IOrderEntityMapper;
import com.pragma.plazoleta.infrastructure.out.jpa.repository.IOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

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

    @Override
    public PageResult<Order> findByRestaurantIdAndStatus(Long restaurantId, OrderStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<OrderEntity> orderEntities =
                orderRepository.findByRestaurantIdAndStatus(
                        restaurantId,
                        status,
                        pageable
                );

        List<Order> orders = orderEntities.getContent().
                stream()
                .map(orderEntityMapper::toDomain)
                .toList();

        return new PageResult<>(
                orders,
                orderEntities.getNumber(),
                orderEntities.getSize(),
                orderEntities.getTotalElements()
        );
    }

    @Override
    public Optional<Order> findById(Long orderId) {
        return orderRepository.findById(orderId)
                .map(orderEntityMapper::toDomain);
    }
}
