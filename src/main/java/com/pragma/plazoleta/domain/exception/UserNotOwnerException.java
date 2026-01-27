package com.pragma.plazoleta.domain.exception;

public class UserNotOwnerException extends RuntimeException{
    public UserNotOwnerException(Long userId) {
        super("El usuario con id " + userId + " no es propietario");
    }
}
