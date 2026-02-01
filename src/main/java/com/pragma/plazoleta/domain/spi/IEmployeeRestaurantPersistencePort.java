package com.pragma.plazoleta.domain.spi;

import com.pragma.plazoleta.domain.model.EmployeeRestaurant;

public interface IEmployeeRestaurantPersistencePort {
    void save(EmployeeRestaurant employeeRestaurant);
}
