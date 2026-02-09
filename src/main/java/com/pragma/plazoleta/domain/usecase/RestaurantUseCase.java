package com.pragma.plazoleta.domain.usecase;

import com.pragma.plazoleta.domain.api.IRestaurantServicePort;
import com.pragma.plazoleta.domain.exception.*;
import com.pragma.plazoleta.domain.spi.IUserPersistencePort;
import com.pragma.plazoleta.domain.model.Restaurant;
import com.pragma.plazoleta.domain.model.Rol;
import com.pragma.plazoleta.domain.spi.IEmployeeRestaurantPersistencePort;
import com.pragma.plazoleta.domain.spi.IRestaurantPersistencePort;

import java.util.List;

public class RestaurantUseCase implements IRestaurantServicePort {

    private final IRestaurantPersistencePort restaurantPersistencePort;

    public RestaurantUseCase(IRestaurantPersistencePort restaurantPersistencePort){
        this.restaurantPersistencePort = restaurantPersistencePort;
    }

    @Override
    public void saveRestaurant(Restaurant restaurant) {
        restaurantPersistencePort.saveRestaurant(restaurant);
    }

    @Override
    public List<Restaurant> listRestaurants(int page, int size) {
        return restaurantPersistencePort.listRestaurants(page, size);
    }
}
