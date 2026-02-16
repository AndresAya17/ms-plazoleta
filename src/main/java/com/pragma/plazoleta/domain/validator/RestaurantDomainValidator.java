package com.pragma.plazoleta.domain.validator;

import com.pragma.plazoleta.domain.constants.DomainConstants;
import com.pragma.plazoleta.domain.exception.DomainException;
import com.pragma.plazoleta.domain.exception.ErrorCode;
import com.pragma.plazoleta.domain.model.Restaurant;

public class RestaurantDomainValidator {

    private RestaurantDomainValidator(){}

    public static void validateRestaurant(Restaurant restaurant) {
        validateName(restaurant);
        validateNit(restaurant);
        validateAddress(restaurant);
        validatePhoneNumber(restaurant);
        validateLogoUrl(restaurant);
        validateOwnerId(restaurant);
    }

    private static void validateName(Restaurant restaurant) {
        if (restaurant.getName() == null || restaurant.getName().trim().isEmpty()) {
            throw new DomainException(ErrorCode.INVALID_RESTAURANT ,"Restaurant name is required");
        }
    }

    private static void validateNit(Restaurant restaurant) {
        if (!restaurant.getNit().matches(DomainConstants.DOCUMENT_NUMBER_REGEX)) {
            throw new DomainException(ErrorCode.INVALID_RESTAURANT, "Restaurant nit is required");
        }
    }

    private static void validateAddress(Restaurant restaurant) {
        if (restaurant.getAddress() == null || restaurant.getAddress().trim().isEmpty()) {
            throw new DomainException(ErrorCode.INVALID_RESTAURANT, "Restaurant address is required");
        }
    }

    private static void validatePhoneNumber(Restaurant restaurant) {
        if (!restaurant.getPhoneNumber().matches(DomainConstants.PHONE_NUMBER_REGEX)) {
            throw new DomainException(ErrorCode.INVALID_RESTAURANT, "Restaurant phone is required");
        }
    }

    private static void validateLogoUrl(Restaurant restaurant) {
        if (restaurant.getLogoUrl() == null || restaurant.getLogoUrl().trim().isEmpty()) {
            throw new DomainException(ErrorCode.INVALID_RESTAURANT, "Restaurant logo_url is required");
        }
    }

    private static void validateOwnerId(Restaurant restaurant) {
        if (restaurant.getOwnerId() == null || restaurant.getOwnerId() <= 0) {
            throw new DomainException(ErrorCode.INVALID_RESTAURANT, "Restaurant owner is required");
        }
    }
}
