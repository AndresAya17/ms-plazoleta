package com.pragma.plazoleta.infrastructure.out.jpa.mapper;


import com.pragma.plazoleta.domain.model.Category;
import com.pragma.plazoleta.domain.model.Dish;
import com.pragma.plazoleta.infrastructure.out.jpa.entity.DishEntity;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {
        IDishEntityMapperImpl.class,
        ICategoryEntityMapperImpl.class
})
public class IDishEntityMapperTest {

    @Autowired
    private IDishEntityMapper mapper;

    @Test
    void shouldMapDishToDishEntity() {
        Category category = new Category(
                1L,
                "MAIN_COURSE",
                "Platos principales"
        );
        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName("Pasta");
        dish.setPrice(25000);
        dish.setDescription("Pasta artesanal");
        dish.setCategory(category);

        DishEntity entity = mapper.toEntity(dish);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getName()).isEqualTo("Pasta");
        assertThat(entity.getPrice()).isEqualTo(25000);
        assertThat(entity.getDescription()).isEqualTo("Pasta artesanal");
    }
    @Test
    void shouldMapDishEntityToDish() {
        DishEntity entity = new DishEntity();
        entity.setId(2L);
        entity.setName("Postre");
        entity.setPrice(12000);
        entity.setDescription("Cheesecake");

        Dish dish = mapper.toDish(entity);

        assertThat(dish).isNotNull();
        assertThat(dish.getId()).isEqualTo(2L);
        assertThat(dish.getName()).isEqualTo("Postre");
        assertThat(dish.getPrice()).isEqualTo(12000);
        assertThat(dish.getDescription()).isEqualTo("Cheesecake");
    }
}
