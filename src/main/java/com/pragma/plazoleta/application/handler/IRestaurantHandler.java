package com.pragma.plazoleta.application.handler;

import com.pragma.plazoleta.application.dto.request.CreateEmployeeRestaurantRequestDto;
import com.pragma.plazoleta.application.dto.request.RestaurantRequestDto;
import com.pragma.plazoleta.application.dto.response.DishResponseDto;
import com.pragma.plazoleta.application.dto.response.PageResponseDto;
import com.pragma.plazoleta.application.dto.response.RestaurantListResponseDto;

import java.util.List;

public interface IRestaurantHandler {
    void saveRestaurant(RestaurantRequestDto restaurantRequestDto);
    PageResponseDto<RestaurantListResponseDto> listRestaurants(int page, int size);
    PageResponseDto<DishResponseDto> listDish(int page, int size, Long restaurantId, Long categoryId);
    void validateOwner(Long restaurantId, Long userId);
    void assignEmployeeToRestaurant(CreateEmployeeRestaurantRequestDto employeeRestaurantRequestDto);
    List<Long> getEmployeeRestaurant(Long restaurantId, Long userId);
}
