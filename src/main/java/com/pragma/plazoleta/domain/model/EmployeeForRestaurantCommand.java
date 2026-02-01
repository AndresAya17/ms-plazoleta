package com.pragma.plazoleta.domain.model;

import com.pragma.plazoleta.domain.constants.DomainConstants;
import com.pragma.plazoleta.domain.exception.DomainException;
import com.pragma.plazoleta.domain.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EmployeeForRestaurantCommand {
    private Long restaurantId;
    private Long ownerId;

    private String firstName;
    private String lastName;
    private String documentNumber;
    private String phoneNumber;
    private String email;
    private String password;

    public void validateEmployee() {
        validateRestaurantId();
        validateOwnerId();
        validateName();
        validateLastName();
        validateDocumentId();
        validatePhone();
        validateEmail();
        validatePassword();
    }

    public void validateRestaurantId() {
        if (restaurantId == null || restaurantId <= 0) {
            throw new DomainException(ErrorCode.INVALID_EMPLOYEE, "RestaurantId is invalid");
        }
    }

    public void validateOwnerId() {
        if (ownerId == null || ownerId <= 0) {
            throw new DomainException(ErrorCode.INVALID_EMPLOYEE, "OwnerId is invalid");
        }
    }

    public void validateName() {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new DomainException(ErrorCode.INVALID_EMPLOYEE, "Employee name is invalid");
        }
    }

    public void validateLastName() {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new DomainException(ErrorCode.INVALID_EMPLOYEE, "Employee lastname is invalid");
        }
    }

    public void validateDocumentId() {
        if (documentNumber == null || !documentNumber.matches(DomainConstants.DOCUMENT_NUMBER_REGEX)) {
            throw new DomainException(ErrorCode.INVALID_EMPLOYEE, "Employee documentId is invalid");
        }
    }

    public void validatePhone() {
        if (phoneNumber == null || !phoneNumber.matches(DomainConstants.PHONE_NUMBER_REGEX)) {
            throw new DomainException(ErrorCode.INVALID_EMPLOYEE, "Employee phone is invalid");
        }
    }

    public void validateEmail() {
        if (email == null || !email.contains("@")) {
            throw new DomainException(ErrorCode.INVALID_EMPLOYEE, "Employee email is invalid");
        }
    }

    public void validatePassword() {
        if (password == null || password.trim().isEmpty()) {
            throw new DomainException(ErrorCode.INVALID_EMPLOYEE, "Employee password is invalid");
        }
    }
}
