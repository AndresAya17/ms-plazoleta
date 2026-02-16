package com.pragma.plazoleta.application.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateEmployeeRestaurantRequestDto {

    @NotNull(message = "Employee user id is required")
    private Long employeeUserId;

    @NotNull(message = "Restaurant id is required")
    private Long restaurantId;

}
