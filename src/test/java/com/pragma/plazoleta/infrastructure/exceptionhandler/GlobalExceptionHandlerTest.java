package com.pragma.plazoleta.infrastructure.exceptionhandler;

import com.pragma.plazoleta.domain.exception.DomainException;
import com.pragma.plazoleta.domain.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new TestController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }


    @RestController
    static class TestController {

        @GetMapping("/unauthorized")
        void unauthorized() {
            throw new DomainException(
                    ErrorCode.UNAUTHORIZED,
                    "Unauthorized action"
            );
        }

        @GetMapping("/forbidden")
        void forbidden() {
            throw new DomainException(
                    ErrorCode.FORBIDDEN,
                    "Forbidden action"
            );
        }

        @GetMapping("/not-found")
        void notFound() {
            throw new DomainException(
                    ErrorCode.DATA_NOT_FOUND,
                    "Resource not found"
            );
        }

        @GetMapping("/bad-request")
        void badRequest() {
            throw new DomainException(
                    ErrorCode.INVALID_RESTAURANT,
                    "Invalid restaurant data"
            );
        }
    }

    @Test
    void shouldReturn401WhenErrorCodeIsUnauthorized() throws Exception {
        mockMvc.perform(get("/unauthorized"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorCode").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.message").value("Unauthorized action"));
    }

    @Test
    void shouldReturn403WhenErrorCodeIsForbidden() throws Exception {
        mockMvc.perform(get("/forbidden"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errorCode").value("FORBIDDEN"))
                .andExpect(jsonPath("$.message").value("Forbidden action"));
    }

    @Test
    void shouldReturn404WhenErrorCodeIsDataNotFound() throws Exception {
        mockMvc.perform(get("/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("DATA_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Resource not found"));
    }

    @Test
    void shouldReturn400WhenErrorCodeIsInvalidRestaurant() throws Exception {
        mockMvc.perform(get("/bad-request"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("INVALID_RESTAURANT"))
                .andExpect(jsonPath("$.message").value("Invalid restaurant data"));
    }
}


