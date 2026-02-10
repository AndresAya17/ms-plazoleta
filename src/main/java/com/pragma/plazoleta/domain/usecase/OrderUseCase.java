package com.pragma.plazoleta.domain.usecase;

import com.pragma.plazoleta.domain.api.IOrderServicePort;
import com.pragma.plazoleta.domain.exception.DomainException;
import com.pragma.plazoleta.domain.exception.ErrorCode;
import com.pragma.plazoleta.domain.model.Dish;
import com.pragma.plazoleta.domain.model.Order;
import com.pragma.plazoleta.domain.model.OrderItem;
import com.pragma.plazoleta.domain.spi.IDishPersistencePort;
import com.pragma.plazoleta.domain.spi.IOrderPersistencePort;
import com.pragma.plazoleta.domain.spi.IRestaurantPersistencePort;

public class OrderUseCase implements IOrderServicePort {

    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IDishPersistencePort dishPersistencePort;
    private final IOrderPersistencePort orderPersistencePort;

    public OrderUseCase(IRestaurantPersistencePort restaurantPersistencePort, IDishPersistencePort dishPersistencePort, IOrderPersistencePort orderPersistencePort){
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.dishPersistencePort = dishPersistencePort;
        this.orderPersistencePort = orderPersistencePort;
    }

    @Override
    public Order saveOrder(Order order, Long userId) {
        restaurantPersistencePort.findById(order.getRestaurantId())
                .orElseThrow(() -> new DomainException(ErrorCode.DATA_NOT_FOUND, "Restaurant not found"));
        for (OrderItem item : order.getItems()) {

            Dish dish = dishPersistencePort.findById(item.getDishId())
                    .orElseThrow(() -> new DomainException(
                            ErrorCode.INVALID_DISH,
                            "Dish not found"));

            if(!dish.isActive()){
                throw new DomainException(
                        ErrorCode.INVALID_DISH,
                        "Dish is inactive"
                );
            }

            if (!dish.getRestaurantId().equals(order.getRestaurantId())) {
                throw new DomainException(
                        ErrorCode.INVALID_DISH,
                        "Dish does not belong to the restaurant"
                );
            }
        }
        order.setClientId(userId);
        return orderPersistencePort.saveOrder(order);
    }
}
