package com.pragma.plazoleta.domain.spi;

import com.pragma.plazoleta.domain.model.Dish;
import com.pragma.plazoleta.domain.model.PageResult;

import java.util.Optional;

public interface IDishPersistencePort {
    Dish saveDish(Dish dish);
    Optional<Dish> findById(Long idDish);
    PageResult<Dish> findByRestaurant(Long restaurantId, int page, int size, Long categoryId);
}
