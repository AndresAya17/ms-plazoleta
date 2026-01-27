package com.pragma.plazoleta.infrastructure.out.jpa.adapter;


import com.pragma.plazoleta.domain.model.Dish;
import com.pragma.plazoleta.domain.model.DishCategory;
import com.pragma.plazoleta.infrastructure.out.jpa.entity.DishEntity;
import com.pragma.plazoleta.infrastructure.out.jpa.mapper.IDishEntityMapper;
import com.pragma.plazoleta.infrastructure.out.jpa.repository.IDishRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DishJpaAdapterTest {

    @Mock
    private IDishRepository dishRepository;

    @Mock
    private IDishEntityMapper dishEntityMapper;

    @InjectMocks
    private DishJpaAdapter dishJpaAdapter;

    @Test
    void shouldSaveDishAndReturnDomainDish() {
        // arrange
        Dish dish = new Dish();
        dish.setName("Pasta");
        dish.setPrice(25000);
        dish.setDescription("Pasta artesanal");
        dish.setCategory(DishCategory.MAIN_COURSE);

        DishEntity dishEntityToSave = new DishEntity();
        dishEntityToSave.setName("Pasta");

        DishEntity savedDishEntity = new DishEntity();
        savedDishEntity.setId(1L);
        savedDishEntity.setName("Pasta");

        Dish expectedDish = new Dish();
        expectedDish.setId(1L);
        expectedDish.setName("Pasta");

        when(dishEntityMapper.toEntity(dish))
                .thenReturn(dishEntityToSave);

        when(dishRepository.save(dishEntityToSave))
                .thenReturn(savedDishEntity);

        when(dishEntityMapper.toDish(savedDishEntity))
                .thenReturn(expectedDish);

        // act
        Dish result = dishJpaAdapter.saveDish(dish);

        // assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Pasta");

        verify(dishEntityMapper).toEntity(dish);
        verify(dishRepository).save(dishEntityToSave);
        verify(dishEntityMapper).toDish(savedDishEntity);
    }




}
