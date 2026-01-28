package com.pragma.plazoleta.domain.exception;

public class UserNotRolException extends RuntimeException{
    public UserNotRolException() {
        super("El usuario no tiene el rol permitido.");
    }
}
