package com.pragma.plazoleta.domain.usecase;

import com.pragma.plazoleta.domain.api.IDishServicePort;
import com.pragma.plazoleta.domain.exception.*;
import com.pragma.plazoleta.domain.model.*;
import com.pragma.plazoleta.domain.spi.IDishPersistencePort;
import com.pragma.plazoleta.domain.spi.IRestaurantPersistencePort;
import com.pragma.plazoleta.domain.validator.DishDomainValidator;

public class DishUseCase implements IDishServicePort {

    private final IDishPersistencePort dishPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private static final String FOUNDATION = "Restaurant not found";

    public DishUseCase(IDishPersistencePort dishPersistencePort, IRestaurantPersistencePort restaurantPersistencePort){
        this.dishPersistencePort = dishPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
    }

    @Override
    public void saveDish(Dish dish, Long userId) {
        DishDomainValidator.validate(dish);
        Restaurant restaurant = restaurantPersistencePort.findById(dish.getRestaurantId())
                .orElseThrow(() -> new DomainException(ErrorCode.DATA_NOT_FOUND, FOUNDATION));
        if (!restaurant.getOwnerId().equals(userId)) {
            throw new DomainException(ErrorCode.FORBIDDEN, "You are not allowed to create dishes for this restaurant");
        }
        dish.setActive(true);
        dishPersistencePort.saveDish(dish);

    }

    @Override
    public void updateDish(Long restaurantId, Long dishId, Integer price, String description, Long userId) {
        Restaurant restaurant = restaurantPersistencePort.findById(restaurantId)
                .orElseThrow(() -> new DomainException(ErrorCode.DATA_NOT_FOUND, FOUNDATION));
        if (!restaurant.getOwnerId().equals(userId)) {
            throw new DomainException(ErrorCode.FORBIDDEN, "You are not allowed to create dishes for this restaurant");
        }

        Dish dish = dishPersistencePort.findById(dishId)
                .orElseThrow(() -> new DomainException(ErrorCode.DATA_NOT_FOUND, "Dish not found"));
        dish.setPrice(price);
        dish.setDescription(description);
        DishDomainValidator.validateForUpdate(dish);
        dishPersistencePort.saveDish(dish);
    }

    @Override
    public void updateDishStatus(Boolean active, Long userId, Long dishId) {
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
    public PageResult<Dish> listDishesByRestaurant(Long restaurantId, int page, int size, Long categoryId) {
        restaurantPersistencePort.findById(restaurantId)
                .orElseThrow(() -> new DomainException(ErrorCode.DATA_NOT_FOUND, FOUNDATION));

        return dishPersistencePort.findByRestaurant(restaurantId, page, size, categoryId);
    }
}
