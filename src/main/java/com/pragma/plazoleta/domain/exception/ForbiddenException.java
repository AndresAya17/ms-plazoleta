package com.pragma.plazoleta.domain.exception;

public class ForbiddenException extends DomainException{
    public ForbiddenException() {
        super("User does not have permission to access this resource");
    }
}
