package com.pragma.plazoleta.application.handler;


import com.pragma.plazoleta.application.dto.request.RestaurantEmployeeRequestDto;
import com.pragma.plazoleta.application.dto.request.RestaurantRequestDto;
import com.pragma.plazoleta.application.dto.response.RestaurantListResponseDto;
import com.pragma.plazoleta.application.handler.impl.RestaurantHandler;
import com.pragma.plazoleta.application.mapper.IEmployeeRestaurantRequestMapper;
import com.pragma.plazoleta.application.mapper.IRestaurantListResponseMapper;
import com.pragma.plazoleta.application.mapper.IRestaurantRequestMapper;
import com.pragma.plazoleta.domain.api.IRestaurantServicePort;
import com.pragma.plazoleta.domain.model.EmployeeForRestaurantCommand;
import com.pragma.plazoleta.domain.model.Restaurant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;


@ExtendWith(MockitoExtension.class)
class RestaurantHandlerTest {
    @Mock
    private IRestaurantServicePort restaurantServicePort;

    @Mock
    private IRestaurantRequestMapper restaurantRequestMapper;

    @Mock
    private IEmployeeRestaurantRequestMapper employeeRestaurantRequestMapper;

    @Mock
    private IRestaurantListResponseMapper restaurantListResponseMapper;

    @InjectMocks
    private RestaurantHandler restaurantHandler;

    @Test
    void shouldMapDtoAndCallServiceWithUserIdAndRol() {
        Long userId = 1L;
        String rol = "ADMINISTRADOR";

        RestaurantRequestDto dto = new RestaurantRequestDto();
        dto.setName("Restaurante Test");

        Restaurant restaurant = new Restaurant();
        restaurant.setName("Restaurante Test");

        when(restaurantRequestMapper.toRestaurant(dto)).thenReturn(restaurant);

        restaurantHandler.saveRestaurant(dto, userId, rol);

        verify(restaurantRequestMapper).toRestaurant(dto);
        verify(restaurantServicePort)
                .saveRestaurant(restaurant, userId, rol);
        verifyNoMoreInteractions(restaurantRequestMapper, restaurantServicePort);
    }

    @Test
    void shouldSaveRestaurantEmployee() {
        RestaurantEmployeeRequestDto requestDto = new RestaurantEmployeeRequestDto();
        Long userId = 1L;
        Long restaurantId = 10L;
        String rol = "PROPIETARIO";

        EmployeeForRestaurantCommand command =
                mock(EmployeeForRestaurantCommand.class);

        when(employeeRestaurantRequestMapper
                .toEmployee(requestDto, restaurantId, userId))
                .thenReturn(command);

        restaurantHandler.saveRestaurantEmployee(
                requestDto,
                userId,
                rol,
                restaurantId
        );

        verify(employeeRestaurantRequestMapper)
                .toEmployee(requestDto, restaurantId, userId);

        verify(restaurantServicePort)
                .saveEmployee(command, rol);

        verifyNoMoreInteractions(
                employeeRestaurantRequestMapper,
                restaurantServicePort
        );
    }

    @Test
    void shouldListRestaurantsAndMapToResponse() {
        int page = 0;
        int size = 10;
        String rol = "CLIENTE";

        Restaurant restaurant = new Restaurant();
        RestaurantListResponseDto responseDto = new RestaurantListResponseDto();
        responseDto.setName("Pollos Popeye");
        responseDto.setLogoUrl("https://logopoll");

        when(restaurantServicePort.listRestaurants(page, size, rol))
                .thenReturn(List.of(restaurant));

        when(restaurantListResponseMapper.toResponse(restaurant))
                .thenReturn(responseDto);

        List<RestaurantListResponseDto> result =
                restaurantHandler.listRestaurants(page, size, rol);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Pollos Popeye", result.get(0).getName());
        assertEquals("https://logopoll", result.get(0).getLogoUrl());

        verify(restaurantServicePort, times(1))
                .listRestaurants(page, size, rol);

        verify(restaurantListResponseMapper, times(1))
                .toResponse(restaurant);
    }
}
