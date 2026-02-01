package com.pragma.plazoleta.domain.model;

import com.pragma.plazoleta.domain.exception.DomainException;
import com.pragma.plazoleta.domain.exception.InvalidEmployeeRestaurant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmployeeRestaurant {
    private Long employeeUserId;
    private Long restaurantId;

    public EmployeeRestaurant(Long employeeUserId, Long restaurantId) {
        this.employeeUserId = employeeUserId;
        this.restaurantId = restaurantId;
        validate();
    }

    public void validate() {
        validateEmployeeUserId();
        validateRestaurantId();
    }

    private void validateEmployeeUserId() {
        if (employeeUserId == null || employeeUserId <= 0) {
            throw new InvalidEmployeeRestaurant("Employee user id is invalid") {
            };
        }
    }

    private void validateRestaurantId() {
        if (restaurantId == null || restaurantId <= 0) {
            throw new InvalidEmployeeRestaurant("Restaurant id is invalid");
        }
    }
}
