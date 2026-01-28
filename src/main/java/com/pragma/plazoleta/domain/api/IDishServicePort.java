package com.pragma.plazoleta.domain.api;

import com.pragma.plazoleta.domain.model.Dish;

public interface IDishServicePort {
    void saveDish(Dish dish, Long userId, String rol);
    void updateDish(Long restaurantId,Long id, Integer price, String description, Long userId, String rol);
}
