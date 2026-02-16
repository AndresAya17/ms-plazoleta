package com.pragma.plazoleta.domain.validator;

import com.pragma.plazoleta.domain.exception.DomainException;
import com.pragma.plazoleta.domain.exception.ErrorCode;
import com.pragma.plazoleta.domain.model.EmployeeRestaurant;

public class EmployeeRestaurantDomainValidator {

    private EmployeeRestaurantDomainValidator(){}


    public static void validate(EmployeeRestaurant employeeRestaurant) {
        validateEmployeeUserId(employeeRestaurant);
        validateRestaurantId(employeeRestaurant);
    }

    private static void validateEmployeeUserId(EmployeeRestaurant employeeRestaurant) {
        if (employeeRestaurant.getEmployeeUserId() == null || employeeRestaurant.getEmployeeUserId() <= 0) {
            throw new DomainException(ErrorCode.INVALID_EMPLOYEE, "Employee user id is invalid") {
            };
        }
    }

    private static void validateRestaurantId(EmployeeRestaurant employeeRestaurant) {
        if (employeeRestaurant.getRestaurantId() == null || employeeRestaurant.getRestaurantId() <= 0) {
            throw new DomainException(ErrorCode.INVALID_EMPLOYEE, "Restaurant id is invalid");
        }
    }
}
