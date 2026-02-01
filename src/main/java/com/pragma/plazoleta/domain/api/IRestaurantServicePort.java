package com.pragma.plazoleta.domain.api;

import com.pragma.plazoleta.domain.model.EmployeeForRestaurantCommand;
import com.pragma.plazoleta.domain.model.Restaurant;

public interface IRestaurantServicePort {
    void saveRestaurant(Restaurant restaurant, Long userId, String rol);
    void saveEmployee(EmployeeForRestaurantCommand employee, String rol);
}
