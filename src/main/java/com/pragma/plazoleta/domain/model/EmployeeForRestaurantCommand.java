package com.pragma.plazoleta.domain.model;

import com.pragma.plazoleta.domain.constants.DomainConstants;
import com.pragma.plazoleta.domain.exception.InvalidEmployeeException;
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
            throw new InvalidEmployeeException("RestaurantId is invalid");
        }
    }

    public void validateOwnerId() {
        if (ownerId == null || ownerId <= 0) {
            throw new InvalidEmployeeException("OwnerId is invalid");
        }
    }

    public void validateName() {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new InvalidEmployeeException("Employee name is invalid");
        }
    }

    public void validateLastName() {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new InvalidEmployeeException("Employee lastname is invalid");
        }
    }

    public void validateDocumentId() {
        if (documentNumber == null || !documentNumber.matches(DomainConstants.DOCUMENT_NUMBER_REGEX)) {
            throw new InvalidEmployeeException("Employee documentId is invalid");
        }
    }

    public void validatePhone() {
        if (phoneNumber == null || !phoneNumber.matches(DomainConstants.PHONE_NUMBER_REGEX)) {
            throw new InvalidEmployeeException("Employee phone is invalid");
        }
    }

    public void validateEmail() {
        if (email == null || !email.contains("@")) {
            throw new InvalidEmployeeException("Employee email is invalid");
        }
    }

    public void validatePassword() {
        if (password == null || password.trim().isEmpty()) {
            throw new InvalidEmployeeException("Employee password is invalid");
        }
    }
}
