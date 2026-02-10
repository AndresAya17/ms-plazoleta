package com.pragma.plazoleta.domain.validator;

import com.pragma.plazoleta.domain.exception.DomainException;
import com.pragma.plazoleta.domain.exception.ErrorCode;
import com.pragma.plazoleta.domain.model.Category;
import com.pragma.plazoleta.domain.model.Dish;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DishDomainValidatorTest {

    private Dish buildValidDish() {
        Dish dish = new Dish();
        dish.setName("Pasta Alfredo");
        dish.setPrice(15000);
        dish.setDescription("Pasta con salsa alfredo");
        dish.setImageUrl("https://img.com/pasta.jpg");

        Category category = new Category(
                1L,
                "MAIN_COURSE",
                "Main course dishes"
        );
        dish.setCategory(category);
        dish.setRestaurantId(1L);

        return dish;
    }

    @Test
    void shouldNotThrowExceptionWhenDishIsValid() {
        Dish dish = buildValidDish();

        assertDoesNotThrow(() ->
                DishDomainValidator.validate(dish)
        );
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        Dish dish = buildValidDish();
        dish.setName(null);

        DomainException exception = assertThrows(
                DomainException.class,
                () -> DishDomainValidator.validate(dish)
        );

        assertEquals(ErrorCode.INVALID_DISH, exception.getErrorCode());
    }

    @Test
    void shouldThrowExceptionWhenNameIsEmpty() {
        Dish dish = buildValidDish();
        dish.setName(" ");

        DomainException exception = assertThrows(
                DomainException.class,
                () -> DishDomainValidator.validate(dish)
        );

        assertEquals(ErrorCode.INVALID_DISH, exception.getErrorCode());
    }

    @Test
    void shouldThrowExceptionWhenPriceIsNull() {
        Dish dish = buildValidDish();
        dish.setPrice(null);

        DomainException exception = assertThrows(
                DomainException.class,
                () -> DishDomainValidator.validate(dish)
        );

        assertEquals(ErrorCode.INVALID_DISH, exception.getErrorCode());
    }

    @Test
    void shouldThrowExceptionWhenPriceIsZeroOrNegative() {
        Dish dish = buildValidDish();
        dish.setPrice(0);

        DomainException exception = assertThrows(
                DomainException.class,
                () -> DishDomainValidator.validate(dish)
        );

        assertEquals(ErrorCode.INVALID_DISH, exception.getErrorCode());
    }

    @Test
    void shouldThrowExceptionWhenDescriptionIsNull() {
        Dish dish = buildValidDish();
        dish.setDescription(null);

        DomainException exception = assertThrows(
                DomainException.class,
                () -> DishDomainValidator.validate(dish)
        );

        assertEquals(ErrorCode.INVALID_DISH, exception.getErrorCode());
    }

    @Test
    void shouldThrowExceptionWhenDescriptionIsEmpty() {
        Dish dish = buildValidDish();
        dish.setDescription(" ");

        DomainException exception = assertThrows(
                DomainException.class,
                () -> DishDomainValidator.validate(dish)
        );

        assertEquals(ErrorCode.INVALID_DISH, exception.getErrorCode());
    }

    @Test
    void shouldThrowExceptionWhenImageUrlIsNull() {
        Dish dish = buildValidDish();
        dish.setImageUrl(null);

        DomainException exception = assertThrows(
                DomainException.class,
                () -> DishDomainValidator.validate(dish)
        );

        assertEquals(ErrorCode.INVALID_DISH, exception.getErrorCode());
    }

    @Test
    void shouldThrowExceptionWhenImageUrlIsEmpty() {
        Dish dish = buildValidDish();
        dish.setImageUrl(" ");

        DomainException exception = assertThrows(
                DomainException.class,
                () -> DishDomainValidator.validate(dish)
        );

        assertEquals(ErrorCode.INVALID_DISH, exception.getErrorCode());
    }

    @Test
    void shouldThrowExceptionWhenCategoryIsNull() {
        Dish dish = buildValidDish();
        dish.setCategory(null);

        DomainException exception = assertThrows(
                DomainException.class,
                () -> DishDomainValidator.validate(dish)
        );

        assertEquals(ErrorCode.INVALID_DISH, exception.getErrorCode());
    }

    @Test
    void shouldThrowExceptionWhenRestaurantIdIsNull() {
        Dish dish = buildValidDish();
        dish.setRestaurantId(null);

        DomainException exception = assertThrows(
                DomainException.class,
                () -> DishDomainValidator.validate(dish)
        );

        assertEquals(ErrorCode.INVALID_DISH, exception.getErrorCode());
    }

    @Test
    void shouldThrowExceptionWhenRestaurantIdIsZeroOrNegative() {
        Dish dish = buildValidDish();
        dish.setRestaurantId(0L);

        DomainException exception = assertThrows(
                DomainException.class,
                () -> DishDomainValidator.validate(dish)
        );

        assertEquals(ErrorCode.INVALID_DISH, exception.getErrorCode());
    }

    @Test
    void shouldNotThrowExceptionWhenUpdatingValidDish() {
        Dish dish = buildValidDish();

        assertDoesNotThrow(() ->
                DishDomainValidator.validateForUpdate(dish)
        );
    }

    @Test
    void shouldThrowExceptionWhenUpdatingWithInvalidPrice() {
        Dish dish = buildValidDish();
        dish.setPrice(-100);

        DomainException exception = assertThrows(
                DomainException.class,
                () -> DishDomainValidator.validateForUpdate(dish)
        );

        assertEquals(ErrorCode.INVALID_DISH, exception.getErrorCode());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingWithInvalidDescription() {
        Dish dish = buildValidDish();
        dish.setDescription(" ");

        DomainException exception = assertThrows(
                DomainException.class,
                () -> DishDomainValidator.validateForUpdate(dish)
        );

        assertEquals(ErrorCode.INVALID_DISH, exception.getErrorCode());
    }

}
