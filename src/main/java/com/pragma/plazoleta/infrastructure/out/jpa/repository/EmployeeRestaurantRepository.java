package com.pragma.plazoleta.infrastructure.out.jpa.repository;

import com.pragma.plazoleta.infrastructure.out.jpa.entity.EmployeeRestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRestaurantRepository extends JpaRepository<EmployeeRestaurantEntity, Long> {
}
