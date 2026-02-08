package com.pragma.plazoleta.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IEmployeeRestaurantRequestMapper {
    EmployeeForRestaurantCommand toEmployee(RestaurantEmployeeRequestDto restaurantEmployeeRequestDto, Long restaurantId, Long ownerId);
}
