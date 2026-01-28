package com.pragma.plazoleta.infrastructure.exceptionhandler;

import com.pragma.plazoleta.domain.exception.DataNotFoundException;
import com.pragma.plazoleta.domain.exception.RestaurantOwnershipException;
import com.pragma.plazoleta.domain.exception.UserNotRolException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GlobalExceptionHandlerTest {

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

        @GetMapping("/user-not-rol")
        void userNotRol() {
            throw new UserNotRolException();
        }

        @GetMapping("/restaurant-ownership")
        void restaurantOwnership() {
            throw new RestaurantOwnershipException();
        }

        @GetMapping("/user-not-found")
        void userNotFound() {
            throw new DataNotFoundException("User");
        }

        @GetMapping("/dish-not-found")
        void dishNotFound() {
            throw new DataNotFoundException("Dish");
        }
    }

    @Test
    void shouldReturn403WhenUserNotRolExceptionIsThrown() throws Exception {
        mockMvc.perform(get("/user-not-rol"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message")
                        .value(containsString("rol permitido")));
    }

    @Test
    void shouldReturn403WhenRestaurantOwnershipExceptionIsThrown() throws Exception {
        mockMvc.perform(get("/restaurant-ownership"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message")
                        .value(containsString("not the owner")));
    }

    @Test
    void shouldReturn404WhenUserNotFoundExceptionIsThrown() throws Exception {
        mockMvc.perform(get("/user-not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value(containsString("User not found")));
    }

    @Test
    void shouldReturn404WhenDishNotFoundExceptionIsThrown() throws Exception {
        mockMvc.perform(get("/dish-not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value(containsString("Dish not found")));
    }
}


