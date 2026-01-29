package com.pragma.plazoleta.domain.exception;

public class InvalidDishNameException extends DomainException{
    public InvalidDishNameException(){
        super("The dish name is required and cannot be empty.");
    }
}
