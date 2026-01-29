package com.pragma.plazoleta.domain.model;

import com.pragma.plazoleta.domain.constants.DomainConstants;
import com.pragma.plazoleta.domain.exception.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Restaurant {
    private Long id;
    private String name;
    private String nit;
    private String address;
    private String phoneNumber;
    private String logoUrl;
    private Long ownerId;

    public void validateName() {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidRestaurantNameException();
        }
    }

    public void validateNit() {
        if (!nit.matches(DomainConstants.DOCUMENT_NUMBER_REGEX)) {
            throw new InvalidRestaurantNitException();
        }
    }

    public void validateAddress() {
        if (address == null || address.trim().isEmpty()) {
            throw new InvalidRestaurantAddressException();
        }
    }

    public void validatePhoneNumber() {
        if (!phoneNumber.matches(DomainConstants.PHONE_NUMBER_REGEX)) {
            throw new InvalidRestaurantPhoneException();
        }
    }

    public void validateLogoUrl() {
        if (logoUrl == null || logoUrl.trim().isEmpty()) {
            throw new InvalidRestaurantLogoException();
        }
    }

    public void validateOwnerId() {
        if (ownerId == null || ownerId <= 0) {
            throw new InvalidRestaurantOwnerException();
        }
    }

    public void validateRestaurant() {
        validateName();
        validateNit();
        validateAddress();
        validatePhoneNumber();
        validateLogoUrl();
        validateOwnerId();
    }
}
