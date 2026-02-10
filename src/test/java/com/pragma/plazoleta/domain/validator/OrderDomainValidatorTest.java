package com.pragma.plazoleta.domain.validator;

import com.pragma.plazoleta.domain.exception.DomainException;
import com.pragma.plazoleta.domain.exception.ErrorCode;
import com.pragma.plazoleta.domain.model.Order;
import com.pragma.plazoleta.domain.model.OrderStatus;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderDomainValidatorTest {

    private Order buildOrderWithStatus(OrderStatus status) {
        Order order = new Order(
                1L,
                1L,
                List.of()
        );
        order.setStatus(status);
        return order;
    }
    @Test
    void shouldAcceptOrderWhenStatusIsPending() {
        Order order = buildOrderWithStatus(OrderStatus.PENDIENTE);

        assertDoesNotThrow(() ->
                OrderDomainValidator.accept(order)
        );

        assertEquals(OrderStatus.EN_PREPARACION, order.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenAcceptingOrderNotPending() {
        Order order = buildOrderWithStatus(OrderStatus.EN_PREPARACION);

        DomainException exception = assertThrows(
                DomainException.class,
                () -> OrderDomainValidator.accept(order)
        );

        assertEquals(ErrorCode.INVALID_ORDER_STATE, exception.getErrorCode());
    }

    @Test
    void shouldMarkOrderAsReadyWhenStatusIsInPreparation() {
        Order order = buildOrderWithStatus(OrderStatus.EN_PREPARACION);

        assertDoesNotThrow(() ->
                OrderDomainValidator.markAsReady(order)
        );

        assertEquals(OrderStatus.LISTO, order.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenMarkingReadyOrderNotInPreparation() {
        Order order = buildOrderWithStatus(OrderStatus.PENDIENTE);

        DomainException exception = assertThrows(
                DomainException.class,
                () -> OrderDomainValidator.markAsReady(order)
        );

        assertEquals(ErrorCode.INVALID_ORDER_STATE, exception.getErrorCode());
    }

    @Test
    void shouldDeliverOrderWhenStatusIsReady() {
        Order order = buildOrderWithStatus(OrderStatus.LISTO);

        assertDoesNotThrow(() ->
                OrderDomainValidator.deliver(order)
        );

        assertEquals(OrderStatus.ENTREGADO, order.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenDeliveringOrderNotReady() {
        Order order = buildOrderWithStatus(OrderStatus.EN_PREPARACION);

        DomainException exception = assertThrows(
                DomainException.class,
                () -> OrderDomainValidator.deliver(order)
        );

        assertEquals(ErrorCode.INVALID_ORDER_STATE, exception.getErrorCode());
    }

    @Test
    void shouldCancelOrderWhenStatusIsPending() {
        Order order = buildOrderWithStatus(OrderStatus.PENDIENTE);

        assertDoesNotThrow(() ->
                OrderDomainValidator.cancel(order)
        );

        assertEquals(OrderStatus.CANCELADO, order.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenCancelingOrderNotPending() {
        Order order = buildOrderWithStatus(OrderStatus.LISTO);

        DomainException exception = assertThrows(
                DomainException.class,
                () -> OrderDomainValidator.cancel(order)
        );

        assertEquals(ErrorCode.INVALID_ORDER_STATE, exception.getErrorCode());
    }
}
