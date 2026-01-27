package com.pragma.plazoleta.domain.model;

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


    public Dish(Long id, String name, Integer price, String description, String imageUrl, DishCategory category, Long restaurantId, Long ownerId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
        this.category = category;
        this.active = false;
        this.restaurantId = restaurantId;
        this.ownerId = ownerId;
    }
}
