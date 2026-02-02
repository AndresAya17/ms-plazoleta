package com.pragma.plazoleta.infrastructure.exceptionhandler;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
class ErrorResponseTest {
    @Test
    void shouldCreateErrorResponseWithMessage() {
        String expectedErrorCode = "INVALID_DISH";
        String expectedMessage = "Invalid request";

        ErrorResponse errorResponse =
                new ErrorResponse(expectedErrorCode, expectedMessage);

        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getErrorCode()).isEqualTo(expectedErrorCode);
        assertThat(errorResponse.getMessage()).isEqualTo(expectedMessage);
    }
}
