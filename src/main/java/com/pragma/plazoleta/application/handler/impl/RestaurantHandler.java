package com.pragma.plazoleta.application.handler.impl;

import com.pragma.plazoleta.application.dto.request.RestaurantEmployeeRequestDto;
import com.pragma.plazoleta.application.dto.request.RestaurantRequestDto;
import com.pragma.plazoleta.application.dto.response.RestaurantListResponseDto;
import com.pragma.plazoleta.application.handler.IRestaurantHandler;
import com.pragma.plazoleta.application.mapper.IEmployeeRestaurantRequestMapper;
import com.pragma.plazoleta.application.mapper.IRestaurantListResponseMapper;
import com.pragma.plazoleta.application.mapper.IRestaurantRequestMapper;
import com.pragma.plazoleta.domain.api.IRestaurantServicePort;
import com.pragma.plazoleta.domain.model.EmployeeForRestaurantCommand;
import com.pragma.plazoleta.domain.model.Restaurant;
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
    private final IEmployeeRestaurantRequestMapper employeeRestaurantRequestMapper;
    private final IRestaurantListResponseMapper restaurantListResponseMapper;

    @Override
    public void saveRestaurant(RestaurantRequestDto restaurantRequestDto, Long userId, String rol) {
        Restaurant restaurant = restaurantRequestMapper.toRestaurant((restaurantRequestDto));
        restaurantServicePort.saveRestaurant(restaurant, userId, rol);
    }

    @Override
    public void saveRestaurantEmployee(RestaurantEmployeeRequestDto restaurantEmployeeRequestDto, Long userId, String rol, Long restaurantId) {
        EmployeeForRestaurantCommand employee = employeeRestaurantRequestMapper.toEmployee(restaurantEmployeeRequestDto,restaurantId,userId);
        restaurantServicePort.saveEmployee(employee, rol);
    }

    @Override
    public List<RestaurantListResponseDto> listRestaurants(int page, int size, String rol) {
        List<Restaurant> restaurants =
                restaurantServicePort.listRestaurants(page, size, rol);

        return restaurants.stream()
                .map(restaurantListResponseMapper::toResponse)
                .toList();
    }

}
