package com.pragma.plazoleta.domain.usecase;

import com.pragma.plazoleta.domain.exception.DomainException;
import com.pragma.plazoleta.domain.exception.ErrorCode;
import com.pragma.plazoleta.domain.model.*;
import com.pragma.plazoleta.domain.spi.IDishPersistencePort;
import com.pragma.plazoleta.domain.spi.IRestaurantPersistencePort;
import com.pragma.plazoleta.domain.validator.DishDomainValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DishUseCaseTest {

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
    void shouldThrowRestaurantOwnershipExceptionWhenRestaurantDoesNotBelongToOwner() {
        Dish dish = buildDish(10L);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(10L);
        restaurant.setOwnerId(99L);

        when(restaurantPersistencePort.findById(10L))
                .thenReturn(Optional.of(restaurant));

        DomainException exception = assertThrows(
                DomainException.class,
                () -> dishUseCase.saveDish(
                        dish,
                        5L
                )
        );
        assertEquals(ErrorCode.FORBIDDEN, exception.getErrorCode());
        assertEquals(
                "You are not allowed to create dishes for this restaurant",
                exception.getMessage()
        );

        verify(restaurantPersistencePort).findById(10L);
        verifyNoInteractions(dishPersistencePort);
    }

    @Test
    void shouldSaveDishSuccessfullyWhenValidOwnerAndRestaurant() {
        Dish dish = buildDish(10L);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(10L);
        restaurant.setOwnerId(5L);

        when(restaurantPersistencePort.findById(10L))
                .thenReturn(Optional.of(restaurant));

        dishUseCase.saveDish(dish, 5L);

        verify(restaurantPersistencePort).findById(10L);
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
                .thenReturn(Optional.of(restaurant));

        when(dishPersistencePort.findById(dishId))
                .thenReturn(Optional.empty());

        DomainException exception = assertThrows(
                DomainException.class,
                () -> dishUseCase.updateDish(
                        restaurantId,
                        dishId,
                        30000,
                        "desc",
                        5L
                )
        );
        assertEquals(ErrorCode.DATA_NOT_FOUND, exception.getErrorCode());
        assertEquals("Dish not found", exception.getMessage());

        verify(dishPersistencePort).findById(dishId);
        verify(dishPersistencePort, never()).saveDish(any());
    }

    @Test
    void shouldThrowUnauthorizedWhenUpdatingDishWithNonOwnerRole() {
        Long restaurantId = 10L;
        Long dishId = 1L;
        Long userId = 5L;
        Long ownerId = 99L;
        Integer price = 30000;
        String description = "desc";

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        restaurant.setOwnerId(ownerId);

        when(restaurantPersistencePort.findById(restaurantId))
                .thenReturn(Optional.of(restaurant));

        DomainException exception = assertThrows(
                DomainException.class,
                () -> dishUseCase.updateDish(
                        restaurantId,
                        dishId,
                        price,
                        description,
                        userId
                )
        );

        assertEquals(ErrorCode.FORBIDDEN, exception.getErrorCode());
        assertEquals(
                "You are not allowed to create dishes for this restaurant",
                exception.getMessage()
        );

        verify(restaurantPersistencePort).findById(restaurantId);
        verifyNoInteractions(dishPersistencePort);
    }

    @Test
    void shouldThrowExceptionWhenRestaurantNotFoundDuringUpdate() {
        when(restaurantPersistencePort.findById(10L))
                .thenReturn(Optional.empty());

        DomainException exception = assertThrows(
                DomainException.class,
                () -> dishUseCase.updateDish(
                        10L,
                        1L,
                        30000,
                        "desc",
                        5L
                )
        );

        assertEquals(ErrorCode.DATA_NOT_FOUND, exception.getErrorCode());
        assertEquals("Restaurant not found", exception.getMessage());

        verify(restaurantPersistencePort).findById(10L);
        verifyNoInteractions(dishPersistencePort);
    }

    @Test
    void shouldThrowForbiddenWhenRestaurantDoesNotBelongToOwnerDuringUpdate() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(10L);
        restaurant.setOwnerId(99L); // dueño distinto

        when(restaurantPersistencePort.findById(10L))
                .thenReturn(Optional.of(restaurant));

        DomainException exception = assertThrows(
                DomainException.class,
                () -> dishUseCase.updateDish(
                        10L,
                        1L,
                        30000,
                        "desc",
                        5L
                )
        );

        assertEquals(ErrorCode.FORBIDDEN, exception.getErrorCode());
        assertEquals(
                "You are not allowed to create dishes for this restaurant",
                exception.getMessage()
        );

        verify(restaurantPersistencePort).findById(10L);
        verifyNoInteractions(dishPersistencePort);
    }

    @Test
    void shouldThrowExceptionWhenDishNotFound1() {
        Long dishId = 1L;

        when(dishPersistencePort.findById(dishId))
                .thenReturn(Optional.empty());

        DomainException exception = assertThrows(
                DomainException.class,
                () -> dishUseCase.updateDishStatus(true, 1L, dishId)
        );

        assertEquals(ErrorCode.DATA_NOT_FOUND, exception.getErrorCode());
        assertEquals("Dish not found", exception.getMessage());

        verify(dishPersistencePort).findById(dishId);
        verifyNoMoreInteractions(dishPersistencePort);
        verifyNoInteractions(restaurantPersistencePort);
    }

    @Test
    void shouldThrowExceptionWhenRestaurantNotFoundOnUpdateDishStatus() {
        Long dishId = 1L;
        Long restaurantId = 10L;
        Long userId = 5L;

        Dish dish = new Dish();
        dish.setId(dishId);
        dish.setRestaurantId(restaurantId);

        when(dishPersistencePort.findById(dishId))
                .thenReturn(Optional.of(dish));

        when(restaurantPersistencePort.findById(restaurantId))
                .thenReturn(Optional.empty());

        DomainException exception = assertThrows(
                DomainException.class,
                () -> dishUseCase.updateDishStatus(true, userId, dishId)
        );

        assertEquals(ErrorCode.DATA_NOT_FOUND, exception.getErrorCode());
        assertEquals("Restaurant not found", exception.getMessage());

        verify(dishPersistencePort).findById(dishId);
        verify(restaurantPersistencePort).findById(restaurantId);
        verify(dishPersistencePort, never()).saveDish(any());
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotOwnerOnUpdateDishStatus() {
        Long dishId = 1L;
        Long restaurantId = 10L;
        Long ownerId = 99L;
        Long userId = 5L;

        Dish dish = new Dish();
        dish.setId(dishId);
        dish.setRestaurantId(restaurantId);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        restaurant.setOwnerId(ownerId);

        when(dishPersistencePort.findById(dishId))
                .thenReturn(Optional.of(dish));

        when(restaurantPersistencePort.findById(restaurantId))
                .thenReturn(Optional.of(restaurant));

        DomainException exception = assertThrows(
                DomainException.class,
                () -> dishUseCase.updateDishStatus(false, userId, dishId)
        );

        assertEquals(ErrorCode.FORBIDDEN, exception.getErrorCode());
        assertEquals(
                "You are not allowed to modify dishes of this restaurant",
                exception.getMessage()
        );

        verify(dishPersistencePort).findById(dishId);
        verify(restaurantPersistencePort).findById(restaurantId);
        verify(dishPersistencePort, never()).saveDish(any());
    }

    @Test
    void shouldSaveDishEvenWhenStatusIsTheSame() {
        Long dishId = 1L;
        Long restaurantId = 10L;
        Long userId = 5L;

        Dish dish = new Dish();
        dish.setId(dishId);
        dish.setRestaurantId(restaurantId);
        dish.setActive(true);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        restaurant.setOwnerId(userId);

        when(dishPersistencePort.findById(dishId))
                .thenReturn(Optional.of(dish));

        when(restaurantPersistencePort.findById(restaurantId))
                .thenReturn(Optional.of(restaurant));

        dishUseCase.updateDishStatus(true, userId, dishId);

        assertTrue(dish.isActive());

        verify(dishPersistencePort).findById(dishId);
        verify(restaurantPersistencePort).findById(restaurantId);
        verify(dishPersistencePort).saveDish(dish);
    }

    @Test
    void shouldThrowExceptionWhenRoleIsNotClient() {
        DomainException exception = assertThrows(
                DomainException.class,
                () -> dishUseCase.listDishesByRestaurant(
                        1L,
                        0,
                        10,
                        1L
                )
        );

        assertEquals(ErrorCode.DATA_NOT_FOUND, exception.getErrorCode());

        verify(restaurantPersistencePort).findById(1L);
        verifyNoInteractions(dishPersistencePort);
    }

    @Test
    void shouldThrowExceptionWhenRestaurantNotFound() {
        Long restaurantId = 1L;

        when(restaurantPersistencePort.findById(restaurantId))
                .thenReturn(Optional.empty());

        DomainException exception = assertThrows(
                DomainException.class,
                () -> dishUseCase.listDishesByRestaurant(
                        restaurantId,
                        0,
                        10,
                        1L
                )
        );

        assertEquals(ErrorCode.DATA_NOT_FOUND, exception.getErrorCode());

        verify(restaurantPersistencePort).findById(restaurantId);
        verifyNoInteractions(dishPersistencePort);
    }
    @Test
    void shouldUpdateDishPriceAndDescriptionAndSave() {
        // given
        Long restaurantId = 1L;
        Long dishId = 2L;
        Long userId = 10L;
        Integer price = 15000;
        String description = "Updated description";

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        restaurant.setOwnerId(userId);

        Dish dish = new Dish();
        dish.setId(dishId);

        when(restaurantPersistencePort.findById(restaurantId))
                .thenReturn(Optional.of(restaurant));

        when(dishPersistencePort.findById(dishId))
                .thenReturn(Optional.of(dish));

        try (MockedStatic<DishDomainValidator> validator =
                     Mockito.mockStatic(DishDomainValidator.class)) {

            dishUseCase.updateDish(
                    restaurantId,
                    dishId,
                    price,
                    description,
                    userId
            );

            assertEquals(price, dish.getPrice());
            assertEquals(description, dish.getDescription());

            validator.verify(() ->
                    DishDomainValidator.validateForUpdate(dish)
            );

            verify(dishPersistencePort).saveDish(dish);
        }
    }

    @Test
    void shouldReturnDishesFromPersistencePort() {
        // given
        Long restaurantId = 1L;
        int page = 0;
        int size = 10;
        Long categoryId = 2L;

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);

        Dish dish = new Dish();
        dish.setId(100L);

        PageResult<Dish> expectedResult =
                new PageResult<>(
                        List.of(dish),
                        page,
                        size,
                        1L
                );

        when(restaurantPersistencePort.findById(restaurantId))
                .thenReturn(Optional.of(restaurant));

        when(dishPersistencePort.findByRestaurant(
                restaurantId,
                page,
                size,
                categoryId
        )).thenReturn(expectedResult);

        // when
        PageResult<Dish> result =
                dishUseCase.listDishesByRestaurant(
                        restaurantId,
                        page,
                        size,
                        categoryId
                );

        // then
        assertNotNull(result);
        assertEquals(expectedResult, result);

        verify(dishPersistencePort).findByRestaurant(
                restaurantId,
                page,
                size,
                categoryId
        );
    }


    private Dish buildDish(Long restaurantId) {
        Category category = new Category(
                1L,
                "MAIN_COURSE",
                "Platos principales"
        );

        Dish dish = new Dish();
        dish.setId(null);
        dish.setName("Pizza");
        dish.setPrice(25000);
        dish.setDescription("Delicious pizza");
        dish.setImageUrl("http://image.com/pizza.jpg");
        dish.setCategory(category);
        dish.setRestaurantId(restaurantId);
        dish.setActive(true);

        return dish;
    }
}
