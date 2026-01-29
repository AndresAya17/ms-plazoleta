package com.pragma.plazoleta.domain.exception;

public class InvalidDishDescriptionException extends DomainException{
    public InvalidDishDescriptionException(){
        super("The dish description is required and cannot be empty.");
    }
}
