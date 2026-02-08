package com.pragma.plazoleta.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Dish {
    private Long id;
    private String name;
    private Integer price;
    private String description;
    private String imageUrl;
    private Category category;
    private boolean active;
    private Long restaurantId;
}
