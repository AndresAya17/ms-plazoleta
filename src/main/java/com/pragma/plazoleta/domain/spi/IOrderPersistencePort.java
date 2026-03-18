package com.pragma.plazoleta.domain.spi;

import com.pragma.plazoleta.domain.model.Order;
import com.pragma.plazoleta.domain.model.OrderStatus;
import com.pragma.plazoleta.domain.model.PageResult;

import java.util.List;
import java.util.Optional;

public interface IOrderPersistencePort {
    Order saveOrder(Order order);
    PageResult<Order> findByRestaurantIdAndStatus(Long restaurantId, OrderStatus orderStatus, int page, int size);
    Optional<Order> findById(Long orderId);
    boolean existsByClientIdAndStatusNotIn(Long clientId, List<OrderStatus> excludedStatuses);
    List<Long> getOrdersByRestaurantId(Long restaurantId);
}
