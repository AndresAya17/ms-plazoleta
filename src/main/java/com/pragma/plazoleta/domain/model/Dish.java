package com.pragma.plazoleta.domain.model;

import com.pragma.plazoleta.domain.exception.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Dish {
    private Long id;
    private String name;
    private Integer price;
    private String description;
    private String imageUrl;
    private DishCategory category;
    private boolean active;
    private Long restaurantId;
    private Long ownerId;

    public record DishInfo(
            Long id,
            String name,
            Integer price,
            String description,
            String imageUrl,
            DishCategory category
    ) {}


    public Dish(DishInfo info, Long restaurantId, Long ownerId) {
        this.id = info.id;
        this.name = info.name;
        this.price = info.price;
        this.description = info.description;
        this.imageUrl = info.imageUrl;
        this.category = info.category;
        this.restaurantId = restaurantId;
        this.ownerId = ownerId;
        this.active = true;
        validate();
    }
    public void validate() {
        validateName();
        validatePrice();
        validateDescription();
        validateImageUrl();
        validateCategory();
        validateRestaurantId();
    }

    public void validateForUpdate() {
        validatePrice();
        validateDescription();
    }

    public void validateName() {
        if (name == null || name.trim().isEmpty()) {
            throw new DomainException(ErrorCode.INVALID_DISH, "Dish name is required");
        }
    }

    public void validatePrice() {
        if (price == null || price <= 0) {
            throw new DomainException(ErrorCode.INVALID_DISH, "Dish price must be greater than zero");
        }
    }

    public void validateDescription() {
        if (description == null || description.trim().isEmpty()) {
            throw new DomainException(ErrorCode.INVALID_DISH, "Dish description is required");
        }
    }

    public void validateImageUrl() {
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            throw new DomainException(ErrorCode.INVALID_DISH, "Dish image URL is required");
        }
    }

    public void validateCategory() {
        if (category == null) {
            throw new DomainException(ErrorCode.INVALID_DISH, "Dish category is required");
        }
    }

    public void validateRestaurantId() {
        if (restaurantId == null || restaurantId <= 0) {
            throw new DomainException(ErrorCode.INVALID_DISH, "Restaurant ID is required and must be greater than zero");
        }
    }

}
