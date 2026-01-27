package com.pragma.plazoleta.application.mapper;

import com.pragma.plazoleta.application.dto.request.DishRequestDto;
import com.pragma.plazoleta.domain.model.Dish;
import com.pragma.plazoleta.domain.model.DishCategory;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;
public class IDishRequestMapperTest {

    private final IDishRequestMapper mapper =
            Mappers.getMapper(IDishRequestMapper.class);

    @Test
    void shouldMapDishRequestDtoToDish() {
        // arrange
        DishRequestDto dto = new DishRequestDto();
        dto.setName("Pizza");
        dto.setPrice(30000);
        dto.setDescription("Pizza artesanal");
        dto.setImageUrl("https://img.com/pizza.png");

        // act
        Dish dish = mapper.toDish(dto);

        // assert
        assertThat(dish).isNotNull();
        assertThat(dish.getName()).isEqualTo("Pizza");
        assertThat(dish.getPrice()).isEqualTo(30000);
        assertThat(dish.getDescription()).isEqualTo("Pizza artesanal");
        assertThat(dish.getImageUrl()).isEqualTo("https://img.com/pizza.png");
    }

    @Test
    void shouldReturnNullWhenDishRequestDtoIsNull() {
        Dish dish = mapper.toDish(null);
        assertThat(dish).isNull();
    }

}
