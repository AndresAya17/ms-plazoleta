package com.pragma.plazoleta.domain.exception;

public class DishNotFoundException extends RuntimeException{
    public DishNotFoundException(Long dishId) {
        super("Dish with id " + dishId + " not found");
    }
}
