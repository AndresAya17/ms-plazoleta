package com.pragma.plazoleta.domain.usecase;

import com.pragma.plazoleta.domain.exception.DataNotFoundException;
import com.pragma.plazoleta.domain.exception.RestaurantOwnershipException;
import com.pragma.plazoleta.domain.exception.UserNotRolException;
import com.pragma.plazoleta.domain.model.Dish;
import com.pragma.plazoleta.domain.model.DishCategory;
import com.pragma.plazoleta.domain.model.Restaurant;
import com.pragma.plazoleta.domain.model.Rol;
import com.pragma.plazoleta.domain.spi.IDishPersistencePort;
import com.pragma.plazoleta.domain.spi.IRestaurantPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DishUseCaseTest {

    private IDishPersistencePort dishPersistencePort;
    private IRestaurantPersistencePort restaurantPersistencePort;
    private DishUseCase dishUseCase;

    @BeforeEach
    void setUp() {
        dishPersistencePort = mock(IDishPersistencePort.class);
        restaurantPersistencePort = mock(IRestaurantPersistencePort.class);

        dishUseCase = new DishUseCase(
                dishPersistencePort,
                restaurantPersistencePort
        );
    }

    @Test
    void shouldThrowUserNotRolExceptionWhenUserIsNotProprietary() {
        Dish dish = buildDish(5L, 10L);

        assertThrows(
                UserNotRolException.class,
                () -> dishUseCase.saveDish(dish, 5L, Rol.ADMINISTRADOR.name())
        );

        verifyNoInteractions(restaurantPersistencePort, dishPersistencePort);
    }

    @Test
    void shouldThrowRestaurantOwnershipExceptionWhenRestaurantDoesNotBelongToOwner() {
        Dish dish = buildDish(5L, 10L);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(10L);
        restaurant.setOwnerId(99L); // dueño distinto

        when(restaurantPersistencePort.findById(10L))
                .thenReturn(restaurant);

        assertThrows(
                RestaurantOwnershipException.class,
                () -> dishUseCase.saveDish(dish, 5L, Rol.PROPIETARIO.name())
        );

        verify(restaurantPersistencePort).findById(10L);
        verifyNoInteractions(dishPersistencePort);
    }

    @Test
    void shouldSaveDishSuccessfullyWhenValidOwnerAndRestaurant() {
        Dish dish = buildDish(5L, 10L);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(10L);
        restaurant.setOwnerId(5L);

        when(restaurantPersistencePort.findById(10L))
                .thenReturn(restaurant);

        dishUseCase.saveDish(dish, 5L, Rol.PROPIETARIO.name());

        verify(restaurantPersistencePort).findById(10L);
        verify(dishPersistencePort).saveDish(dish);
    }

    // ---------- UPDATE DISH ----------

    @Test
    void shouldUpdateDishSuccessfully() {
        Long restaurantId = 10L;
        Long dishId = 1L;
        Long userId = 5L;

        Dish.DishInfo dishInfo = new Dish.DishInfo(
                dishId,
                "Pizza",
                25000,
                "Old description",
                "http://img.com/pizza.png",
                DishCategory.MAIN_COURSE
        );
        Dish dish = new Dish(dishInfo, restaurantId, userId);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        restaurant.setOwnerId(5L);

        when(restaurantPersistencePort.findById(restaurantId))
                .thenReturn(restaurant);

        when(dishPersistencePort.findById(dishId))
                .thenReturn(Optional.of(dish));

        dishUseCase.updateDish(
                restaurantId,
                dishId,
                30000,
                "Updated description",
                5L,
                Rol.PROPIETARIO.name()
        );

        assertEquals(30000, dish.getPrice());
        assertEquals("Updated description", dish.getDescription());

        verify(dishPersistencePort).saveDish(dish);
    }

    @Test
    void shouldThrowExceptionWhenDishNotFound() {
        Long restaurantId = 10L;
        Long dishId = 99L;

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        restaurant.setOwnerId(5L);

        when(restaurantPersistencePort.findById(restaurantId))
                .thenReturn(restaurant);

        when(dishPersistencePort.findById(dishId))
                .thenReturn(Optional.empty());

        assertThrows(
                DataNotFoundException.class,
                () -> dishUseCase.updateDish(
                        restaurantId,
                        dishId,
                        30000,
                        "desc",
                        5L,
                        Rol.PROPIETARIO.name()
                )
        );

        verify(dishPersistencePort).findById(dishId);
        verify(dishPersistencePort, never()).saveDish(any());
    }

    // ---------- UTIL ----------

    private Dish buildDish(Long ownerId, Long restaurantId) {
        Dish.DishInfo info = new Dish.DishInfo(
                null,
                "Pizza",
                25000,
                "Delicious pizza",
                "http://image.com/pizza.jpg",
                DishCategory.MAIN_COURSE
        );

        return new Dish(info, restaurantId, ownerId);
    }
}
