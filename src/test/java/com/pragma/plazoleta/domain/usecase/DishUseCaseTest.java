package com.pragma.plazoleta.domain.usecase;

import com.pragma.plazoleta.domain.exception.DomainException;
import com.pragma.plazoleta.domain.exception.ErrorCode;
import com.pragma.plazoleta.domain.model.*;
import com.pragma.plazoleta.domain.spi.IDishPersistencePort;
import com.pragma.plazoleta.domain.spi.IRestaurantPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    void shouldThrowUserNotRolExceptionWhenUserIsNotProprietary() {
        Dish dish = buildDish(5L, 10L);

        DomainException exception = assertThrows(
                DomainException.class,
                () -> dishUseCase.saveDish(
                        dish,
                        5L,
                        Rol.ADMINISTRADOR.name()
                )
        );

        assertEquals(ErrorCode.UNAUTHORIZED, exception.getErrorCode());
        assertEquals(
                "Only a restaurant owner can create dishes",
                exception.getMessage()
        );

        verifyNoInteractions(restaurantPersistencePort, dishPersistencePort);
    }

    @Test
    void shouldThrowRestaurantOwnershipExceptionWhenRestaurantDoesNotBelongToOwner() {
        Dish dish = buildDish(5L, 10L);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(10L);
        restaurant.setOwnerId(99L);

        when(restaurantPersistencePort.findById(10L))
                .thenReturn(Optional.of(restaurant));

        DomainException exception = assertThrows(
                DomainException.class,
                () -> dishUseCase.saveDish(
                        dish,
                        5L,
                        Rol.PROPIETARIO.name()
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
        Dish dish = buildDish(5L, 10L);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(10L);
        restaurant.setOwnerId(5L);

        when(restaurantPersistencePort.findById(10L))
                .thenReturn(Optional.of(restaurant));

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
                .thenReturn(Optional.of(restaurant));

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
                        5L,
                        Rol.PROPIETARIO.name()
                )
        );
        assertEquals(ErrorCode.DATA_NOT_FOUND, exception.getErrorCode());
        assertEquals("Dish not found", exception.getMessage());

        verify(dishPersistencePort).findById(dishId);
        verify(dishPersistencePort, never()).saveDish(any());
    }

    @Test
    void shouldThrowUnauthorizedWhenUpdatingDishWithNonOwnerRole() {
        DomainException exception = assertThrows(
                DomainException.class,
                () -> dishUseCase.updateDish(
                        10L,
                        1L,
                        30000,
                        "desc",
                        5L,
                        Rol.ADMINISTRADOR.name()
                )
        );

        assertEquals(ErrorCode.UNAUTHORIZED, exception.getErrorCode());
        assertEquals(
                "Only a restaurant owner can create dishes",
                exception.getMessage()
        );

        verifyNoInteractions(restaurantPersistencePort, dishPersistencePort);
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
                        5L,
                        Rol.PROPIETARIO.name()
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
                        5L,
                        Rol.PROPIETARIO.name()
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
        // arrange
        Long dishId = 1L;

        when(dishPersistencePort.findById(dishId))
                .thenReturn(Optional.empty());

        // act
        DomainException exception = assertThrows(
                DomainException.class,
                () -> dishUseCase.updateDishStatus(true, 1L, "PROPIETARIO", dishId)
        );

        // assert
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
                () -> dishUseCase.updateDishStatus(true, userId, Rol.PROPIETARIO.name(), dishId)
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
                () -> dishUseCase.updateDishStatus(false, userId, Rol.PROPIETARIO.name(), dishId)
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

        dishUseCase.updateDishStatus(true, userId, Rol.PROPIETARIO.name(), dishId);

        assertTrue(dish.isActive());

        verify(dishPersistencePort).findById(dishId);
        verify(restaurantPersistencePort).findById(restaurantId);
        verify(dishPersistencePort).saveDish(dish);
    }

    @Test
    void shouldThrowExceptionWhenRoleIsNotClient() {
        // act & assert
        DomainException exception = assertThrows(
                DomainException.class,
                () -> dishUseCase.listDishesByRestaurant(
                        1L,
                        DishCategory.STARTER,
                        0,
                        10,
                        Rol.PROPIETARIO.name()
                )
        );

        assertEquals(ErrorCode.UNAUTHORIZED, exception.getErrorCode());

        verifyNoInteractions(restaurantPersistencePort);
        verifyNoInteractions(dishPersistencePort);
    }

    @Test
    void shouldThrowExceptionWhenRestaurantNotFound() {
        // arrange
        Long restaurantId = 1L;

        when(restaurantPersistencePort.findById(restaurantId))
                .thenReturn(Optional.empty());

        // act & assert
        DomainException exception = assertThrows(
                DomainException.class,
                () -> dishUseCase.listDishesByRestaurant(
                        restaurantId,
                        DishCategory.STARTER,
                        0,
                        10,
                        Rol.CLIENTE.name()
                )
        );

        assertEquals(ErrorCode.DATA_NOT_FOUND, exception.getErrorCode());

        verify(restaurantPersistencePort).findById(restaurantId);
        verifyNoInteractions(dishPersistencePort);
    }
    @Test
    void shouldReturnDishesWhenRoleIsClientAndRestaurantExists() {
        Long restaurantId = 1L;
        DishCategory category = DishCategory.STARTER;
        int page = 0;
        int size = 10;

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);

        PageResult<Dish> pageResult =
                new PageResult<>(
                        List.of(new Dish()),
                        page,
                        size,
                        1L
                );

        when(restaurantPersistencePort.findById(restaurantId))
                .thenReturn(Optional.of(restaurant));

        when(dishPersistencePort.findByRestaurant(
                restaurantId,
                category,
                page,
                size
        )).thenReturn(pageResult);

        PageResult<Dish> result =
                dishUseCase.listDishesByRestaurant(
                        restaurantId,
                        category,
                        page,
                        size,
                        Rol.CLIENTE.name()
                );

        assertNotNull(result);
        assertEquals(1, result.getContent().size());

        verify(restaurantPersistencePort).findById(restaurantId);
        verify(dishPersistencePort)
                .findByRestaurant(restaurantId, category, page, size);
    }

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
