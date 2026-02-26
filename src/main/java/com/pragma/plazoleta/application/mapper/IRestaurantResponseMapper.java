package com.pragma.plazoleta.application.mapper;

import com.pragma.plazoleta.application.dto.response.DishResponseDto;
import com.pragma.plazoleta.application.dto.response.PageResponseDto;
import com.pragma.plazoleta.application.dto.response.RestaurantListResponseDto;
import com.pragma.plazoleta.application.dto.response.RestaurantResponseDto;
import com.pragma.plazoleta.domain.model.Dish;
import com.pragma.plazoleta.domain.model.PageResult;
import com.pragma.plazoleta.domain.model.Restaurant;
import com.pragma.plazoleta.infrastructure.out.jpa.mapper.IDishPageMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        uses = IDishPageMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IRestaurantResponseMapper {
    RestaurantResponseDto toResponse(Restaurant restaurant);

    @Mapping(target = "hasNext", expression = "java(pageResult.hasNext())")
    PageResponseDto<DishResponseDto> toResponsePage(PageResult<Dish> pageResult);

    PageResponseDto<RestaurantListResponseDto> RestaurantToResponsePage(PageResult<Restaurant> pageResult);
}
