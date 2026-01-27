package com.pragma.plazoleta.domain.usecase;

import com.pragma.plazoleta.domain.api.IDishServicePort;
import com.pragma.plazoleta.domain.exception.DishNotFoundException;
import com.pragma.plazoleta.domain.exception.RestaurantOwnershipException;
import com.pragma.plazoleta.domain.exception.UserNotRolException;
import com.pragma.plazoleta.domain.model.Dish;
import com.pragma.plazoleta.domain.model.Restaurant;
import com.pragma.plazoleta.domain.model.Rol;
import com.pragma.plazoleta.domain.spi.IDishPersistencePort;
import com.pragma.plazoleta.domain.spi.IRestaurantPersistencePort;
import com.pragma.plazoleta.domain.spi.IUserValidationPort;

public class DishUseCase implements IDishServicePort {

    private final IDishPersistencePort dishPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IUserValidationPort userValidationPort;

    public DishUseCase(IDishPersistencePort dishPersistencePort, IRestaurantPersistencePort restaurantPersistencePort, IUserValidationPort userValidationPort){
        this.dishPersistencePort = dishPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.userValidationPort = userValidationPort;
    }

    @Override
    public void saveDish(Dish dish) {
        Rol rol = userValidationPort.getUserRol(dish.getOwnerId());


        if (rol != Rol.PROPIETARIO){
            throw new UserNotRolException(dish.getOwnerId());
        }

        Restaurant restaurant = restaurantPersistencePort.findById(dish.getRestaurantId());
        if (!restaurant.getOwnerId().equals(dish.getOwnerId())) {
            throw new RestaurantOwnershipException(dish.getOwnerId(), dish.getRestaurantId());
        }
        dishPersistencePort.saveDish(dish);

    }

    @Override
    public void updateDish(Long dishId, Integer price, String description) {
        Dish dish = dishPersistencePort.findById(dishId)
                .orElseThrow(() -> new DishNotFoundException(dishId));
        dish.setPrice(price);
        dish.setDescription(description);
        dishPersistencePort.updateDish(dish);
    }
}
