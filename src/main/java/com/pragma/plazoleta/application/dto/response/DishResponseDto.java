package com.pragma.plazoleta.application.dto.response;

import com.pragma.plazoleta.domain.model.DishCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DishResponseDto {
    private String name;
    private Integer price;
    private String description;
    private String imageUrl;
    private DishCategory category;
}
