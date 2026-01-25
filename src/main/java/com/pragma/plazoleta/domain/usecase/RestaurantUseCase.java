package com.pragma.plazoleta.domain.usecase;

import com.pragma.plazoleta.domain.api.IRestaurantServicePort;
import com.pragma.plazoleta.domain.model.Restaurant;
import com.pragma.plazoleta.domain.spi.IRestaurantPersistencePort;
import com.pragma.plazoleta.domain.spi.IUserOwnerValidationPort;

public class RestaurantUseCase implements IRestaurantServicePort {

    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IUserOwnerValidationPort userOwnerValidationPort;

    public RestaurantUseCase(IRestaurantPersistencePort restaurantPersistencePort, IUserOwnerValidationPort userOwnerValidationPort){
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.userOwnerValidationPort = userOwnerValidationPort;
    }

    @Override
    public void saveRestaurant(Restaurant restaurant) {
        if (!userOwnerValidationPort.isOwner(restaurant.getOwnerId())) {
            throw new RuntimeException("El usuario no es propietario");
        }
        restaurantPersistencePort.saveRestaurant(restaurant);
    }
}
