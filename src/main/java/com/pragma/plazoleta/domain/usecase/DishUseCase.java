package com.pragma.plazoleta.domain.usecase;

import com.pragma.plazoleta.domain.api.IDishServicePort;
import com.pragma.plazoleta.domain.exception.*;
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
            throw new DomainException(ErrorCode.UNAUTHORIZED, "Only a restaurant owner can create dishes");
        }

        Restaurant restaurant = restaurantPersistencePort.findById(dish.getRestaurantId())
                .orElseThrow(() -> new DomainException(ErrorCode.DATA_NOT_FOUND, "Restaurant not found"));
        if (!restaurant.getOwnerId().equals(userId)) {
            throw new DomainException(ErrorCode.FORBIDDEN, "You are not allowed to create dishes for this restaurant");
        }
        dishPersistencePort.saveDish(dish);

    }

    @Override
    public void updateDish(Long restaurantId, Long dishId, Integer price, String description, Long userId, String rol) {
        if (!Rol.PROPIETARIO.name().equals(rol)){
            throw new DomainException(ErrorCode.UNAUTHORIZED, "Only a restaurant owner can create dishes");
        }
        Restaurant restaurant = restaurantPersistencePort.findById(restaurantId)
                .orElseThrow(() -> new DomainException(ErrorCode.DATA_NOT_FOUND, "Restaurant not found"));
        if (!restaurant.getOwnerId().equals(userId)) {
            throw new DomainException(ErrorCode.FORBIDDEN, "You are not allowed to create dishes for this restaurant");
        }

        Dish dish = dishPersistencePort.findById(dishId)
                .orElseThrow(() -> new DomainException(ErrorCode.DATA_NOT_FOUND, "Dish not found"));
        dish.setPrice(price);
        dish.setDescription(description);
        dish.validateForUpdate();
        dishPersistencePort.saveDish(dish);
    }
}
