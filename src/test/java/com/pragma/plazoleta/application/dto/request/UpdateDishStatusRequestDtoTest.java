package com.pragma.plazoleta.application.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UpdateDishStatusRequestDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldFailWhenActiveIsNull() {
        // arrange
        UpdateDishStatusRequestDto dto = new UpdateDishStatusRequestDto();
        dto.setActive(null);

        // act
        Set<ConstraintViolation<UpdateDishStatusRequestDto>> violations =
                validator.validate(dto);

        // assert
        assertFalse(violations.isEmpty());
        assertTrue(
                violations.stream()
                        .anyMatch(v ->
                                v.getMessage().equals("Status is required")
                        )
        );
    }

    @Test
    void shouldPassWhenActiveIsTrue() {
        // arrange
        UpdateDishStatusRequestDto dto = new UpdateDishStatusRequestDto();
        dto.setActive(true);

        // act
        Set<ConstraintViolation<UpdateDishStatusRequestDto>> violations =
                validator.validate(dto);

        // assert
        assertTrue(violations.isEmpty());
    }
}
