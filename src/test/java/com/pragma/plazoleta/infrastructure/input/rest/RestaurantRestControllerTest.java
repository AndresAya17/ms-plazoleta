package com.pragma.plazoleta.infrastructure.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.plazoleta.application.dto.request.RestaurantRequestDto;
import com.pragma.plazoleta.application.dto.response.DishResponseDto;
import com.pragma.plazoleta.application.dto.response.PageResponseDto;
import com.pragma.plazoleta.application.dto.response.RestaurantListResponseDto;
import com.pragma.plazoleta.application.handler.IRestaurantHandler;
import com.pragma.plazoleta.domain.spi.IJwtPersistencePort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(RestaurantRestController.class)
class RestaurantRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IRestaurantHandler restaurantHandler;

    @MockBean
    private IJwtPersistencePort jwtPersistencePort;

    private static final String BASE_URL = "/api/v1/plazoleta/restaurant";

    @Test
    @WithMockUser(authorities = "ADMIN")
    void shouldReturn201WhenRestaurantIsCreated() throws Exception {
        RestaurantRequestDto requestDto = new RestaurantRequestDto();
        requestDto.setName("Restaurante Test");
        requestDto.setNit("123456789");
        requestDto.setAddress("Calle 123");
        requestDto.setPhoneNumber("+573001234567");
        requestDto.setLogoUrl("https://logo.com/logo.png");
        requestDto.setOwnerId(1L);

        doNothing().when(restaurantHandler)
                .saveRestaurant(any(RestaurantRequestDto.class));

        mockMvc.perform(
                        post(BASE_URL + "/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))
                )
                .andExpect(status().isCreated());

        verify(restaurantHandler)
                .saveRestaurant(any(RestaurantRequestDto.class));
    }

    @Test
    void shouldListRestaurantsSuccessfully() throws Exception {
        int page = 0;
        int size = 10;

        RestaurantListResponseDto dto = new RestaurantListResponseDto();
        dto.setName("Restaurante Test");
        dto.setLogoUrl("https://logo.com/logo.png");

        when(restaurantHandler.listRestaurants(page, size))
                .thenReturn(List.of(dto));

        mockMvc.perform(
                        get(BASE_URL + "/restaurants")
                                .param("page", String.valueOf(page))
                                .param("size", String.valueOf(size))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        verify(restaurantHandler).listRestaurants(page, size);
        verifyNoMoreInteractions(restaurantHandler);
    }

    @Test
    void shouldListDishesByRestaurantWithoutCategory() throws Exception {
        Long restaurantId = 1L;
        int page = 0;
        int size = 10;

        DishResponseDto dish = new DishResponseDto(
                "Pasta",
                12000,
                "Pasta artesanal",
                "https://img.com/pasta.png",
                1L
        );

        PageResponseDto<DishResponseDto> response =
                new PageResponseDto<>(
                        List.of(dish),
                        page,
                        size,
                        1L,
                        1
                );

        when(restaurantHandler.listDish(page, size, restaurantId, null))
                .thenReturn(response);

        mockMvc.perform(
                        get(BASE_URL + "/" + restaurantId + "/dishes")
                                .param("page", String.valueOf(page))
                                .param("size", String.valueOf(size))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        verify(restaurantHandler)
                .listDish(page, size, restaurantId, null);
        verifyNoMoreInteractions(restaurantHandler);
    }
}
