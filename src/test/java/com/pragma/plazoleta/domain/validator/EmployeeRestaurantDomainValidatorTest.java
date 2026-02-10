package com.pragma.plazoleta.domain.validator;

import com.pragma.plazoleta.domain.exception.DomainException;
import com.pragma.plazoleta.domain.exception.ErrorCode;
import com.pragma.plazoleta.domain.model.EmployeeRestaurant;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeRestaurantDomainValidatorTest {

    private EmployeeRestaurant buildValidEmployeeRestaurant() {
        EmployeeRestaurant employeeRestaurant = new EmployeeRestaurant();
        employeeRestaurant.setEmployeeUserId(1L);
        employeeRestaurant.setRestaurantId(1L);
        return employeeRestaurant;
    }

    @Test
    void shouldNotThrowExceptionWhenEmployeeRestaurantIsValid() {
        EmployeeRestaurant employeeRestaurant = buildValidEmployeeRestaurant();

        assertDoesNotThrow(() ->
                EmployeeRestaurantDomainValidator.validate(employeeRestaurant)
        );
    }

    @Test
    void shouldThrowExceptionWhenEmployeeUserIdIsNull() {
        EmployeeRestaurant employeeRestaurant = buildValidEmployeeRestaurant();
        employeeRestaurant.setEmployeeUserId(null);

        DomainException exception = assertThrows(
                DomainException.class,
                () -> EmployeeRestaurantDomainValidator.validate(employeeRestaurant)
        );

        assertEquals(ErrorCode.INVALID_EMPLOYEE, exception.getErrorCode());
    }

    @Test
    void shouldThrowExceptionWhenEmployeeUserIdIsZeroOrNegative() {
        EmployeeRestaurant employeeRestaurant = buildValidEmployeeRestaurant();
        employeeRestaurant.setEmployeeUserId(0L);

        DomainException exception = assertThrows(
                DomainException.class,
                () -> EmployeeRestaurantDomainValidator.validate(employeeRestaurant)
        );

        assertEquals(ErrorCode.INVALID_EMPLOYEE, exception.getErrorCode());
    }

    @Test
    void shouldThrowExceptionWhenRestaurantIdIsNull() {
        EmployeeRestaurant employeeRestaurant = buildValidEmployeeRestaurant();
        employeeRestaurant.setRestaurantId(null);

        DomainException exception = assertThrows(
                DomainException.class,
                () -> EmployeeRestaurantDomainValidator.validate(employeeRestaurant)
        );

        assertEquals(ErrorCode.INVALID_EMPLOYEE, exception.getErrorCode());
    }

    @Test
    void shouldThrowExceptionWhenRestaurantIdIsZeroOrNegative() {
        EmployeeRestaurant employeeRestaurant = buildValidEmployeeRestaurant();
        employeeRestaurant.setRestaurantId(0L);

        DomainException exception = assertThrows(
                DomainException.class,
                () -> EmployeeRestaurantDomainValidator.validate(employeeRestaurant)
        );

        assertEquals(ErrorCode.INVALID_EMPLOYEE, exception.getErrorCode());
    }
}
