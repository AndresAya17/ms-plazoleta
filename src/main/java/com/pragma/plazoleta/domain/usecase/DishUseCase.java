package com.pragma.plazoleta.domain.usecase;

import com.pragma.plazoleta.domain.api.IDishServicePort;
import com.pragma.plazoleta.domain.exception.DataNotFoundException;
import com.pragma.plazoleta.domain.exception.RestaurantOwnershipException;
import com.pragma.plazoleta.domain.exception.UserNotRolException;
import com.pragma.plazoleta.domain.model.Dish;
import com.pragma.plazoleta.domain.model.Restaurant;
import com.pragma.plazoleta.domain.model.Rol;
import com.pragma.plazoleta.domain.spi.IDishPersistencePort;
import com.pragma.plazoleta.domain.spi.IRestaurantPersistencePort;

public class DishUseCase implements IDishServicePort {

    private final IDishPersistencePort dishPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;

    public DishUseCase(IDishPersistencePort dishPersistencePort, IRestaurantPersistencePort restaurantPersistencePort){
        this.dishPersistencePort = dishPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
    }

    @Override
    public void saveDish(Dish dish, Long userId, String rol) {

        dish.validate();
        if (!Rol.PROPIETARIO.name().equals(rol)){
            throw new UserNotRolException();
        }

        Restaurant restaurant = restaurantPersistencePort.findById(dish.getRestaurantId());
        if (!restaurant.getOwnerId().equals(userId)) {
            throw new RestaurantOwnershipException();
        }
        dishPersistencePort.saveDish(dish);

    }

    @Override
    public void updateDish(Long restaurantId, Long dishId, Integer price, String description, Long userId, String rol) {
        if (!Rol.PROPIETARIO.name().equals(rol)){
            throw new UserNotRolException();
        }
        Restaurant restaurant = restaurantPersistencePort.findById(restaurantId);
        if (!restaurant.getOwnerId().equals(userId)) {
            throw new RestaurantOwnershipException();
        }


        Dish dish = dishPersistencePort.findById(dishId)
                .orElseThrow(() -> new DataNotFoundException("Dish"));
        dish.setPrice(price);
        dish.setDescription(description);
        dish.validateForUpdate();
        dishPersistencePort.saveDish(dish);
    }
}
