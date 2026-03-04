package com.pragma.plazoleta.domain.usecase;

import com.pragma.plazoleta.domain.api.IDishServicePort;
import com.pragma.plazoleta.domain.constants.DomainConstants;
import com.pragma.plazoleta.domain.exception.*;
import com.pragma.plazoleta.domain.model.*;
import com.pragma.plazoleta.domain.spi.IDishPersistencePort;
import com.pragma.plazoleta.domain.spi.IRestaurantPersistencePort;
import com.pragma.plazoleta.domain.validator.DishDomainValidator;

public class DishUseCase implements IDishServicePort {

    private final IDishPersistencePort dishPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;


    public DishUseCase(IDishPersistencePort dishPersistencePort, IRestaurantPersistencePort restaurantPersistencePort){
        this.dishPersistencePort = dishPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
    }

    @Override
    public void saveDish(Dish dish, Long userId) {
        DishDomainValidator.validate(dish);
        Restaurant restaurant = restaurantPersistencePort.findById(dish.getRestaurantId())
                .orElseThrow(() -> new DomainException(ErrorCode.DATA_NOT_FOUND, DomainConstants.RNF));
        if (!restaurant.getOwnerId().equals(userId)) {
            throw new DomainException(ErrorCode.FORBIDDEN, DomainConstants.NAR);
        }
        dish.setActive(true);
        dishPersistencePort.saveDish(dish);

    }

    @Override
    public void updateDish(Long restaurantId, Long dishId, Integer price, String description, Long userId) {
        Restaurant restaurant = restaurantPersistencePort.findById(restaurantId)
                .orElseThrow(() -> new DomainException(ErrorCode.DATA_NOT_FOUND, DomainConstants.RNF));
        if (!restaurant.getOwnerId().equals(userId)) {
            throw new DomainException(ErrorCode.FORBIDDEN, DomainConstants.NAR);
        }

        Dish dish = dishPersistencePort.findById(dishId)
                .orElseThrow(() -> new DomainException(ErrorCode.DATA_NOT_FOUND, DomainConstants.DNF));
        dish.setPrice(price);
        dish.setDescription(description);
        DishDomainValidator.validateForUpdate(dish);
        dishPersistencePort.saveDish(dish);
    }

    @Override
    public void updateDishStatus(Boolean active, Long userId, Long dishId) {
        Dish dish = dishPersistencePort.findById(dishId)
                .orElseThrow(() -> new DomainException(ErrorCode.DATA_NOT_FOUND, DomainConstants.DNF));

        Restaurant restaurant = restaurantPersistencePort.findById(dish.getRestaurantId())
                .orElseThrow(() -> new DomainException(ErrorCode.DATA_NOT_FOUND, DomainConstants.RNF));
        if (!restaurant.getOwnerId().equals(userId)) {
            throw new DomainException(
                    ErrorCode.FORBIDDEN,
                    DomainConstants.NAR
            );
        }
        dish.setActive(active);
        dishPersistencePort.saveDish(dish);
    }

    @Override
    public PageResult<Dish> listDishesByRestaurant(Long restaurantId, int page, int size, Long categoryId) {
        restaurantPersistencePort.findById(restaurantId)
                .orElseThrow(() -> new DomainException(ErrorCode.DATA_NOT_FOUND, DomainConstants.RNF));

        return dishPersistencePort.findByRestaurant(restaurantId, page, size, categoryId);
    }
}
