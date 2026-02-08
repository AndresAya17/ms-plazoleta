package com.pragma.plazoleta.application.handler;


import com.pragma.plazoleta.application.dto.request.RestaurantRequestDto;
import com.pragma.plazoleta.application.dto.response.DishResponseDto;
import com.pragma.plazoleta.application.dto.response.PageResponseDto;
import com.pragma.plazoleta.application.dto.response.RestaurantListResponseDto;
import com.pragma.plazoleta.application.handler.impl.RestaurantHandler;
import com.pragma.plazoleta.application.mapper.IEmployeeRestaurantRequestMapper;
import com.pragma.plazoleta.application.mapper.IRestaurantListResponseMapper;
import com.pragma.plazoleta.application.mapper.IRestaurantRequestMapper;
import com.pragma.plazoleta.application.mapper.IRestaurantResponseMapper;
import com.pragma.plazoleta.domain.api.IDishServicePort;
import com.pragma.plazoleta.domain.api.IRestaurantServicePort;
import com.pragma.plazoleta.domain.model.*;
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
    private IDishServicePort dishServicePort;

    @Mock
    private IRestaurantRequestMapper restaurantRequestMapper;

    @Mock
    private IEmployeeRestaurantRequestMapper employeeRestaurantRequestMapper;

    @Mock
    private IRestaurantListResponseMapper restaurantListResponseMapper;

    @Mock
    private IRestaurantResponseMapper restaurantResponseMapper;

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

    @Test
    void shouldListDishesByRestaurantAndMapResponse() {
        int page = 0;
        int size = 10;
        String rol = "CLIENTE";
        Long restaurantId = 1L;
        DishCategory category = DishCategory.STARTER;

        Dish dish = new Dish();

        PageResult<Dish> pageResult =
                new PageResult<>(
                        List.of(dish),
                        page,
                        size,
                        1L
                );

        DishResponseDto dishResponseDto =
                new DishResponseDto(
                        "Pizza",
                        25000,
                        "Pizza artesanal",
                        "https://image",
                        DishCategory.STARTER
                );

        PageResponseDto<DishResponseDto> responsePage =
                new PageResponseDto<>(
                        List.of(dishResponseDto),
                        page,
                        size,
                        1L,
                        1
                );

        when(dishServicePort.listDishesByRestaurant(
                restaurantId,
                category,
                page,
                size,
                rol
        )).thenReturn(pageResult);

        when(restaurantResponseMapper.toResponsePage(pageResult))
                .thenReturn(responsePage);

        // act
        PageResponseDto<DishResponseDto> result =
                restaurantHandler.listDish(
                        page,
                        size,
                        rol,
                        restaurantId,
                        category
                );

        // assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Pizza", result.getContent().get(0).getName());
        assertEquals(25000, result.getContent().get(0).getPrice());
        assertEquals(DishCategory.STARTER, result.getContent().get(0).getCategory());

        verify(dishServicePort, times(1))
                .listDishesByRestaurant(
                        restaurantId,
                        category,
                        page,
                        size,
                        rol
                );

        verify(restaurantResponseMapper, times(1))
                .toResponsePage(pageResult);

        verifyNoMoreInteractions(dishServicePort, restaurantResponseMapper);
    }

}
