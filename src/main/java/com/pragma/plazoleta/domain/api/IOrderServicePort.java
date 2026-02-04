package com.pragma.plazoleta.domain.api;

import com.pragma.plazoleta.domain.model.Order;

public interface IOrderServicePort {
    Order saveOrder(Order order, Long userId, String rol);
}
