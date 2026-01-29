package com.pragma.plazoleta.domain.exception;

public class InvalidRestaurantIdException extends DomainException{
    public InvalidRestaurantIdException(){
        super("The restaurant ID is required and must be greater than zero.");
    }
}
