package com.pragma.plazoleta.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class DishRequestDto {
    @NotBlank(message = "The dish name is required")
    private String name;

    @NotNull(message = "The price is required")
    @Positive(message = "The price must be greater than 0")
    private Integer price;

    @NotBlank(message = "The description is required")
    private String description;

    @NotBlank(message = "The image URL is required")
    private String imageUrl;

    @NotNull(message = "The dish category is required")
    private Long category;

    @NotNull(message = "The restaurant id is required")
    private Long restaurantId;

}
