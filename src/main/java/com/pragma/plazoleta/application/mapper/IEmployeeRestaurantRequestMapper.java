package com.pragma.plazoleta.application.mapper;

import com.pragma.plazoleta.application.dto.request.RestaurantEmployeeRequestDto;
import com.pragma.plazoleta.domain.model.EmployeeForRestaurantCommand;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IEmployeeRestaurantRequestMapper {
    EmployeeForRestaurantCommand toEmployee(RestaurantEmployeeRequestDto restaurantEmployeeRequestDto, Long restaurantId, Long ownerId);
}
