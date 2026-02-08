package com.pragma.plazoleta.domain.usecase;

import com.pragma.plazoleta.domain.api.IRestaurantServicePort;
import com.pragma.plazoleta.domain.exception.*;
import com.pragma.plazoleta.domain.spi.IUserPersistencePort;
import com.pragma.plazoleta.domain.model.Restaurant;
import com.pragma.plazoleta.domain.model.Rol;
import com.pragma.plazoleta.domain.spi.IEmployeeRestaurantPersistencePort;
import com.pragma.plazoleta.domain.spi.IRestaurantPersistencePort;

import java.util.List;

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
    public List<Restaurant> listRestaurants(int page, int size, String rol) {
        if (!rol.equals(Rol.CLIENTE.name())) {
            throw new DomainException(ErrorCode.UNAUTHORIZED, "Only clients can list restaurants");
        }
        return restaurantPersistencePort.listRestaurants(page, size);
    }
}
