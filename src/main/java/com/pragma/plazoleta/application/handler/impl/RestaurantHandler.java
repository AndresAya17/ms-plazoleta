package com.pragma.plazoleta.application.handler.impl;

import com.pragma.plazoleta.application.dto.request.RestaurantRequestDto;
import com.pragma.plazoleta.application.dto.response.DishResponseDto;
import com.pragma.plazoleta.application.dto.response.PageResponseDto;
import com.pragma.plazoleta.application.dto.response.RestaurantListResponseDto;
import com.pragma.plazoleta.application.handler.IRestaurantHandler;
import com.pragma.plazoleta.application.mapper.IEmployeeRestaurantRequestMapper;
import com.pragma.plazoleta.application.mapper.IRestaurantListResponseMapper;
import com.pragma.plazoleta.application.mapper.IRestaurantRequestMapper;
import com.pragma.plazoleta.application.mapper.IRestaurantResponseMapper;
import com.pragma.plazoleta.domain.api.IDishServicePort;
import com.pragma.plazoleta.domain.api.IRestaurantServicePort;
import com.pragma.plazoleta.domain.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantHandler implements IRestaurantHandler {
    private final IRestaurantServicePort restaurantServicePort;
    private final IRestaurantRequestMapper restaurantRequestMapper;
    private final IRestaurantResponseMapper restaurantResponseMapper;
    private final IEmployeeRestaurantRequestMapper employeeRestaurantRequestMapper;
    private final IRestaurantListResponseMapper restaurantListResponseMapper;
    private final IDishServicePort dishServicePort;

    @Override
    public void saveRestaurant(RestaurantRequestDto restaurantRequestDto, Long userId, String rol) {
        Restaurant restaurant = restaurantRequestMapper.toRestaurant((restaurantRequestDto));
        restaurantServicePort.saveRestaurant(restaurant, userId, rol);
    }

    @Override
    public List<RestaurantListResponseDto> listRestaurants(int page, int size, String rol) {
        List<Restaurant> restaurants =
                restaurantServicePort.listRestaurants(page, size, rol);

        return restaurants.stream()
                .map(restaurantListResponseMapper::toResponse)
                .toList();
    }

    @Override
    public PageResponseDto<DishResponseDto> listDish(int page, int size, String rol, Long restaurantId, Long categoryId) {
        PageResult<Dish> result =
                dishServicePort.listDishesByRestaurant(
                        restaurantId, page, size, rol, categoryId);
        return restaurantResponseMapper.toResponsePage(result);
    }

}
