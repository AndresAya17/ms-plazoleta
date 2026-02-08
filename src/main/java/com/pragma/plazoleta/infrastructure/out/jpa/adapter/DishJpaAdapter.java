package com.pragma.plazoleta.infrastructure.out.jpa.adapter;

import com.pragma.plazoleta.domain.model.Dish;
import com.pragma.plazoleta.domain.model.PageResult;
import com.pragma.plazoleta.domain.spi.IDishPersistencePort;
import com.pragma.plazoleta.infrastructure.out.jpa.entity.DishEntity;
import com.pragma.plazoleta.infrastructure.out.jpa.mapper.IDishEntityMapper;
import com.pragma.plazoleta.infrastructure.out.jpa.mapper.IDishPageMapper;
import com.pragma.plazoleta.infrastructure.out.jpa.repository.IDishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

@RequiredArgsConstructor
public class DishJpaAdapter implements IDishPersistencePort {

    private final IDishRepository dishRepository;
    private final IDishEntityMapper dishEntityMapper;
    private final IDishPageMapper dishPageMapper;

    @Override
    public Dish saveDish(Dish dish) {
        DishEntity dishEntity = dishRepository.save(dishEntityMapper.toEntity(dish));
        return dishEntityMapper.toDish(dishEntity);
    }

    @Override
    public Optional<Dish> findById(Long idDish) {
        return dishRepository.findById(idDish).map(dishEntityMapper::toDish);
    }

    @Override
    public PageResult<Dish> findByRestaurant(Long restaurantId, int page, int size, Long categoryId) {
        Pageable pageable = PageRequest.of(page, size);

        Page<DishEntity> dishPage;

        if (categoryId == null) {
            dishPage = dishRepository.findByRestaurantIdAndActiveTrue(
                    restaurantId,
                    pageable
            );
        } else {
            dishPage = dishRepository.findByRestaurantIdAndCategory_IdAndActiveTrue(
                    restaurantId,
                    categoryId,
                    pageable
            );
        }

        return dishPageMapper.toDomain(dishPage, dishEntityMapper);
    }


}
