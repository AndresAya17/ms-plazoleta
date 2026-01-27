package com.pragma.plazoleta.infrastructure.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.plazoleta.application.dto.request.DishRequestDto;
import com.pragma.plazoleta.application.dto.request.RestaurantRequestDto;
import com.pragma.plazoleta.application.handler.IDishHandler;
import com.pragma.plazoleta.application.handler.IRestaurantHandler;
import com.pragma.plazoleta.domain.model.DishCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RestaurantRestController.class)
public class RestaurantRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IRestaurantHandler restaurantHandler;

    @MockBean
    private IDishHandler dishHandler;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BASE_URL = "/api/v1/plazoleta";

    @Test
    void shouldReturn201WhenRestaurantIsCreated() throws Exception {
        RestaurantRequestDto dto = new RestaurantRequestDto();
        dto.setName("Restaurante Test");
        dto.setNit("123456789");
        dto.setAddress("Calle 123");
        dto.setPhoneNumber("+573001234567");
        dto.setLogoUrl("https://logo.com/logo.png");
        dto.setOwnerId(1L);

        doNothing().when(restaurantHandler)
                .saveRestaurant(any(RestaurantRequestDto.class));

        // act & assert
        mockMvc.perform(post(BASE_URL + "/restaurant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        verify(restaurantHandler, times(1))
                .saveRestaurant(any(RestaurantRequestDto.class));
    }

    @Test
    void shouldReturn400WhenRequestBodyIsInvalid() throws Exception {
        RestaurantRequestDto dto = new RestaurantRequestDto(); // vacío

        // act & assert
        mockMvc.perform(post(BASE_URL + "/restaurant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(restaurantHandler);
    }

}
