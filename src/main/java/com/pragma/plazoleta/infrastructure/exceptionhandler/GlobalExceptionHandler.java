package com.pragma.plazoleta.infrastructure.exceptionhandler;

import com.pragma.plazoleta.domain.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(DomainException ex) {

        HttpStatus status = mapStatus(ex.getErrorCode());

        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(
                        ex.getErrorCode().name(),
                        ex.getMessage()
                ));
    }

    private HttpStatus mapStatus(ErrorCode errorCode) {
        return switch (errorCode) {
            case DATA_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case UNAUTHORIZED -> HttpStatus.UNAUTHORIZED;
            case FORBIDDEN -> HttpStatus.FORBIDDEN;
            case INVALID_DISH,
                 INVALID_EMPLOYEE,
                 INVALID_RESTAURANT -> HttpStatus.BAD_REQUEST;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}
