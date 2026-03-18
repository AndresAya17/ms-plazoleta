package com.pragma.plazoleta.domain.spi;

import com.pragma.plazoleta.domain.model.EmployeeRestaurant;

import java.util.List;
import java.util.Optional;

public interface IEmployeeRestaurantPersistencePort {
    void save(EmployeeRestaurant employeeRestaurant);
    Optional<Long> findRestaurantIdByEmployeeUserId(Long employeeUserId);
    List<Long> findEmployeeByRestaurantId (Long restaurantId);
}
