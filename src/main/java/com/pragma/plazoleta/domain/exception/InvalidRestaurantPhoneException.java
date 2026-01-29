package com.pragma.plazoleta.domain.exception;

public class InvalidRestaurantPhoneException extends DomainException{
    public InvalidRestaurantPhoneException(){
        super("Phone number must be numeric, may start with '+' and contain up to 13 digits");
    }
}
