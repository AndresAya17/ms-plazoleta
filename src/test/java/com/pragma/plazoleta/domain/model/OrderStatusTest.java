package com.pragma.plazoleta.domain.model;

import com.pragma.plazoleta.domain.exception.DomainException;
import com.pragma.plazoleta.domain.exception.ErrorCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class OrderStatusTest {

    @Test
    void shouldReturnCorrectEnumWhenValueIsValidLowerCase() {
        OrderStatus status = OrderStatus.from("pendiente");
        assertEquals(OrderStatus.PENDIENTE, status);
    }

    @Test
    void shouldThrowDomainExceptionWhenValueIsNull() {
        DomainException exception = assertThrows(
                DomainException.class,
                () -> OrderStatus.from(null)
        );

        assertEquals(ErrorCode.INVALID_STATUS, exception.getErrorCode());
    }
}
