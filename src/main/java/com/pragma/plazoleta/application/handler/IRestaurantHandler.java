package com.pragma.plazoleta.application.handler;

import com.pragma.plazoleta.application.dto.request.RestaurantEmployeeRequestDto;
import com.pragma.plazoleta.application.dto.request.RestaurantRequestDto;

public interface IRestaurantHandler {
    void saveRestaurant(RestaurantRequestDto restaurantRequestDto, Long userId, String rol);
    void saveRestaurantEmployee(RestaurantEmployeeRequestDto restaurantEmployeeRequestDto, Long userId, String rol, Long restaurantId);
}
