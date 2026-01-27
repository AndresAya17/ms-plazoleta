package com.pragma.plazoleta.domain.usecase;

import com.pragma.plazoleta.domain.api.IDishServicePort;
import com.pragma.plazoleta.domain.exception.DishNotFoundException;
import com.pragma.plazoleta.domain.exception.RestaurantOwnershipException;
import com.pragma.plazoleta.domain.exception.UserNotOwnerException;
import com.pragma.plazoleta.domain.model.Dish;
import com.pragma.plazoleta.domain.model.Restaurant;
import com.pragma.plazoleta.domain.spi.IDishPersistencePort;
import com.pragma.plazoleta.domain.spi.IRestaurantPersistencePort;
import com.pragma.plazoleta.domain.spi.IUserOwnerValidationPort;

public class DishUseCase implements IDishServicePort {

    private final IDishPersistencePort dishPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IUserOwnerValidationPort userOwnerValidationPort;

    public DishUseCase(IDishPersistencePort dishPersistencePort, IRestaurantPersistencePort restaurantPersistencePort, IUserOwnerValidationPort userOwnerValidationPort){
        this.dishPersistencePort = dishPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.userOwnerValidationPort = userOwnerValidationPort;
    }

    @Override
    public void saveDish(Dish dish) {
        if (!userOwnerValidationPort.isOwner(dish.getOwnerId())) {
            throw new UserNotOwnerException(dish.getOwnerId());
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
