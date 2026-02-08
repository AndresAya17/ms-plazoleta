package com.pragma.plazoleta.infrastructure.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.plazoleta.application.dto.request.RestaurantRequestDto;
import com.pragma.plazoleta.application.dto.response.RestaurantListResponseDto;
import com.pragma.plazoleta.application.handler.IRestaurantHandler;
import com.pragma.plazoleta.domain.spi.IJwtPersistencePort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(RestaurantRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class RestaurantRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IRestaurantHandler restaurantHandler;

    @MockBean
    private IJwtPersistencePort jwtPersistencePort;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BASE_URL = "/api/v1/plazoleta/restaurant/";

    @Test
    void shouldReturn201WhenRestaurantIsCreated() throws Exception {
        RestaurantRequestDto dto = new RestaurantRequestDto();
        dto.setName("Restaurante Test");
        dto.setNit("123456789");
        dto.setAddress("Calle 123");
        dto.setPhoneNumber("+573001234567");
        dto.setLogoUrl("https://logo.com/logo.png");
        dto.setOwnerId(1L);

        Long userId = 1L;
        String rol = "OWNER";

        doNothing().when(restaurantHandler)
                .saveRestaurant(any(RestaurantRequestDto.class), anyLong(), anyString());


        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("auth.userId", userId)
                        .requestAttr("auth.rol", rol)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        verify(restaurantHandler).saveRestaurant(
                any(RestaurantRequestDto.class),
                eq(userId),
                eq(rol)
        );
    }

    @Test
    void shouldReturn400WhenRequestBodyIsInvalid() throws Exception {
        RestaurantRequestDto dto = new RestaurantRequestDto();

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(restaurantHandler);
    }

    @Test
    void shouldSaveRestaurantEmployee() throws Exception {
        // arrange
        Long restaurantId = 10L;
        Long userId = 1L;
        String rol = "PROPIETARIO";

        RestaurantEmployeeRequestDto requestDto = new RestaurantEmployeeRequestDto();
        requestDto.setFirstName("Juan");
        requestDto.setLastName("Perez");
        requestDto.setEmail("juan@mail.com");
        requestDto.setPhoneNumber("3001234567");
        requestDto.setDocumentNumber("123456");
        requestDto.setPassword("password");

        // act
        mockMvc.perform(
                        post("/api/v1/plazoleta/restaurant/{id}/employees", restaurantId)
                                .requestAttr("auth.userId", userId)
                                .requestAttr("auth.rol", rol)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))
                )
                .andExpect(status().isCreated());

        // assert
        verify(restaurantHandler)
                .saveRestaurantEmployee(
                        any(RestaurantEmployeeRequestDto.class),
                        eq(userId),
                        eq(rol),
                        eq(restaurantId)
                );
    }

    @Test
    void shouldListRestaurants() throws Exception {
        // arrange
        Long userId = 2L;
        String rol = "CLIENTE";
        int page = 0;
        int size = 10;

        RestaurantListResponseDto dto = new RestaurantListResponseDto();
        dto.setName("Pollos Popeye");
        dto.setLogoUrl("https://logopoll");

        when(restaurantHandler.listRestaurants(page, size, rol))
                .thenReturn(List.of(dto));

        // act
        mockMvc.perform(
                        get("/api/v1/plazoleta/restaurant/restaurants")
                                .param("page", String.valueOf(page))
                                .param("size", String.valueOf(size))
                                .requestAttr("auth.userId", userId)
                                .requestAttr("auth.rol", rol)
                )
                .andExpect(status().isOk());

        // assert
        verify(restaurantHandler)
                .listRestaurants(page, size, rol);
    }

}
