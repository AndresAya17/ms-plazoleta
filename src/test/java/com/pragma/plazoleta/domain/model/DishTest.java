package com.pragma.plazoleta.domain.model;

import com.pragma.plazoleta.domain.exception.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class DishTest {

    private Dish buildValidDish() {
        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName("Pizza");
        dish.setPrice(25000);
        dish.setDescription("Delicious pizza");
        dish.setImageUrl("http://image.com/pizza.jpg");
        dish.setCategory(DishCategory.MAIN_COURSE);
        dish.setRestaurantId(10L);
        dish.setOwnerId(5L);
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
        DishCategory category = DishCategory.MAIN_COURSE;
        Long restaurantId = 10L;
        Long ownerId = 5L;

        Dish.DishInfo info = new Dish.DishInfo(
                id,
                name,
                price,
                description,
                imageUrl,
                category
        );

        Dish dish = new Dish(info, restaurantId, ownerId);

        assertEquals(id, dish.getId());
        assertEquals(name, dish.getName());
        assertEquals(price, dish.getPrice());
        assertEquals(description, dish.getDescription());
        assertEquals(imageUrl, dish.getImageUrl());
        assertEquals(category, dish.getCategory());
        assertEquals(restaurantId, dish.getRestaurantId());
        assertEquals(ownerId, dish.getOwnerId());
        assertTrue(dish.isActive());
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

    @Test
    void shouldCreateValidDishSuccessfully() {
        Dish dish = buildValidDish();

        assertNotNull(dish);
        assertEquals("Pizza", dish.getName());
        assertEquals(25000, dish.getPrice());
        assertEquals("Delicious pizza", dish.getDescription());
        assertEquals("http://image.com/pizza.jpg", dish.getImageUrl());
        assertEquals(DishCategory.MAIN_COURSE, dish.getCategory());
        assertEquals(10L, dish.getRestaurantId());
        assertEquals(5L, dish.getOwnerId());
        assertTrue(dish.isActive());
    }


    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        Dish.DishInfo info = new Dish.DishInfo(
                null, null, 25000, "Desc", "url", DishCategory.MAIN_COURSE
        );

        assertThrows(
                InvalidDishNameException.class,
                () -> new Dish(info, 1L, 1L)
        );
    }

    @Test
    void shouldThrowExceptionWhenNameIsBlank() {
        Dish.DishInfo info = new Dish.DishInfo(
                null, "   ", 25000, "Desc", "url", DishCategory.MAIN_COURSE
        );

        assertThrows(
                InvalidDishNameException.class,
                () -> new Dish(info, 1L, 1L)
        );
    }

    @Test
    void shouldThrowExceptionWhenPriceIsNull() {
        Dish.DishInfo info = new Dish.DishInfo(
                null, "Pizza", null, "Desc", "url", DishCategory.MAIN_COURSE
        );

        assertThrows(
                InvalidDishPriceException.class,
                () -> new Dish(info, 1L, 1L)
        );
    }

    @Test
    void shouldThrowExceptionWhenPriceIsZeroOrNegative() {
        Dish.DishInfo info = new Dish.DishInfo(
                null, "Pizza", 0, "Desc", "url", DishCategory.MAIN_COURSE
        );

        assertThrows(
                InvalidDishPriceException.class,
                () -> new Dish(info, 1L, 1L)
        );
    }

    @Test
    void shouldThrowExceptionWhenDescriptionIsNull() {
        Dish.DishInfo info = new Dish.DishInfo(
                null, "Pizza", 25000, null, "url", DishCategory.MAIN_COURSE
        );

        assertThrows(
                InvalidDishDescriptionException.class,
                () -> new Dish(info, 1L, 1L)
        );
    }

    @Test
    void shouldThrowExceptionWhenImageUrlIsNull() {
        Dish.DishInfo info = new Dish.DishInfo(
                null, "Pizza", 25000, "Desc", null, DishCategory.MAIN_COURSE
        );

        assertThrows(
                InvalidDishImageException.class,
                () -> new Dish(info, 1L, 1L)
        );
    }

    @Test
    void shouldThrowExceptionWhenCategoryIsNull() {
        Dish.DishInfo info = new Dish.DishInfo(
                null, "Pizza", 25000, "Desc", "url", null
        );

        assertThrows(
                InvalidDishCategoryException.class,
                () -> new Dish(info, 1L, 1L)
        );
    }

    @Test
    void shouldValidateForUpdateSuccessfully() {
        Dish dish = buildValidDish();

        dish.setPrice(30000);
        dish.setDescription("Updated description");

        assertDoesNotThrow(dish::validateForUpdate);
    }

    @Test
    void shouldThrowExceptionWhenUpdatePriceIsInvalid() {
        Dish dish = buildValidDish();
        dish.setPrice(0);

        assertThrows(
                InvalidDishPriceException.class,
                dish::validateForUpdate
        );
    }

    @Test
    void shouldThrowExceptionWhenUpdateDescriptionIsInvalid() {
        Dish dish = buildValidDish();
        dish.setDescription(" ");

        assertThrows(
                InvalidDishDescriptionException.class,
                dish::validateForUpdate
        );
    }

    @Test
    void shouldThrowExceptionWhenRestaurantIdIsNull() {
        Dish dish = new Dish();
        dish.setRestaurantId(null);

        assertThrows(
                InvalidRestaurantIdException.class,
                dish::validateRestaurantId
        );
    }
    @Test
    void shouldThrowExceptionWhenRestaurantIdIsZero() {
        Dish dish = new Dish();
        dish.setRestaurantId(0L);

        assertThrows(
                InvalidRestaurantIdException.class,
                dish::validateRestaurantId
        );
    }
    @Test
    void shouldThrowExceptionWhenRestaurantIdIsNegative() {
        Dish dish = new Dish();
        dish.setRestaurantId(-10L);

        assertThrows(
                InvalidRestaurantIdException.class,
                dish::validateRestaurantId
        );
    }
}
