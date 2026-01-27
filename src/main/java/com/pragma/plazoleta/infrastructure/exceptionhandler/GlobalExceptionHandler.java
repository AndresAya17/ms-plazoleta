package com.pragma.plazoleta.infrastructure.exceptionhandler;

import com.pragma.plazoleta.domain.exception.DishNotFoundException;
import com.pragma.plazoleta.domain.exception.UserNotRolException;
import com.pragma.plazoleta.domain.exception.RestaurantOwnershipException;
import com.pragma.plazoleta.domain.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotRolException.class)
    public ResponseEntity<ErrorResponse> handleUserNotOwner(
            UserNotRolException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(RestaurantOwnershipException.class)
    public ResponseEntity<ErrorResponse> handleRestaurantOwnership(
            RestaurantOwnershipException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(
            UserNotFoundException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(DishNotFoundException.class)
    public ResponseEntity<ErrorResponse> handledishNotFound(
            DishNotFoundException ex
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ErrorResponse(ex.getMessage()));
    }
}
