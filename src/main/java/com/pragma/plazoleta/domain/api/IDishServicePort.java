package com.pragma.plazoleta.domain.api;

import com.pragma.plazoleta.domain.model.Dish;

public interface IDishServicePort {
    void saveDish(Dish dish);
    void updateDish(Long id, Integer price, String description);
}
