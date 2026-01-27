package com.pragma.plazoleta.domain.usecase;

import com.pragma.plazoleta.domain.api.IRestaurantServicePort;
import com.pragma.plazoleta.domain.exception.UserNotRolException;
import com.pragma.plazoleta.domain.model.Restaurant;
import com.pragma.plazoleta.domain.model.Rol;
import com.pragma.plazoleta.domain.spi.IRestaurantPersistencePort;
import com.pragma.plazoleta.domain.spi.IUserValidationPort;

public class RestaurantUseCase implements IRestaurantServicePort {

    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IUserValidationPort userOwnerValidationPort;

    public RestaurantUseCase(IRestaurantPersistencePort restaurantPersistencePort, IUserValidationPort userOwnerValidationPort){
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.userOwnerValidationPort = userOwnerValidationPort;
    }

    @Override
    public void saveRestaurant(Restaurant restaurant) {
        Rol rol = userOwnerValidationPort.getUserRol(restaurant.getOwnerId());
        if (rol != Rol.ADMINISTRADOR){
            throw new UserNotRolException(restaurant.getOwnerId());
        }
        restaurantPersistencePort.saveRestaurant(restaurant);
    }
}
