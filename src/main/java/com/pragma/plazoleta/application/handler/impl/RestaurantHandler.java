package com.pragma.plazoleta.application.handler.impl;

import com.pragma.plazoleta.application.dto.request.CreateEmployeeRestaurantRequestDto;
import com.pragma.plazoleta.application.dto.request.RestaurantRequestDto;
import com.pragma.plazoleta.application.dto.response.DishResponseDto;
import com.pragma.plazoleta.application.dto.response.PageResponseDto;
import com.pragma.plazoleta.application.dto.response.RestaurantListResponseDto;
import com.pragma.plazoleta.application.handler.IRestaurantHandler;
import com.pragma.plazoleta.application.mapper.IEmployeeRestaurantMapper;
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
    private final IDishServicePort dishServicePort;
    private final IEmployeeRestaurantMapper employeeRestaurantMapper;

    @Override
    public void saveRestaurant(RestaurantRequestDto restaurantRequestDto) {
        Restaurant restaurant = restaurantRequestMapper.toRestaurant((restaurantRequestDto));
        restaurantServicePort.saveRestaurant(restaurant);
    }

    @Override
    public PageResponseDto<RestaurantListResponseDto> listRestaurants(int page, int size) {
        PageResult<Restaurant> restaurants =
                restaurantServicePort.listRestaurants(page, size);
        return restaurantResponseMapper.RestaurantToResponsePage(restaurants);
    }

    @Override
    public PageResponseDto<DishResponseDto> listDish(int page, int size, Long restaurantId, Long categoryId) {
        PageResult<Dish> result =
                dishServicePort.listDishesByRestaurant(
                        restaurantId, page, size, categoryId);
        return restaurantResponseMapper.toResponsePage(result);
    }

    @Override
    public void validateOwner(Long restaurantId, Long userId) {
        restaurantServicePort.validateOwner(restaurantId, userId);
    }

    @Override
    public void assignEmployeeToRestaurant(CreateEmployeeRestaurantRequestDto employeeRestaurantRequestDto) {
        EmployeeRestaurant employeeRestaurant = employeeRestaurantMapper.toEmployee(employeeRestaurantRequestDto);
        restaurantServicePort.assignEmployeeToRestaurant(employeeRestaurant);
    }

    @Override
    public List<Long> getEmployeeRestaurant(Long restaurantId, Long userId) {
        return restaurantServicePort.getEmployeeRestaurant(restaurantId, userId);
    }


}
