package com.pragma.plazoleta.infrastructure.out.jpa.repository;

import com.pragma.plazoleta.infrastructure.out.jpa.entity.EmployeeRestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRestaurantRepository extends JpaRepository<EmployeeRestaurantEntity, Long> {
    Optional<EmployeeRestaurantEntity> findByEmployeeUserId(Long employeeUserId);

    @Query("SELECT er.employeeUserId FROM EmployeeRestaurantEntity er WHERE er.restaurantId = :restaurantId")
    List<Long> findEmployeeIdsByRestaurantId(@Param("restaurantId") Long restaurantId);
}
