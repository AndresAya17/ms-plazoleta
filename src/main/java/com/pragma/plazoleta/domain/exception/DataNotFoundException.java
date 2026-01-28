package com.pragma.plazoleta.domain.exception;

public class DataNotFoundException extends RuntimeException{
    public DataNotFoundException(String data) {
        super(data + " not found");
    }
}
