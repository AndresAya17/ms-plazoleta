package com.pragma.plazoleta.domain.exception;

public class InvalidDishCategoryException extends DomainException{
    public InvalidDishCategoryException(){
        super("The dish category is required.");
    }
}
