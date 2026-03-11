package com.pragma.plazoleta.domain.api;

import com.pragma.plazoleta.domain.model.Order;
import com.pragma.plazoleta.domain.model.PageResult;

public interface IOrderServicePort {
    Order saveOrder(Order order, Long userId, String email);
    PageResult<Order> listOrderByStatus(Long userId, String status, int page, int size);
    Order updateStatus(Long userId, Long orderId);
    Order updateStatusReady(Long userId, Long orderId);
    void updateStatusDelivery(String code, Long userId, Long orderId);
    void updateStatusCanceled(Long userId, Long orderId);
}
