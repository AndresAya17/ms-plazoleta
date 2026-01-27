package com.pragma.plazoleta.infrastructure.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.plazoleta.application.dto.request.DishRequestDto;
import com.pragma.plazoleta.application.dto.request.UpdateDishRequestDto;
import com.pragma.plazoleta.application.handler.IDishHandler;
import com.pragma.plazoleta.application.handler.IRestaurantHandler;
import com.pragma.plazoleta.domain.model.DishCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DishRestController.class)
public class DishRestControllerTest {
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
    void shouldReturn201WhenDishIsCreated() throws Exception {
        DishRequestDto dto = new DishRequestDto();
        dto.setName("Pasta");
        dto.setPrice(25000);
        dto.setDescription("Pasta artesanal");
        dto.setImageUrl("https://img.com/pasta.png");

        dto.setCategory(DishCategory.MAIN_COURSE);
        dto.setRestaurantId(1L);
        dto.setOwnerId(10L);

        doNothing().when(dishHandler)
                .saveDish(any(DishRequestDto.class));

        mockMvc.perform(post("/api/v1/plazoleta/dish")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        verify(dishHandler).saveDish(any(DishRequestDto.class));
    }

    @Test
    void shouldReturn400WhenDishRequestIsInvalid() throws Exception {
        DishRequestDto dto = new DishRequestDto();

        mockMvc.perform(post(BASE_URL + "/dish")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(dishHandler);
    }

    @Test
    void shouldReturn200WhenDishIsUpdatedSuccessfully() throws Exception {
        // arrange
        UpdateDishRequestDto dto = new UpdateDishRequestDto();
        dto.setDishId(1L);
        dto.setPrice(30000);
        dto.setDescription("Updated description");

        doNothing()
                .when(dishHandler)
                .updateDish(any(UpdateDishRequestDto.class));

        // act & assert
        mockMvc.perform(patch("/api/v1/plazoleta/dish")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(dishHandler, times(1))
                .updateDish(any(UpdateDishRequestDto.class));
    }


}
