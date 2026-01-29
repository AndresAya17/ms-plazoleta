package com.pragma.plazoleta.domain.exception;

public class InvalidRestaurantNameException extends DomainException{
    public InvalidRestaurantNameException(){
        super("Restaurant name is required");
    }
}
