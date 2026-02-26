package com.pragma.plazoleta.domain.usecase;

import com.pragma.plazoleta.domain.api.IRestaurantServicePort;
import com.pragma.plazoleta.domain.exception.DomainException;
import com.pragma.plazoleta.domain.exception.ErrorCode;
import com.pragma.plazoleta.domain.model.EmployeeRestaurant;
import com.pragma.plazoleta.domain.model.PageResult;
import com.pragma.plazoleta.domain.model.Restaurant;
import com.pragma.plazoleta.domain.spi.IEmployeeRestaurantPersistencePort;
import com.pragma.plazoleta.domain.spi.IRestaurantPersistencePort;

public class RestaurantUseCase implements IRestaurantServicePort {

    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort;

    public RestaurantUseCase(IRestaurantPersistencePort restaurantPersistencePort, IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort){
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.employeeRestaurantPersistencePort = employeeRestaurantPersistencePort;
    }

    @Override
    public void saveRestaurant(Restaurant restaurant) {
        restaurantPersistencePort.saveRestaurant(restaurant);
    }

    @Override
    public PageResult<Restaurant> listRestaurants(int page, int size) {
        return restaurantPersistencePort.listRestaurants(page, size);
    }

    @Override
    public void validateOwner(Long restaurantId, Long userId) {
        Restaurant restaurant = restaurantPersistencePort.findById(
                restaurantId).orElseThrow(() ->
                new DomainException(ErrorCode.DATA_NOT_FOUND, "Restaurant not found"));

        if(!restaurant.getOwnerId().equals(userId)){
            throw new DomainException(ErrorCode.UNAUTHORIZED, "The user is not the owner of this restaurant");
        }
    }

    @Override
    public void assignEmployeeToRestaurant(EmployeeRestaurant employeeRestaurant) {
        employeeRestaurantPersistencePort.save(employeeRestaurant);
    }
}
