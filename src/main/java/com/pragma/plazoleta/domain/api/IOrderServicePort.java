package com.pragma.plazoleta.domain.api;

import com.pragma.plazoleta.domain.model.Order;
import org.springframework.data.domain.Page;

public interface IOrderServicePort {
    Order saveOrder(Order order, Long userId);
    Page<Order> listOrderByStatus(Long userId, String status, int page, int size);
    Order updateStatus(Long userId, Long orderId);
}
