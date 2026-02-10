package com.pragma.plazoleta.domain.model;

import com.pragma.plazoleta.domain.exception.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class DishTest {

    Category category = new Category(
            1L,
            "MAIN_COURSE",
            "Platos principales"
    );

    private Dish buildValidDish() {
        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName("Pizza");
        dish.setPrice(25000);
        dish.setDescription("Delicious pizza");
        dish.setImageUrl("http://image.com/pizza.jpg");
        dish.setCategory(category);
        dish.setRestaurantId(10L);
        dish.setActive(true);
        return dish;
    }

    @Test
    void shouldCreateDishWithInactiveStatusByDefault() {
        Long id = 1L;
        String name = "Pizza Margarita";
        Integer price = 25000;
        String description = "Classic pizza";
        String imageUrl = "http://image.com/pizza.jpg";
        Long restaurantId = 10L;

        Category category = new Category(
                1L,
                "MAIN_COURSE",
                "Platos principales"
        );

        Dish dish = new Dish();
        dish.setId(id);
        dish.setName(name);
        dish.setPrice(price);
        dish.setDescription(description);
        dish.setImageUrl(imageUrl);
        dish.setCategory(category);
        dish.setRestaurantId(restaurantId);
        dish.setActive(true);

        assertEquals(id, dish.getId());
        assertEquals(name, dish.getName());
        assertEquals(price, dish.getPrice());
        assertEquals(description, dish.getDescription());
        assertEquals(imageUrl, dish.getImageUrl());
        assertEquals(category, dish.getCategory());
        assertEquals(restaurantId, dish.getRestaurantId());
        assertTrue(dish.isActive());
    }
}
