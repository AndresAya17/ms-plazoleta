package com.pragma.plazoleta.application.handler;


import com.pragma.plazoleta.application.dto.request.RestaurantRequestDto;
import com.pragma.plazoleta.application.dto.response.DishResponseDto;
import com.pragma.plazoleta.application.dto.response.PageResponseDto;
import com.pragma.plazoleta.application.dto.response.RestaurantListResponseDto;
import com.pragma.plazoleta.application.handler.impl.RestaurantHandler;
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
    private IRestaurantListResponseMapper restaurantListResponseMapper;

    @Mock
    private IRestaurantResponseMapper restaurantResponseMapper;

    @InjectMocks
    private RestaurantHandler restaurantHandler;

    @Test
    void shouldMapDtoAndCallServiceWithUserIdAndRol() {
        RestaurantRequestDto dto = new RestaurantRequestDto();
        dto.setName("Restaurante Test");

        Restaurant restaurant = new Restaurant();
        restaurant.setName("Restaurante Test");

        when(restaurantRequestMapper.toRestaurant(dto)).thenReturn(restaurant);

        restaurantHandler.saveRestaurant(dto);

        verify(restaurantRequestMapper).toRestaurant(dto);
        verify(restaurantServicePort)
                .saveRestaurant(restaurant);
        verifyNoMoreInteractions(restaurantRequestMapper, restaurantServicePort);
    }

    @Test
    void shouldListRestaurantsAndMapToResponse() {
        int page = 0;
        int size = 10;

        Restaurant restaurant = new Restaurant();
        RestaurantListResponseDto responseDto = new RestaurantListResponseDto();
        responseDto.setName("Pollos Popeye");
        responseDto.setLogoUrl("https://logopoll");

        when(restaurantServicePort.listRestaurants(page, size))
                .thenReturn(List.of(restaurant));

        when(restaurantListResponseMapper.toResponse(restaurant))
                .thenReturn(responseDto);

        List<RestaurantListResponseDto> result =
                restaurantHandler.listRestaurants(page, size);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Pollos Popeye", result.get(0).getName());
        assertEquals("https://logopoll", result.get(0).getLogoUrl());

        verify(restaurantServicePort, times(1))
                .listRestaurants(page, size);

        verify(restaurantListResponseMapper, times(1))
                .toResponse(restaurant);
    }

    @Test
    void shouldListDishesByRestaurantAndMapResponse() {
        int page = 0;
        int size = 10;
        Long categoryId = 1L;
        Long restaurantId = 1L;

        Dish dish = new Dish();

        PageResult<Dish> pageResult =
                new PageResult<>(
                        List.of(dish),
                        page,
                        size,
                        categoryId
                );

        DishResponseDto dishResponseDto =
                new DishResponseDto(
                        "Pizza",
                        25000,
                        "Pizza artesanal",
                        "https://image",
                        categoryId
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
                page,
                size,
                categoryId
        )).thenReturn(pageResult);

        when(restaurantResponseMapper.toResponsePage(pageResult))
                .thenReturn(responsePage);

        PageResponseDto<DishResponseDto> result =
                restaurantHandler.listDish(
                        page,
                        size,
                        restaurantId,
                        categoryId
                );
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Pizza", result.getContent().get(0).getName());
        assertEquals(25000, result.getContent().get(0).getPrice());
        assertEquals(categoryId, result.getContent().get(0).getCategoryId());

        verify(dishServicePort, times(1))
                .listDishesByRestaurant(
                        restaurantId,
                        page,
                        size,
                        categoryId
                );

        verify(restaurantResponseMapper, times(1))
                .toResponsePage(pageResult);

        verifyNoMoreInteractions(dishServicePort, restaurantResponseMapper);
    }

}
