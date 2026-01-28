package com.pragma.plazoleta.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class DishTest {
    @Test
    void shouldCreateDishWithInactiveStatusByDefault() {
        // arrange
        Long id = 1L;
        String name = "Pizza Margarita";
        Integer price = 25000;
        String description = "Classic pizza";
        String imageUrl = "http://image.com/pizza.jpg";
        DishCategory category = DishCategory.MAIN_COURSE;
        Long restaurantId = 10L;
        Long ownerId = 5L;

        // act
        Dish dish = new Dish(
                id,
                name,
                price,
                description,
                imageUrl,
                category,
                restaurantId,
                ownerId
        );

        // assert
        assertEquals(id, dish.getId());
        assertEquals(name, dish.getName());
        assertEquals(price, dish.getPrice());
        assertEquals(description, dish.getDescription());
        assertEquals(imageUrl, dish.getImageUrl());
        assertEquals(category, dish.getCategory());
        assertEquals(restaurantId, dish.getRestaurantId());
        assertEquals(ownerId, dish.getOwnerId());
    }

    @Test
    void shouldAllowUpdatingDishFieldsUsingSetters() {
        // arrange
        Dish dish = new Dish();

        // act
        dish.setId(2L);
        dish.setName("Burger");
        dish.setPrice(18000);
        dish.setDescription("Beef burger");
        dish.setImageUrl("http://image.com/burger.jpg");
        dish.setCategory(DishCategory.MAIN_COURSE);
        dish.setRestaurantId(20L);
        dish.setOwnerId(8L);
        dish.setActive(true);

        // assert
        assertEquals(2L, dish.getId());
        assertEquals("Burger", dish.getName());
        assertEquals(18000, dish.getPrice());
        assertEquals("Beef burger", dish.getDescription());
        assertEquals("http://image.com/burger.jpg", dish.getImageUrl());
        assertEquals(DishCategory.MAIN_COURSE, dish.getCategory());
        assertEquals(20L, dish.getRestaurantId());
        assertEquals(8L, dish.getOwnerId());
        assertTrue(dish.isActive());
    }
}
