package com.pragma.plazoleta.domain.exception;

public class InvalidRestaurantAddressException extends DomainException{
    public InvalidRestaurantAddressException(){
        super("Restaurant address is required");
    }
}
