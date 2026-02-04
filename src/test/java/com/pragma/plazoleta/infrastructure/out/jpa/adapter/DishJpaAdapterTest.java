package com.pragma.plazoleta.infrastructure.out.jpa.adapter;


import com.pragma.plazoleta.domain.model.Dish;
import com.pragma.plazoleta.domain.model.DishCategory;
import com.pragma.plazoleta.domain.model.PageResult;
import com.pragma.plazoleta.infrastructure.out.jpa.entity.DishEntity;
import com.pragma.plazoleta.infrastructure.out.jpa.mapper.IDishEntityMapper;
import com.pragma.plazoleta.infrastructure.out.jpa.mapper.IDishPageMapper;
import com.pragma.plazoleta.infrastructure.out.jpa.repository.IDishRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DishJpaAdapterTest {

    @Mock
    private IDishRepository dishRepository;

    @Mock
    private IDishEntityMapper dishEntityMapper;

    @Mock
    private IDishPageMapper dishPageMapper;

    @InjectMocks
    private DishJpaAdapter dishJpaAdapter;

    @Test
    void shouldSaveDishAndReturnDomainDish() {
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

        Dish result = dishJpaAdapter.saveDish(dish);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Pasta");

        verify(dishEntityMapper).toEntity(dish);
        verify(dishRepository).save(dishEntityToSave);
        verify(dishEntityMapper).toDish(savedDishEntity);
    }

    @Test
    void shouldUpdateDishAndReturnUpdatedDomainDish() {
        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName("Pasta");
        dish.setPrice(30000);
        dish.setDescription("Updated description");

        DishEntity dishEntityToUpdate = new DishEntity();
        dishEntityToUpdate.setId(1L);
        dishEntityToUpdate.setName("Pasta");

        DishEntity updatedDishEntity = new DishEntity();
        updatedDishEntity.setId(1L);
        updatedDishEntity.setName("Pasta");

        Dish expectedDish = new Dish();
        expectedDish.setId(1L);
        expectedDish.setName("Pasta");

        when(dishEntityMapper.toEntity(dish))
                .thenReturn(dishEntityToUpdate);

        when(dishRepository.save(dishEntityToUpdate))
                .thenReturn(updatedDishEntity);

        when(dishEntityMapper.toDish(updatedDishEntity))
                .thenReturn(expectedDish);

        Dish result = dishJpaAdapter.saveDish(dish);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Pasta");

        verify(dishEntityMapper).toEntity(dish);
        verify(dishRepository).save(dishEntityToUpdate);
        verify(dishEntityMapper).toDish(updatedDishEntity);
        verifyNoMoreInteractions(dishRepository, dishEntityMapper);
    }

    @Test
    void shouldReturnDishWhenFoundById() {
        Long dishId = 1L;

        DishEntity dishEntity = new DishEntity();
        dishEntity.setId(dishId);
        dishEntity.setName("Pasta");

        Dish dish = new Dish();
        dish.setId(dishId);
        dish.setName("Pasta");

        when(dishRepository.findById(dishId))
                .thenReturn(Optional.of(dishEntity));

        when(dishEntityMapper.toDish(dishEntity))
                .thenReturn(dish);

        Optional<Dish> result = dishJpaAdapter.findById(dishId);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(dishId);
        assertThat(result.get().getName()).isEqualTo("Pasta");

        verify(dishRepository).findById(dishId);
        verify(dishEntityMapper).toDish(dishEntity);
        verifyNoMoreInteractions(dishRepository, dishEntityMapper);
    }
    @Test
    void shouldFindDishesByRestaurantWhenCategoryIsNull() {
        Long restaurantId = 1L;
        int page = 0;
        int size = 10;

        DishEntity dishEntity = new DishEntity();
        dishEntity.setId(1L);
        dishEntity.setName("Pasta");

        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName("Pasta");

        Page<DishEntity> dishEntityPage =
                new PageImpl<>(
                        List.of(dishEntity),
                        PageRequest.of(page, size),
                        1
                );

        PageResult<Dish> pageResult =
                new PageResult<>(
                        List.of(dish),
                        page,
                        size,
                        1L
                );

        when(dishRepository.findByRestaurantIdAndActiveTrue(
                restaurantId,
                PageRequest.of(page, size)
        )).thenReturn(dishEntityPage);

        when(dishPageMapper.toDomain(dishEntityPage, dishEntityMapper))
                .thenReturn(pageResult);

        PageResult<Dish> result =
                dishJpaAdapter.findByRestaurant(
                        restaurantId,
                        null,
                        page,
                        size
                );

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Pasta");

        verify(dishRepository)
                .findByRestaurantIdAndActiveTrue(
                        restaurantId,
                        PageRequest.of(page, size)
                );
        verify(dishPageMapper)
                .toDomain(dishEntityPage, dishEntityMapper);
        verifyNoMoreInteractions(dishRepository, dishPageMapper);
    }

    @Test
    void shouldFindDishesByRestaurantWhenCategoryIsProvided() {
        Long restaurantId = 1L;
        DishCategory category = DishCategory.STARTER;
        int page = 0;
        int size = 10;

        DishEntity dishEntity = new DishEntity();
        dishEntity.setId(1L);
        dishEntity.setName("Pizza");
        dishEntity.setDishCategory(category);

        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName("Pizza");
        dish.setCategory(category);

        Page<DishEntity> dishEntityPage =
                new PageImpl<>(
                        List.of(dishEntity),
                        PageRequest.of(page, size),
                        1
                );

        PageResult<Dish> pageResult =
                new PageResult<>(
                        List.of(dish),
                        page,
                        size,
                        1L
                );

        when(dishRepository.findByRestaurantIdAndDishCategoryAndActiveTrue(
                restaurantId,
                category,
                PageRequest.of(page, size)
        )).thenReturn(dishEntityPage);

        when(dishPageMapper.toDomain(dishEntityPage, dishEntityMapper))
                .thenReturn(pageResult);

        PageResult<Dish> result =
                dishJpaAdapter.findByRestaurant(
                        restaurantId,
                        category,
                        page,
                        size
                );

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getCategory())
                .isEqualTo(DishCategory.STARTER);

        verify(dishRepository)
                .findByRestaurantIdAndDishCategoryAndActiveTrue(
                        restaurantId,
                        category,
                        PageRequest.of(page, size)
                );
        verify(dishPageMapper)
                .toDomain(dishEntityPage, dishEntityMapper);
        verifyNoMoreInteractions(dishRepository, dishPageMapper);
    }


}
