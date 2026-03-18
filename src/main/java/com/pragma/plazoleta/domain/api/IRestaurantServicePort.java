package com.pragma.plazoleta.domain.api;

import com.pragma.plazoleta.domain.model.EmployeeRestaurant;
import com.pragma.plazoleta.domain.model.PageResult;
import com.pragma.plazoleta.domain.model.Restaurant;

import java.util.List;

public interface IRestaurantServicePort {
    void saveRestaurant(Restaurant restaurant);
    PageResult<Restaurant> listRestaurants(int page, int size);
    void validateOwner(Long restaurantId, Long userId);
    void assignEmployeeToRestaurant(EmployeeRestaurant employeeRestaurant);
    List<Long> getEmployeeRestaurant(Long restaurantId, Long userId);
}
