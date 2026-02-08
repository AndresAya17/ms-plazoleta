package com.pragma.plazoleta.domain.usecase;

import com.pragma.plazoleta.domain.api.IDishServicePort;
import com.pragma.plazoleta.domain.exception.*;
import com.pragma.plazoleta.domain.model.*;
import com.pragma.plazoleta.domain.spi.IDishPersistencePort;
import com.pragma.plazoleta.domain.spi.IRestaurantPersistencePort;

public class DishUseCase implements IDishServicePort {

    private final IDishPersistencePort dishPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private static final String FOUNDATION = "Restaurant not found";

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
                .orElseThrow(() -> new DomainException(ErrorCode.DATA_NOT_FOUND, FOUNDATION));
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
                .orElseThrow(() -> new DomainException(ErrorCode.DATA_NOT_FOUND, FOUNDATION));
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

    @Override
    public void updateDishStatus(Boolean active, Long userId, String rol, Long dishId) {
        Dish dish = dishPersistencePort.findById(dishId)
                .orElseThrow(() -> new DomainException(ErrorCode.DATA_NOT_FOUND, "Dish not found"));

        Restaurant restaurant = restaurantPersistencePort.findById(dish.getRestaurantId())
                .orElseThrow(() -> new DomainException(ErrorCode.DATA_NOT_FOUND, FOUNDATION));
        if (!restaurant.getOwnerId().equals(userId)) {
            throw new DomainException(
                    ErrorCode.FORBIDDEN,
                    "You are not allowed to modify dishes of this restaurant"
            );
        }
        dish.setActive(active);
        dishPersistencePort.saveDish(dish);
    }

    @Override
    public PageResult<Dish> listDishesByRestaurant(Long restaurantId, int page, int size, String rol, Long categoryId) {
        if (!rol.equals(Rol.CLIENTE.name())) {
            throw new DomainException(ErrorCode.UNAUTHORIZED, "Only clients can list restaurants");
        }
        restaurantPersistencePort.findById(restaurantId)
                .orElseThrow(() -> new DomainException(ErrorCode.DATA_NOT_FOUND, FOUNDATION));

        return dishPersistencePort.findByRestaurant(restaurantId, page, size, categoryId);
    }
}
