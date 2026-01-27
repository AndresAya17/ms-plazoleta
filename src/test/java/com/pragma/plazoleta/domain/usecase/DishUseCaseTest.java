package com.pragma.plazoleta.domain.usecase;

import com.pragma.plazoleta.domain.exception.RestaurantOwnershipException;
import com.pragma.plazoleta.domain.exception.UserNotOwnerException;
import com.pragma.plazoleta.domain.model.Dish;
import com.pragma.plazoleta.domain.model.DishCategory;
import com.pragma.plazoleta.domain.model.Restaurant;
import com.pragma.plazoleta.domain.spi.IDishPersistencePort;
import com.pragma.plazoleta.domain.spi.IRestaurantPersistencePort;
import com.pragma.plazoleta.domain.spi.IUserOwnerValidationPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class DishUseCaseTest {

    private IDishPersistencePort dishPersistencePort;
    private IRestaurantPersistencePort restaurantPersistencePort;
    private IUserOwnerValidationPort userOwnerValidationPort;

    private DishUseCase dishUseCase;

    @BeforeEach
    void setUp() {
        dishPersistencePort = mock(IDishPersistencePort.class);
        restaurantPersistencePort = mock(IRestaurantPersistencePort.class);
        userOwnerValidationPort = mock(IUserOwnerValidationPort.class);

        dishUseCase = new DishUseCase(
                dishPersistencePort,
                restaurantPersistencePort,
                userOwnerValidationPort
        );
    }

    @Test
    void shouldThrowUserNotOwnerExceptionWhenUserIsNotOwner() {
        // arrange
        Dish dish = buildDish(5L, 10L);

        when(userOwnerValidationPort.isOwner(5L)).thenReturn(false);

        // act & assert
        assertThrows(
                UserNotOwnerException.class,
                () -> dishUseCase.saveDish(dish)
        );

        verify(userOwnerValidationPort).isOwner(5L);
        verifyNoInteractions(restaurantPersistencePort, dishPersistencePort);
    }

    @Test
    void shouldThrowRestaurantOwnershipExceptionWhenRestaurantDoesNotBelongToOwner() {
        // arrange
        Dish dish = buildDish(5L, 10L);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(10L);
        restaurant.setOwnerId(99L); // 👈 dueño distinto

        when(userOwnerValidationPort.isOwner(5L)).thenReturn(true);
        when(restaurantPersistencePort.findById(10L)).thenReturn(restaurant);

        // act & assert
        assertThrows(
                RestaurantOwnershipException.class,
                () -> dishUseCase.saveDish(dish)
        );

        verify(userOwnerValidationPort).isOwner(5L);
        verify(restaurantPersistencePort).findById(10L);
        verifyNoInteractions(dishPersistencePort);
    }

    @Test
    void shouldSaveDishSuccessfullyWhenValidOwnerAndRestaurant() {
        // arrange
        Dish dish = buildDish(5L, 10L);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(10L);
        restaurant.setOwnerId(5L); // 👈 dueño correcto

        when(userOwnerValidationPort.isOwner(5L)).thenReturn(true);
        when(restaurantPersistencePort.findById(10L)).thenReturn(restaurant);

        // act
        dishUseCase.saveDish(dish);

        // assert
        verify(userOwnerValidationPort).isOwner(5L);
        verify(restaurantPersistencePort).findById(10L);
        verify(dishPersistencePort).saveDish(dish);
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
