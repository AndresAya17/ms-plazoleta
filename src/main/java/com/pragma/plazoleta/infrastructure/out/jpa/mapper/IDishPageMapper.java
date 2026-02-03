package com.pragma.plazoleta.infrastructure.out.jpa.mapper;

import com.pragma.plazoleta.domain.model.Dish;
import com.pragma.plazoleta.domain.model.PageResult;
import com.pragma.plazoleta.infrastructure.out.jpa.entity.DishEntity;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface IDishPageMapper {
    default PageResult<Dish> toDomain(
            Page<DishEntity> page,
            IDishEntityMapper dishEntityMapper
    ) {
        return new PageResult<>(
                page.getContent()
                        .stream()
                        .map(dishEntityMapper::toDish)
                        .toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements()
        );
    }
}
