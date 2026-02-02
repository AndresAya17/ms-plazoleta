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
            throw new DomainException(ErrorCode.INVALID_RESTAURANT ,"Restaurant name is required");
        }
    }

    public void validateNit() {
        if (!nit.matches(DomainConstants.DOCUMENT_NUMBER_REGEX)) {
            throw new DomainException(ErrorCode.INVALID_RESTAURANT, "Restaurant nit is required");
        }
    }

    public void validateAddress() {
        if (address == null || address.trim().isEmpty()) {
            throw new DomainException(ErrorCode.INVALID_RESTAURANT, "Restaurant address is required");
        }
    }

    public void validatePhoneNumber() {
        if (!phoneNumber.matches(DomainConstants.PHONE_NUMBER_REGEX)) {
            throw new DomainException(ErrorCode.INVALID_RESTAURANT, "Restaurant phone is required");
        }
    }

    public void validateLogoUrl() {
        if (logoUrl == null || logoUrl.trim().isEmpty()) {
            throw new DomainException(ErrorCode.INVALID_RESTAURANT, "Restaurant logo_url is required");
        }
    }

    public void validateOwnerId() {
        if (ownerId == null || ownerId <= 0) {
            throw new DomainException(ErrorCode.INVALID_RESTAURANT, "Restaurant owner is required");
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
