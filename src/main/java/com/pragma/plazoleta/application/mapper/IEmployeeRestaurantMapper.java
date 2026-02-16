package com.pragma.plazoleta.application.mapper;

import com.pragma.plazoleta.application.dto.request.CreateEmployeeRestaurantRequestDto;
import com.pragma.plazoleta.domain.model.EmployeeRestaurant;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IEmployeeRestaurantMapper {
    EmployeeRestaurant toEmployee(CreateEmployeeRestaurantRequestDto employeeRestaurantRequestDto);
}
