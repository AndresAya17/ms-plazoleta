package com.pragma.plazoleta.domain.spi;

import com.pragma.plazoleta.domain.model.Order;
import com.pragma.plazoleta.domain.model.OrderStatus;
import org.springframework.data.domain.Page;

public interface IOrderPersistencePort {
    Order saveOrder(Order order);
    Page<Order> findByRestaurantIdAndStatus(Long restaurantId, OrderStatus orderStatus, int page, int size);
}
