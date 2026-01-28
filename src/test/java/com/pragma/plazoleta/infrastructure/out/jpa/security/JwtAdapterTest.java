package com.pragma.plazoleta.infrastructure.out.jpa.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class JwtAdapterTest {

    private JwtAdapter jwtAdapter;

    @BeforeEach
    void setUp() {
        jwtAdapter = new JwtAdapter();
    }

    @Test
    void shouldGenerateValidToken() {
        // arrange
        Long userId = 1L;
        String rol = "PROPIETARIO";

        // act
        String token = jwtAdapter.generateToken(userId, rol);

        // assert
        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void shouldValidateValidToken() {
        // arrange
        String token = jwtAdapter.generateToken(1L, "ADMIN");

        // act
        boolean isValid = jwtAdapter.validateToken(token);

        // assert
        assertTrue(isValid);
    }

    @Test
    void shouldReturnFalseWhenTokenIsInvalid() {
        // arrange
        String invalidToken = "this.is.not.a.valid.jwt";

        // act
        boolean isValid = jwtAdapter.validateToken(invalidToken);

        // assert
        assertFalse(isValid);
    }

    @Test
    void shouldExtractUserIdFromToken() {
        // arrange
        Long expectedUserId = 5L;
        String token = jwtAdapter.generateToken(expectedUserId, "EMPLEADO");

        // act
        Long userId = jwtAdapter.getUserId(token);

        // assert
        assertEquals(expectedUserId, userId);
    }

    @Test
    void shouldExtractRolFromToken() {
        // arrange
        String expectedRol = "CLIENTE";
        String token = jwtAdapter.generateToken(10L, expectedRol);

        // act
        String rol = jwtAdapter.getRol(token);

        // assert
        assertEquals(expectedRol, rol);
    }
}
