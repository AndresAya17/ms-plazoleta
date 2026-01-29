package com.pragma.plazoleta.domain.exception;

public class InvalidDishPriceException extends DomainException{
    public InvalidDishPriceException(){
        super("The dish price must be greater than zero.");
    }
}
