package com.pragma.plazoleta.application.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateOrderRequestDtoTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldCreateValidCreateOrderRequestDto() {
        CreateOrderRequestDto dto = new CreateOrderRequestDto();
        dto.setRestaurantId(1L);
        dto.setItems(List.of(new OrderItemRequestDto()));

        Set<ConstraintViolation<CreateOrderRequestDto>> violations =
                validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailWhenRestaurantIdIsNull() {
        CreateOrderRequestDto dto = new CreateOrderRequestDto();
        dto.setItems(List.of(new OrderItemRequestDto()));

        Set<ConstraintViolation<CreateOrderRequestDto>> violations =
                validator.validate(dto);

        assertEquals(1, violations.size());
        ConstraintViolation<CreateOrderRequestDto> violation =
                violations.iterator().next();

        assertEquals("Restaurant id is required", violation.getMessage());
    }
}
