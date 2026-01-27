package com.pragma.plazoleta.infrastructure.exceptionhandler;

import com.pragma.plazoleta.domain.exception.DishNotFoundException;
import com.pragma.plazoleta.domain.exception.RestaurantOwnershipException;
import com.pragma.plazoleta.domain.exception.UserNotFoundException;
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

        @GetMapping("/user-not-owner")
        void userNotOwner() {
            throw new UserNotRolException(10L);
        }

        @GetMapping("/restaurant-ownership")
        void restaurantOwnership() {
            throw new RestaurantOwnershipException(5L, 10L);
        }

        @GetMapping("/user-not-found")
        void userNotFound() {
            throw new UserNotFoundException(99L);
        }

        @GetMapping("/dish-not-found")
        void dishNotFound() {
            throw new DishNotFoundException(20L);
        }
    }

    @Test
    void shouldReturn403WhenUserNotOwnerExceptionIsThrown() throws Exception {
        mockMvc.perform(get("/user-not-owner")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message")
                        .value(containsString("no tiene el rol permitido")));
    }

    @Test
    void shouldReturn403WhenRestaurantOwnershipExceptionIsThrown() throws Exception {
        mockMvc.perform(get("/restaurant-ownership")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message")
                        .value(containsString("no es propietario")));
    }

    @Test
    void shouldReturn404WhenUserNotFoundExceptionIsThrown() throws Exception {
        mockMvc.perform(get("/user-not-found")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value(containsString("not found")));
    }
    @Test
    void shouldReturn200WhenDishNotFoundExceptionIsThrown() throws Exception {
        mockMvc.perform(get("/dish-not-found")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value(containsString("not found")));
    }
}


