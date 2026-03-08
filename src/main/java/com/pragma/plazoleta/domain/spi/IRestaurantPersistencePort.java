package com.pragma.plazoleta.domain.spi;

import com.pragma.plazoleta.domain.model.PageResult;
import com.pragma.plazoleta.domain.model.Restaurant;

import java.util.Optional;

public interface IRestaurantPersistencePort {
    Restaurant saveRestaurant(Restaurant restaurant);
    Optional<Restaurant> findById(Long idRestaurant);
    PageResult<Restaurant> listRestaurants(int page, int size);
}
