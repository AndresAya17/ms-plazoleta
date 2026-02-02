package com.pragma.plazoleta.infrastructure.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.plazoleta.application.dto.request.RestaurantRequestDto;
import com.pragma.plazoleta.application.handler.IDishHandler;
import com.pragma.plazoleta.application.handler.IRestaurantHandler;
import com.pragma.plazoleta.domain.spi.IJwtPersistencePort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RestaurantRestController.class)
@AutoConfigureMockMvc(addFilters = false)
public class RestaurantRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IRestaurantHandler restaurantHandler;

    // 🔑 NECESARIO para que el JwtFilter no rompa el contexto
    @MockBean
    private IJwtPersistencePort jwtPersistencePort;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BASE_URL = "/api/v1/plazoleta/restaurant/";

    @Test
    void shouldReturn201WhenRestaurantIsCreated() throws Exception {
        // arrange – DTO COMPLETO y válido
        RestaurantRequestDto dto = new RestaurantRequestDto();
        dto.setName("Restaurante Test");
        dto.setNit("123456789");
        dto.setAddress("Calle 123");
        dto.setPhoneNumber("+573001234567");
        dto.setLogoUrl("https://logo.com/logo.png");
        dto.setOwnerId(1L); // 🔥 OBLIGATORIO

        Long userId = 1L;
        String rol = "OWNER";

        doNothing().when(restaurantHandler)
                .saveRestaurant(any(RestaurantRequestDto.class), anyLong(), anyString());

        // act & assert
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
        // arrange – DTO vacío → inválido
        RestaurantRequestDto dto = new RestaurantRequestDto();

        // act & assert
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(restaurantHandler);
    }

}
