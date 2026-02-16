package com.pragma.plazoleta.domain.api;

import com.pragma.plazoleta.domain.model.Dish;
import com.pragma.plazoleta.domain.model.PageResult;

public interface IDishServicePort {
    void saveDish(Dish dish, Long userId);
    void updateDish(Long restaurantId,Long id, Integer price, String description, Long userId);
    void updateDishStatus(Boolean active, Long userId, Long dishId);
    PageResult<Dish> listDishesByRestaurant(Long restaurantId, int page, int size, Long categoryId);
}
