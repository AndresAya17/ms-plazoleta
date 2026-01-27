package com.pragma.plazoleta.infrastructure.out.jpa.mapper;

import com.pragma.plazoleta.domain.model.Dish;
import com.pragma.plazoleta.infrastructure.out.jpa.entity.DishEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface IDishEntityMapper {

    @Mapping(source = "category", target = "dishCategory")
    DishEntity toEntity(Dish dish);

    @Mapping(source = "dishCategory", target = "category")
    Dish toDish(DishEntity dishEntity);
}
