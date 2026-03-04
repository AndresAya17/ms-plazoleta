package com.pragma.plazoleta.infrastructure.out.jpa.repository;

import com.pragma.plazoleta.infrastructure.out.jpa.entity.DishEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IDishRepository extends JpaRepository<DishEntity, Long> {
    @Query("""
        SELECT d
            FROM DishEntity d
            WHERE d.restaurantId = :restaurantId
              AND d.active = true
              AND (:categoryId IS NULL OR d.category.id = :categoryId)
    """)
    Page<DishEntity> findByRestaurantAndOptionalCategory(
            @Param("restaurantId") Long restaurantId,
            @Param("categoryId") Long categoryId,
            Pageable pageable
    );
}
