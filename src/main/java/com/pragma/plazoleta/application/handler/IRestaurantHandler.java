package com.pragma.plazoleta.application.handler;

import com.pragma.plazoleta.application.dto.request.RestaurantEmployeeRequestDto;
import com.pragma.plazoleta.application.dto.request.RestaurantRequestDto;
import com.pragma.plazoleta.application.dto.response.DishResponseDto;
import com.pragma.plazoleta.application.dto.response.PageResponseDto;
import com.pragma.plazoleta.application.dto.response.RestaurantListResponseDto;
import com.pragma.plazoleta.domain.model.DishCategory;

import java.util.List;

public interface IRestaurantHandler {
    void saveRestaurant(RestaurantRequestDto restaurantRequestDto, Long userId, String rol);
    void saveRestaurantEmployee(RestaurantEmployeeRequestDto restaurantEmployeeRequestDto, Long userId, String rol, Long restaurantId);
    List<RestaurantListResponseDto> listRestaurants(int page, int size, String rol);
    PageResponseDto<DishResponseDto> listDish(int page, int size, String rol, Long restaurantId, DishCategory category);
}
