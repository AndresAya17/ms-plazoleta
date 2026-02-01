package com.pragma.plazoleta.domain.usecase;

import com.pragma.plazoleta.application.dto.request.EmployeeUserRequestDto;
import com.pragma.plazoleta.domain.api.IRestaurantServicePort;
import com.pragma.plazoleta.domain.spi.IUserPersistencePort;
import com.pragma.plazoleta.domain.exception.DataNotFoundException;
import com.pragma.plazoleta.domain.exception.ForbiddenException;
import com.pragma.plazoleta.domain.exception.UnauthorizedException;
import com.pragma.plazoleta.domain.exception.UserNotRolException;
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
            throw new UserNotRolException();
        }
        restaurantPersistencePort.saveRestaurant(restaurant);
    }

    @Override
    public void saveEmployee(EmployeeForRestaurantCommand employee, String rol) {
        employee.validateEmployee();
        if (!rol.equals(Rol.PROPIETARIO.name())) {
            throw new UnauthorizedException();
        }

        Restaurant restaurant = restaurantPersistencePort
                .findById(employee.getRestaurantId())
                .orElseThrow(() -> new DataNotFoundException("Restaurant"));

        if (!restaurant.getOwnerId().equals(employee.getOwnerId())) {
            throw new ForbiddenException();
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
