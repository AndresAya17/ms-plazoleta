package com.pragma.plazoleta.infrastructure.exceptionhandler;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
public class ErrorResponseTest {
    @Test
    void shouldCreateErrorResponseWithMessage() {
        // arrange
        String expectedMessage = "Invalid request";

        // act
        ErrorResponse errorResponse = new ErrorResponse(expectedMessage);

        // assert
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getMessage()).isEqualTo(expectedMessage);
    }
}
