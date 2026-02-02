package com.pragma.plazoleta.domain.spi;

import com.pragma.plazoleta.application.dto.request.EmployeeUserRequestDto;

public interface IUserPersistencePort {
    Long createEmployee(EmployeeUserRequestDto employeeUserRequestDto);
}
