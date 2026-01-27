package com.pragma.plazoleta.application.mapper;

import com.pragma.plazoleta.application.dto.request.DishRequestDto;
import com.pragma.plazoleta.domain.model.Dish;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IDishRequestMapper {
    Dish toDish(DishRequestDto dishRequestDto);
}
