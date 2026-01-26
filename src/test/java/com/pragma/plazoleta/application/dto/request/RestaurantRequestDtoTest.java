package com.pragma.plazoleta.application.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class RestaurantRequestDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private RestaurantRequestDto buildValidDto() {
        RestaurantRequestDto dto = new RestaurantRequestDto();
        dto.setName("Restaurante Prueba");
        dto.setNit("123456789");
        dto.setAddress("Calle 123 #45-67");
        dto.setPhoneNumber("+573001234567");
        dto.setLogoUrl("https://logo.com/logo.png");
        dto.setOwnerId(1L);
        return dto;
    }


    @Test
    void shouldPassValidationWhenAllFieldsAreValid() {
        RestaurantRequestDto dto = buildValidDto();

        Set<ConstraintViolation<RestaurantRequestDto>> violations =
                validator.validate(dto);

        assertTrue(violations.isEmpty());
    }


    @Test
    void shouldFailWhenNameIsBlank() {
        RestaurantRequestDto dto = buildValidDto();
        dto.setName("");

        Set<ConstraintViolation<RestaurantRequestDto>> violations =
                validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getMessage().equals("El nombre es obligatorio"))
        );
    }


    @Test
    void shouldFailWhenNitIsBlank() {
        RestaurantRequestDto dto = buildValidDto();
        dto.setNit("");

        Set<ConstraintViolation<RestaurantRequestDto>> violations =
                validator.validate(dto);

        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getMessage().equals("El NIT es obligatorio"))
        );
    }


    @Test
    void shouldFailWhenNitContainsLetters() {
        RestaurantRequestDto dto = buildValidDto();
        dto.setNit("ABC123");

        Set<ConstraintViolation<RestaurantRequestDto>> violations =
                validator.validate(dto);

        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getMessage().equals("El NIT debe contener solo números"))
        );
    }


    @Test
    void shouldFailWhenAddressIsBlank() {
        RestaurantRequestDto dto = buildValidDto();
        dto.setAddress(" ");

        Set<ConstraintViolation<RestaurantRequestDto>> violations =
                validator.validate(dto);

        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getMessage().equals("La dirección es obligatoria"))
        );
    }


    @Test
    void shouldFailWhenPhoneNumberIsBlank() {
        RestaurantRequestDto dto = buildValidDto();
        dto.setPhoneNumber("");

        Set<ConstraintViolation<RestaurantRequestDto>> violations =
                validator.validate(dto);

        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getMessage().equals("El teléfono es obligatorio"))
        );
    }


    @Test
    void shouldFailWhenPhoneNumberIsInvalid() {
        RestaurantRequestDto dto = buildValidDto();
        dto.setPhoneNumber("123ABC");

        Set<ConstraintViolation<RestaurantRequestDto>> violations =
                validator.validate(dto);

        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getMessage().contains("El teléfono debe ser numérico"))
        );
    }


    @Test
    void shouldFailWhenLogoUrlIsBlank() {
        RestaurantRequestDto dto = buildValidDto();
        dto.setLogoUrl(null);

        Set<ConstraintViolation<RestaurantRequestDto>> violations =
                validator.validate(dto);

        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getMessage().equals("La URL del logo es obligatoria"))
        );
    }


    @Test
    void shouldFailWhenOwnerIdIsNull() {
        RestaurantRequestDto dto = buildValidDto();
        dto.setOwnerId(null);

        Set<ConstraintViolation<RestaurantRequestDto>> violations =
                validator.validate(dto);

        assertTrue(
                violations.stream()
                        .anyMatch(v -> v.getMessage().equals("El id del propietario es obligatorio"))
        );
    }
}
