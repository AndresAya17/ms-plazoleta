package com.pragma.plazoleta.domain.model;

import com.pragma.plazoleta.domain.exception.DomainException;
import com.pragma.plazoleta.domain.exception.ErrorCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeRestaurantTest {

    @Test
    void shouldCreateEmployeeRestaurantWhenDataIsValid() {
        assertDoesNotThrow(() ->
                new EmployeeRestaurant(1L, 10L)
        );
    }

    @Test
    void shouldThrowExceptionWhenEmployeeUserIdIsNull() {
        DomainException ex = assertThrows(
                DomainException.class,
                () -> new EmployeeRestaurant(null, 10L)
        );

        assertEquals(ErrorCode.INVALID_EMPLOYEE, ex.getErrorCode());
        assertEquals("Employee user id is invalid", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenEmployeeUserIdIsZero() {
        DomainException ex = assertThrows(
                DomainException.class,
                () -> new EmployeeRestaurant(0L, 10L)
        );

        assertEquals(ErrorCode.INVALID_EMPLOYEE, ex.getErrorCode());
        assertEquals("Employee user id is invalid", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenEmployeeUserIdIsNegative() {
        DomainException ex = assertThrows(
                DomainException.class,
                () -> new EmployeeRestaurant(-5L, 10L)
        );

        assertEquals(ErrorCode.INVALID_EMPLOYEE, ex.getErrorCode());
        assertEquals("Employee user id is invalid", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenRestaurantIdIsNull() {
        DomainException ex = assertThrows(
                DomainException.class,
                () -> new EmployeeRestaurant(1L, null)
        );

        assertEquals(ErrorCode.INVALID_EMPLOYEE, ex.getErrorCode());
        assertEquals("Restaurant id is invalid", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenRestaurantIdIsZero() {
        DomainException ex = assertThrows(
                DomainException.class,
                () -> new EmployeeRestaurant(1L, 0L)
        );

        assertEquals(ErrorCode.INVALID_EMPLOYEE, ex.getErrorCode());
        assertEquals("Restaurant id is invalid", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenRestaurantIdIsNegative() {
        DomainException ex = assertThrows(
                DomainException.class,
                () -> new EmployeeRestaurant(1L, -10L)
        );

        assertEquals(ErrorCode.INVALID_EMPLOYEE, ex.getErrorCode());
        assertEquals("Restaurant id is invalid", ex.getMessage());
    }
}
