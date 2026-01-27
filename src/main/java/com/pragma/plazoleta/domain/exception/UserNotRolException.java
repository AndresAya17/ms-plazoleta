package com.pragma.plazoleta.domain.exception;

public class UserNotRolException extends RuntimeException{
    public UserNotRolException(Long userId) {
        super("El usuario con id " + userId + " no tiene el rol permitido.");
    }
}
