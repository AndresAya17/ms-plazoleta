package com.pragma.plazoleta.domain.exception;

public class UnauthorizedException extends DomainException{
    public UnauthorizedException() {
        super("User is not authorized to perform this action");
    }
}
