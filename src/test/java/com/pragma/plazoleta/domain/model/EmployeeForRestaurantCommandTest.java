package com.pragma.plazoleta.domain.model;

import com.pragma.plazoleta.domain.exception.DomainException;
import com.pragma.plazoleta.domain.exception.ErrorCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeForRestaurantCommandTest {

    private EmployeeForRestaurantCommand buildValidEmployee() {
        return new EmployeeForRestaurantCommand(
                1L,                     // restaurantId
                1L,                     // ownerId
                "Juan",                 // firstName
                "Perez",                // lastName
                "123456789",            // documentNumber
                "+573001234567",        // phoneNumber
                "juan@mail.com",        // email
                "password123"           // password
        );
    }

    @Test
    void shouldNotThrowExceptionWhenEmployeeIsValid() {
        EmployeeForRestaurantCommand command = buildValidEmployee();

        assertDoesNotThrow(command::validateEmployee);
    }
    @Test
    void shouldThrowExceptionWhenRestaurantIdIsNull() {
        EmployeeForRestaurantCommand command = buildValidEmployee();
        command.setRestaurantId(null);

        DomainException ex = assertThrows(
                DomainException.class,
                command::validateRestaurantId
        );

        assertEquals(ErrorCode.INVALID_EMPLOYEE, ex.getErrorCode());
        assertEquals("RestaurantId is invalid", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenRestaurantIdIsZero() {
        EmployeeForRestaurantCommand command = buildValidEmployee();
        command.setRestaurantId(0L);

        DomainException ex = assertThrows(
                DomainException.class,
                command::validateRestaurantId
        );

        assertEquals(ErrorCode.INVALID_EMPLOYEE, ex.getErrorCode());
        assertEquals("RestaurantId is invalid", ex.getMessage());
    }

    // ---------- OwnerId ----------
    @Test
    void shouldThrowExceptionWhenOwnerIdIsInvalid() {
        EmployeeForRestaurantCommand command = buildValidEmployee();
        command.setOwnerId(-1L);

        DomainException ex = assertThrows(
                DomainException.class,
                command::validateOwnerId
        );

        assertEquals(ErrorCode.INVALID_EMPLOYEE, ex.getErrorCode());
        assertEquals("OwnerId is invalid", ex.getMessage());
    }

    // ---------- First name ----------
    @Test
    void shouldThrowExceptionWhenFirstNameIsInvalid() {
        EmployeeForRestaurantCommand command = buildValidEmployee();
        command.setFirstName(" ");

        DomainException ex = assertThrows(
                DomainException.class,
                command::validateName
        );

        assertEquals(ErrorCode.INVALID_EMPLOYEE, ex.getErrorCode());
        assertEquals("Employee name is invalid", ex.getMessage());
    }

    // ---------- Last name ----------
    @Test
    void shouldThrowExceptionWhenLastNameIsInvalid() {
        EmployeeForRestaurantCommand command = buildValidEmployee();
        command.setLastName(null);

        DomainException ex = assertThrows(
                DomainException.class,
                command::validateLastName
        );

        assertEquals(ErrorCode.INVALID_EMPLOYEE, ex.getErrorCode());
        assertEquals("Employee lastname is invalid", ex.getMessage());
    }

    // ---------- Document number ----------
    @Test
    void shouldThrowExceptionWhenDocumentNumberIsInvalid() {
        EmployeeForRestaurantCommand command = buildValidEmployee();
        command.setDocumentNumber("ABC123");

        DomainException ex = assertThrows(
                DomainException.class,
                command::validateDocumentId
        );

        assertEquals(ErrorCode.INVALID_EMPLOYEE, ex.getErrorCode());
        assertEquals("Employee documentId is invalid", ex.getMessage());
    }

    // ---------- Phone ----------
    @Test
    void shouldThrowExceptionWhenPhoneNumberIsInvalid() {
        EmployeeForRestaurantCommand command = buildValidEmployee();
        command.setPhoneNumber("123ABC");

        DomainException ex = assertThrows(
                DomainException.class,
                command::validatePhone
        );

        assertEquals(ErrorCode.INVALID_EMPLOYEE, ex.getErrorCode());
        assertEquals("Employee phone is invalid", ex.getMessage());
    }

    // ---------- Email ----------
    @Test
    void shouldThrowExceptionWhenEmailIsInvalid() {
        EmployeeForRestaurantCommand command = buildValidEmployee();
        command.setEmail("juanmail.com");

        DomainException ex = assertThrows(
                DomainException.class,
                command::validateEmail
        );

        assertEquals(ErrorCode.INVALID_EMPLOYEE, ex.getErrorCode());
        assertEquals("Employee email is invalid", ex.getMessage());
    }

    // ---------- Password ----------
    @Test
    void shouldThrowExceptionWhenPasswordIsInvalid() {
        EmployeeForRestaurantCommand command = buildValidEmployee();
        command.setPassword(" ");

        DomainException ex = assertThrows(
                DomainException.class,
                command::validatePassword
        );

        assertEquals(ErrorCode.INVALID_EMPLOYEE, ex.getErrorCode());
        assertEquals("Employee password is invalid", ex.getMessage());
    }

    // ---------- validateEmployee (orchestrator) ----------
    @Test
    void shouldThrowExceptionWhenValidateEmployeeAndRestaurantIdIsInvalid() {
        EmployeeForRestaurantCommand command = buildValidEmployee();
        command.setRestaurantId(null);

        DomainException ex = assertThrows(
                DomainException.class,
                command::validateEmployee
        );

        assertEquals(ErrorCode.INVALID_EMPLOYEE, ex.getErrorCode());
        assertEquals("RestaurantId is invalid", ex.getMessage());
    }
}
