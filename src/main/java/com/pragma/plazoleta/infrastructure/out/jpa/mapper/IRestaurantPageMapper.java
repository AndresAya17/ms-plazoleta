package com.pragma.plazoleta.infrastructure.out.jpa.mapper;

import com.pragma.plazoleta.domain.model.PageResult;
import com.pragma.plazoleta.domain.model.Restaurant;
import com.pragma.plazoleta.infrastructure.out.jpa.entity.RestaurantEntity;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IRestaurantPageMapper {

    default PageResult<Restaurant> toDomain(
            Page<RestaurantEntity> page,
            IRestaurantEntityMapper restaurantEntityMapper
    ) {
        List<Restaurant> restaurants =
                page.getContent()
                        .stream()
                        .map(restaurantEntityMapper::toRestaurant)
                        .toList();

        return new PageResult<>(
                restaurants,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements()
        );
    }

}
