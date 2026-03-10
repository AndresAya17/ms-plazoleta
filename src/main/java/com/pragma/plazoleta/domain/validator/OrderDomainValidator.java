package com.pragma.plazoleta.domain.validator;

import com.pragma.plazoleta.domain.exception.DomainException;
import com.pragma.plazoleta.domain.exception.ErrorCode;
import com.pragma.plazoleta.domain.model.Order;
import com.pragma.plazoleta.domain.model.OrderStatus;

public class OrderDomainValidator {

    private OrderDomainValidator(){}

    public static void accept(Order order) {
        if (order.getStatus() != OrderStatus.PENDIENTE) {
            throw new DomainException(ErrorCode.INVALID_ORDER_STATE, "Order cannot be accepted");
        }
        order.setStatus(OrderStatus.EN_PREPARACION);
    }

    public static void markAsReady(Order order) {
        if (order.getStatus() != OrderStatus.EN_PREPARACION) {
            throw new DomainException(ErrorCode.INVALID_ORDER_STATE, "Order is not in preparation");
        }
        order.setStatus(OrderStatus.LISTO);
    }

    public static void deliver(Order order) {
        if (order.getStatus() != OrderStatus.LISTO) {
            throw new DomainException(ErrorCode.INVALID_ORDER_STATE, "Order is not ready");
        }
        order.setStatus(OrderStatus.ENTREGADO);
    }

    public static void cancel(Order order) {
        if (order.getStatus() != OrderStatus.PENDIENTE) {
            throw new DomainException(ErrorCode.INVALID_ORDER_STATE, "Lo sentimos, tu pedido ya está en preparación y no puede cancelarse");
        }
        order.setStatus(OrderStatus.CANCELADO);
    }
}
