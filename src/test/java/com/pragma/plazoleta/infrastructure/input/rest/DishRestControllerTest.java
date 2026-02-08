package com.pragma.plazoleta.infrastructure.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.plazoleta.application.dto.request.DishRequestDto;
import com.pragma.plazoleta.application.dto.request.UpdateDishRequestDto;
import com.pragma.plazoleta.application.dto.request.UpdateDishStatusRequestDto;
import com.pragma.plazoleta.application.handler.IDishHandler;
import com.pragma.plazoleta.domain.spi.IJwtPersistencePort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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
@AutoConfigureMockMvc(addFilters = false)
class DishRestControllerTest {

    private static final String DISH_URL = "/api/v1/plazoleta/dish/";

    @MockBean
    private IJwtPersistencePort jwtPersistencePort;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IDishHandler dishHandler;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void shouldReturn201WhenDishIsCreated() throws Exception {
        DishRequestDto dto = new DishRequestDto();
        dto.setName("Pasta");
        dto.setPrice(25000);
        dto.setDescription("Pasta artesanal");
        dto.setImageUrl("https://img.com/pasta.png");
        dto.setCategory(DishCategory.MAIN_COURSE);
        dto.setRestaurantId(1L);

        Long userId = 10L;
        String rol = "OWNER";

        doNothing().when(dishHandler)
                .saveDish(any(DishRequestDto.class), anyLong(), anyString());

        mockMvc.perform(post(DISH_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("auth.userId", userId)
                        .requestAttr("auth.rol", rol)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        verify(dishHandler).saveDish(
                any(DishRequestDto.class),
                eq(userId),
                eq(rol)
        );
    }

    @Test
    void shouldReturn400WhenDishRequestIsInvalid() throws Exception {
        DishRequestDto dto = new DishRequestDto();

        mockMvc.perform(post(DISH_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("auth.userId", 1L)
                        .requestAttr("auth.rol", "OWNER")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(dishHandler);
    }

    @Test
    void shouldReturn200WhenDishIsUpdatedSuccessfully() throws Exception {
        UpdateDishRequestDto dto = new UpdateDishRequestDto();
        dto.setDishId(1L);
        dto.setPrice(30000);
        dto.setDescription("Updated description");
        dto.setRestaurantId(1L);

        Long userId = 10L;
        String rol = "OWNER";

        doNothing().when(dishHandler)
                .updateDish(any(UpdateDishRequestDto.class), anyLong(), anyString());

        // act & assert
        mockMvc.perform(patch("/api/v1/plazoleta/dish/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("auth.userId", userId)
                        .requestAttr("auth.rol", rol)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(dishHandler).updateDish(
                any(UpdateDishRequestDto.class),
                eq(userId),
                eq(rol)
        );
    }
    @Test
    void shouldUpdateDishStatus() throws Exception {
        Long dishId = 5L;
        Long userId = 1L;
        String rol = "PROPIETARIO";

        UpdateDishStatusRequestDto requestDto = new UpdateDishStatusRequestDto();
        requestDto.setActive(true);

        mockMvc.perform(
                        patch("/api/v1/plazoleta/dish/{id}/status", dishId)
                                .requestAttr("auth.userId", userId)
                                .requestAttr("auth.rol", rol)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))
                )
                .andExpect(status().isOk());

        verify(dishHandler)
                .updateDishStatus(
                        any(UpdateDishStatusRequestDto.class),
                        eq(userId),
                        eq(rol),
                        eq(dishId)
                );
    }


}
