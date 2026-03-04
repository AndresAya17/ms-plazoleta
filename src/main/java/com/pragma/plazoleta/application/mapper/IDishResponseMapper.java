package com.pragma.plazoleta.application.mapper;

import com.pragma.plazoleta.application.dto.response.DishResponseDto;
import com.pragma.plazoleta.domain.model.Dish;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IDishResponseMapper {

    @Mapping(target = "categoryId", source = "category.id")
    DishResponseDto toResponse(Dish dish);

    List<DishResponseDto> toResponseList(List<Dish> dishes);
}
