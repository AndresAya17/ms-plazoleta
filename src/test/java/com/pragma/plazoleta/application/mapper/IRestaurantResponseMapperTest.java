package com.pragma.plazoleta.application.mapper;

import com.pragma.plazoleta.application.dto.response.DishResponseDto;
import com.pragma.plazoleta.application.dto.response.PageResponseDto;
import com.pragma.plazoleta.application.dto.response.RestaurantResponseDto;
import com.pragma.plazoleta.domain.model.Dish;
import com.pragma.plazoleta.domain.model.PageResult;
import com.pragma.plazoleta.domain.model.Restaurant;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class IRestaurantResponseMapperTest {

    private final IRestaurantResponseMapper mapper =
            Mappers.getMapper(IRestaurantResponseMapper.class);

    @Test
    void shouldMapRestaurantToResponseDto() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Restaurante Test");
        restaurant.setNit("123456789");
        restaurant.setAddress("Calle 123");
        restaurant.setPhoneNumber("+573001234567");
        restaurant.setLogoUrl("https://logo.com/logo.png");
        restaurant.setOwnerId(10L);

        RestaurantResponseDto response = mapper.toResponse(restaurant);

        assertNotNull(response);
        assertEquals(restaurant.getId(), response.getId());
        assertEquals(restaurant.getName(), response.getName());
        assertEquals(restaurant.getNit(), response.getNit());
        assertEquals(restaurant.getAddress(), response.getAddress());
        assertEquals(restaurant.getPhoneNumber(), response.getPhoneNumber());
        assertEquals(restaurant.getLogoUrl(), response.getLogoUrl());
        assertEquals(restaurant.getOwnerId(), response.getOwnerId());
    }

    @Test
    void shouldReturnNullWhenRestaurantIsNull() {
        assertNull(mapper.toResponse(null));
    }

    @Test
    void shouldMapPageResultToPageResponseDto() {
        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName("Pasta");
        dish.setPrice(15000);

        PageResult<Dish> pageResult = new PageResult<>(
                List.of(dish),
                0,
                1,
                1
        );

        PageResponseDto<DishResponseDto> responsePage =
                mapper.toResponsePage(pageResult);

        assertNotNull(responsePage);
        assertEquals(1, responsePage.getContent().size());
        assertEquals(1, responsePage.getTotalElements());
        assertEquals(1, responsePage.getTotalPages());
        assertEquals(0, responsePage.getPage());
        assertEquals(1, responsePage.getSize());
    }

}
