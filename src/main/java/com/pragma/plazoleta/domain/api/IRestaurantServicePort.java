package com.pragma.plazoleta.domain.api;

import com.pragma.plazoleta.domain.model.Restaurant;

import java.util.List;

public interface IRestaurantServicePort {
    void saveRestaurant(Restaurant restaurant, Long userId, String rol);
    List<Restaurant> listRestaurants(int page, int size, String rol);
}
