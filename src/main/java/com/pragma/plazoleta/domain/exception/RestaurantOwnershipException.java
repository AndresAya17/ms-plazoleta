package com.pragma.plazoleta.domain.exception;

public class RestaurantOwnershipException extends RuntimeException{
    public RestaurantOwnershipException() {
        super("The user is not the owner of this restaurant");
    }
}
