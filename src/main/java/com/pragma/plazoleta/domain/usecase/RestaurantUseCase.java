package com.pragma.plazoleta.domain.usecase;

import com.pragma.plazoleta.domain.api.IRestaurantServicePort;
import com.pragma.plazoleta.domain.constants.DomainConstants;
import com.pragma.plazoleta.domain.exception.DomainException;
import com.pragma.plazoleta.domain.exception.ErrorCode;
import com.pragma.plazoleta.domain.model.EmployeeRestaurant;
import com.pragma.plazoleta.domain.model.PageResult;
import com.pragma.plazoleta.domain.model.Restaurant;
import com.pragma.plazoleta.domain.spi.IEmployeeRestaurantPersistencePort;
import com.pragma.plazoleta.domain.spi.IRestaurantPersistencePort;

import java.util.List;

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
                new DomainException(ErrorCode.DATA_NOT_FOUND, DomainConstants.RNF));

        if(!restaurant.getOwnerId().equals(userId)){
            throw new DomainException(ErrorCode.UNAUTHORIZED, DomainConstants.UNO);
        }
    }

    @Override
    public void assignEmployeeToRestaurant(EmployeeRestaurant employeeRestaurant) {
        employeeRestaurantPersistencePort.save(employeeRestaurant);
    }

    @Override
    public List<Long> getEmployeeRestaurant(Long restaurantId, Long userId) {
        Restaurant restaurant = restaurantPersistencePort.findById(
                restaurantId).orElseThrow(() ->
                new DomainException(ErrorCode.DATA_NOT_FOUND, DomainConstants.RNF));

        if(!restaurant.getOwnerId().equals(userId)){
            throw new DomainException(ErrorCode.UNAUTHORIZED, DomainConstants.UNO);
        }
        return employeeRestaurantPersistencePort.findEmployeeByRestaurantId(restaurantId);
    }
}
