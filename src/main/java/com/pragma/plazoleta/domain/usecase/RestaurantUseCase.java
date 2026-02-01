package com.pragma.plazoleta.domain.usecase;

import com.pragma.plazoleta.application.dto.request.EmployeeUserRequestDto;
import com.pragma.plazoleta.domain.api.IRestaurantServicePort;
import com.pragma.plazoleta.domain.exception.*;
import com.pragma.plazoleta.domain.spi.IUserPersistencePort;
import com.pragma.plazoleta.domain.model.EmployeeForRestaurantCommand;
import com.pragma.plazoleta.domain.model.EmployeeRestaurant;
import com.pragma.plazoleta.domain.model.Restaurant;
import com.pragma.plazoleta.domain.model.Rol;
import com.pragma.plazoleta.domain.spi.IEmployeeRestaurantPersistencePort;
import com.pragma.plazoleta.domain.spi.IRestaurantPersistencePort;

public class RestaurantUseCase implements IRestaurantServicePort {

    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IUserPersistencePort userServicePort;
    private final IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort;

    public RestaurantUseCase(IRestaurantPersistencePort restaurantPersistencePort, IUserPersistencePort userServicePort, IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort){
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.userServicePort = userServicePort;
        this.employeeRestaurantPersistencePort = employeeRestaurantPersistencePort;
    }

    @Override
    public void saveRestaurant(Restaurant restaurant, Long userId, String rol) {
        if (!Rol.ADMINISTRADOR.name().equals(rol)){
            throw new DomainException(ErrorCode.UNAUTHORIZED, "Only a admin can create dishes");
        }
        restaurantPersistencePort.saveRestaurant(restaurant);
    }

    @Override
    public void saveEmployee(EmployeeForRestaurantCommand employee, String rol) {
        employee.validateEmployee();
        if (!rol.equals(Rol.PROPIETARIO.name())) {
            throw new DomainException(ErrorCode.UNAUTHORIZED, "Only a restaurant owner can create employees");
        }

        Restaurant restaurant = restaurantPersistencePort
                .findById(employee.getRestaurantId())
                .orElseThrow(() -> new DomainException(ErrorCode.DATA_NOT_FOUND, "Restaurant not found"));

        if (!restaurant.getOwnerId().equals(employee.getOwnerId())) {
            throw new DomainException(ErrorCode.FORBIDDEN, "User role is not authorized to create employees");
        }

        EmployeeUserRequestDto employeeUserRequestDto =
                new EmployeeUserRequestDto(
                        employee.getFirstName(),
                        employee.getLastName(),
                        employee.getDocumentNumber(),
                        employee.getPhoneNumber(),
                        employee.getEmail(),
                        employee.getPassword()
                );

        Long employeeUserId = userServicePort.createEmployee(employeeUserRequestDto);

        employeeRestaurantPersistencePort.save(
                new EmployeeRestaurant(employeeUserId, employee.getRestaurantId())
        );
    }
}
