package com.pragma.plazoleta.domain.exception;

public class InvalidRestaurantNitException extends DomainException{
    public InvalidRestaurantNitException(){
        super("NIT must contain only numeric characters");
    }
}
