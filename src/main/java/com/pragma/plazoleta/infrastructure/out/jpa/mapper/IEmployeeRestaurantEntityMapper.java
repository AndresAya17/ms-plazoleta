package com.pragma.plazoleta.infrastructure.out.jpa.mapper;

import com.pragma.plazoleta.domain.model.EmployeeRestaurant;
import com.pragma.plazoleta.infrastructure.out.jpa.entity.EmployeeRestaurantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface IEmployeeRestaurantEntityMapper {
    EmployeeRestaurantEntity toEntity(EmployeeRestaurant employeeRestaurant);
}
