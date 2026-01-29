package com.pragma.plazoleta.domain.exception;

public class InvalidDishImageException extends DomainException{
    public InvalidDishImageException(){
        super("The dish image URL is required and cannot be empty.");
    }
}
