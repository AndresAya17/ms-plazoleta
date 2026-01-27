package com.pragma.plazoleta.domain.exception;

public class RestaurantOwnershipException extends RuntimeException{
    public RestaurantOwnershipException(Long userId, Long restaurantId) {
        super("El usuario " + userId + " no es propietario del restaurante " + restaurantId);
    }
}
