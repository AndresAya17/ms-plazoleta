package com.pragma.plazoleta.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmployeeRestaurant {
    private Long employeeUserId;
    private Long restaurantId;
}
