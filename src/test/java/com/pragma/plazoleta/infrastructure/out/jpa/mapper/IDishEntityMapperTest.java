package com.pragma.plazoleta.infrastructure.out.jpa.mapper;


import com.pragma.plazoleta.domain.model.Dish;
import com.pragma.plazoleta.infrastructure.out.jpa.entity.DishEntity;
import com.pragma.plazoleta.domain.model.DishCategory;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;
public class IDishEntityMapperTest {

    private final IDishEntityMapper mapper =
            Mappers.getMapper(IDishEntityMapper.class);

    @Test
    void shouldMapDishToDishEntity() {
        // arrange
        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName("Pasta");
        dish.setPrice(25000);
        dish.setDescription("Pasta artesanal");
        dish.setCategory(DishCategory.MAIN_COURSE);

        // act
        DishEntity entity = mapper.toEntity(dish);

        // assert
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getName()).isEqualTo("Pasta");
        assertThat(entity.getPrice()).isEqualTo(25000);
        assertThat(entity.getDescription()).isEqualTo("Pasta artesanal");
        assertThat(entity.getDishCategory()).isEqualTo(DishCategory.MAIN_COURSE);
    }
    @Test
    void shouldMapDishEntityToDish() {
        // arrange
        DishEntity entity = new DishEntity();
        entity.setId(2L);
        entity.setName("Postre");
        entity.setPrice(12000);
        entity.setDescription("Cheesecake");
        entity.setDishCategory(DishCategory.DESSERT);

        // act
        Dish dish = mapper.toDish(entity);

        // assert
        assertThat(dish).isNotNull();
        assertThat(dish.getId()).isEqualTo(2L);
        assertThat(dish.getName()).isEqualTo("Postre");
        assertThat(dish.getPrice()).isEqualTo(12000);
        assertThat(dish.getDescription()).isEqualTo("Cheesecake");
        assertThat(dish.getCategory()).isEqualTo(DishCategory.DESSERT);
    }
}
