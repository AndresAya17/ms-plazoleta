package com.pragma.plazoleta.domain.validator;

import com.pragma.plazoleta.domain.exception.DomainException;
import com.pragma.plazoleta.domain.exception.ErrorCode;
import com.pragma.plazoleta.domain.model.Dish;

public class DishDomainValidator {

    private DishDomainValidator() {}

    public static void validate(Dish dish) {
        validateName(dish);
        validatePrice(dish);
        validateDescription(dish);
        validateImageUrl(dish);
        validateCategory(dish);
        validateRestaurantId(dish);
    }

    public static void validateForUpdate(Dish dish) {
        validatePrice(dish);
        validateDescription(dish);
    }

    private static void validateName(Dish dish) {
        if (dish.getName() == null || dish.getName().trim().isEmpty()) {
            throw new DomainException(ErrorCode.INVALID_DISH, "Dish name is required");
        }
    }

    private static void validatePrice(Dish dish) {
        if (dish.getPrice() == null || dish.getPrice() <= 0) {
            throw new DomainException(ErrorCode.INVALID_DISH, "Dish price must be greater than zero");
        }
    }

    private static void validateDescription(Dish dish) {
        if (dish.getDescription() == null || dish.getDescription().trim().isEmpty()) {
            throw new DomainException(ErrorCode.INVALID_DISH, "Dish description is required");
        }
    }

    private static void validateImageUrl(Dish dish) {
        if (dish.getImageUrl() == null || dish.getImageUrl().trim().isEmpty()) {
            throw new DomainException(ErrorCode.INVALID_DISH, "Dish image URL is required");
        }
    }

    private static void validateCategory(Dish dish) {
        if (dish.getCategory() == null) {
            throw new DomainException(ErrorCode.INVALID_DISH, "Dish category is required");
        }
    }

    private static void validateRestaurantId(Dish dish) {
        if (dish.getRestaurantId() == null || dish.getRestaurantId() <= 0) {
            throw new DomainException(ErrorCode.INVALID_DISH, "Restaurant ID is required and must be greater than zero");
        }
    }
}
