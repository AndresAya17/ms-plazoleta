package com.pragma.plazoleta.infrastructure.out.jpa.repository;

import com.pragma.plazoleta.infrastructure.out.jpa.entity.RestaurantEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRestaurantRepository extends JpaRepository<RestaurantEntity, Long> {
    Page<RestaurantEntity> findAllByOrderByNameAsc(Pageable pageable);
}
