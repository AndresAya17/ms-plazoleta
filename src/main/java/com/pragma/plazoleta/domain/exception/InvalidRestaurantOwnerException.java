package com.pragma.plazoleta.domain.exception;

public class InvalidRestaurantOwnerException extends DomainException{
    public InvalidRestaurantOwnerException(){
        super("Owner id must be a valid positive number");
    }
}
