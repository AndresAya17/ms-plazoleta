package com.pragma.plazoleta.infrastructure.out.jpa.repository;

import com.pragma.plazoleta.domain.model.DishCategory;
import com.pragma.plazoleta.infrastructure.out.jpa.entity.DishEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDishRepository extends JpaRepository<DishEntity, Long> {
    Page<DishEntity> findByRestaurantIdAndActiveTrue(
            Long restaurantId,
            Pageable pageable
    );

    Page<DishEntity> findByRestaurantIdAndDishCategoryAndActiveTrue(
            Long restaurantId,
            DishCategory category,
            Pageable pageable
    );
}
