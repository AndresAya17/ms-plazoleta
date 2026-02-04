package com.pragma.plazoleta.infrastructure.out.jpa.mapper;

import com.pragma.plazoleta.domain.model.Dish;
import com.pragma.plazoleta.domain.model.PageResult;
import com.pragma.plazoleta.infrastructure.out.jpa.entity.DishEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class IDishPageMapperTest {

    private IDishPageMapper dishPageMapper;

    @Mock
    private IDishEntityMapper dishEntityMapper;

    @BeforeEach
    void setUp() {
        dishPageMapper = new IDishPageMapper() {};
    }

    @Test
    void shouldMapPageDishEntityToPageResultDomain() {
        DishEntity dishEntity = new DishEntity();
        dishEntity.setId(1L);
        dishEntity.setName("Pasta");

        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName("Pasta");

        Page<DishEntity> page =
                new PageImpl<>(
                        List.of(dishEntity),
                        PageRequest.of(0, 10),
                        1
                );

        when(dishEntityMapper.toDish(dishEntity))
                .thenReturn(dish);

        PageResult<Dish> result =
                dishPageMapper.toDomain(page, dishEntityMapper);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Pasta");
        assertThat(result.getPage()).isEqualTo(0);
        assertThat(result.getSize()).isEqualTo(10);
        assertThat(result.getTotalElements()).isEqualTo(1);

        verify(dishEntityMapper).toDish(dishEntity);
        verifyNoMoreInteractions(dishEntityMapper);
    }
}
