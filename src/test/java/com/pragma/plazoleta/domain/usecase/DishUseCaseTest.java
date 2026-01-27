package com.pragma.plazoleta.domain.usecase;

import com.pragma.plazoleta.domain.exception.DishNotFoundException;
import com.pragma.plazoleta.domain.exception.RestaurantOwnershipException;
import com.pragma.plazoleta.domain.exception.UserNotRolException;
import com.pragma.plazoleta.domain.model.Dish;
import com.pragma.plazoleta.domain.model.DishCategory;
import com.pragma.plazoleta.domain.model.Restaurant;
import com.pragma.plazoleta.domain.model.Rol;
import com.pragma.plazoleta.domain.spi.IDishPersistencePort;
import com.pragma.plazoleta.domain.spi.IRestaurantPersistencePort;
import com.pragma.plazoleta.domain.spi.IUserValidationPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DishUseCaseTest {

    private IDishPersistencePort dishPersistencePort;
    private IRestaurantPersistencePort restaurantPersistencePort;
    private IUserValidationPort userValidationPort;

    private DishUseCase dishUseCase;

    @BeforeEach
    void setUp() {
        dishPersistencePort = mock(IDishPersistencePort.class);
        restaurantPersistencePort = mock(IRestaurantPersistencePort.class);
        userValidationPort = mock(IUserValidationPort.class);

        dishUseCase = new DishUseCase(
                dishPersistencePort,
                restaurantPersistencePort,
                userValidationPort
        );
    }

    @Test
    void shouldThrowUserNotRolExceptionWhenUserIsNotProprietary() {
        Dish dish = buildDish(5L, 10L);

        when(userValidationPort.getUserRol(5L)).thenReturn(Rol.ADMINISTRADOR);

        // act & assert
        assertThrows(
                UserNotRolException.class,
                () -> dishUseCase.saveDish(dish)
        );

        verify(userValidationPort).getUserRol(5L);
        verifyNoInteractions(restaurantPersistencePort, dishPersistencePort);
    }

    @Test
    void shouldThrowRestaurantOwnershipExceptionWhenRestaurantDoesNotBelongToOwner() {
        // arrange
        Dish dish = buildDish(5L, 10L);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(10L);
        restaurant.setOwnerId(99L); // dueño distinto

        when(userValidationPort.getUserRol(5L)).thenReturn(Rol.PROPIETARIO);
        when(restaurantPersistencePort.findById(10L)).thenReturn(restaurant);

        // act & assert
        assertThrows(
                RestaurantOwnershipException.class,
                () -> dishUseCase.saveDish(dish)
        );

        verify(userValidationPort).getUserRol(5L);
        verify(restaurantPersistencePort).findById(10L);
        verifyNoInteractions(dishPersistencePort);
    }


    @Test
    void shouldSaveDishSuccessfullyWhenValidOwnerAndRestaurant() {
        // arrange
        Dish dish = buildDish(5L, 10L);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(10L);
        restaurant.setOwnerId(5L); // dueño correcto

        when(userValidationPort.getUserRol(5L)).thenReturn(Rol.PROPIETARIO);
        when(restaurantPersistencePort.findById(10L)).thenReturn(restaurant);

        // act
        dishUseCase.saveDish(dish);

        // assert
        verify(userValidationPort).getUserRol(5L);
        verify(restaurantPersistencePort).findById(10L);
        verify(dishPersistencePort).saveDish(dish);
    }



    @Test
    void shouldUpdateDishSuccessfully() {
        // arrange
        Long dishId = 1L;
        Integer newPrice = 30000;
        String newDescription = "Updated description";

        Dish dish = new Dish(
                dishId,
                "Pizza",
                25000,
                "Old description",
                "http://img.com/pizza.png",
                DishCategory.MAIN_COURSE,
                10L,
                5L
        );

        when(dishPersistencePort.findById(dishId))
                .thenReturn(Optional.of(dish));

        // act
        dishUseCase.updateDish(dishId, newPrice, newDescription);

        // assert
        assertEquals(newPrice, dish.getPrice());
        assertEquals(newDescription, dish.getDescription());

        verify(dishPersistencePort).updateDish(dish);
        verify(dishPersistencePort).findById(dishId);
        verifyNoMoreInteractions(dishPersistencePort);
    }

    @Test
    void shouldThrowExceptionWhenDishNotFound() {
        // arrange
        Long dishId = 99L;

        when(dishPersistencePort.findById(dishId))
                .thenReturn(Optional.empty());

        // act & assert
        DishNotFoundException exception = assertThrows(
                DishNotFoundException.class,
                () -> dishUseCase.updateDish(dishId, 30000, "desc")
        );

        assertTrue(exception.getMessage().contains(dishId.toString()));

        verify(dishPersistencePort).findById(dishId);
        verify(dishPersistencePort, never()).updateDish(any());
    }

    private Dish buildDish(Long ownerId, Long restaurantId) {
        return new Dish(
                null,
                "Pizza",
                25000,
                "Delicious pizza",
                "http://image.com/pizza.jpg",
                DishCategory.MAIN_COURSE,
                restaurantId,
                ownerId
        );
    }
}
