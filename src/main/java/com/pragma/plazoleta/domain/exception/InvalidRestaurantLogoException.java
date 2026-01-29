package com.pragma.plazoleta.domain.exception;

public class InvalidRestaurantLogoException extends DomainException{
    public InvalidRestaurantLogoException(){
        super("Logo URL is required");
    }
}
