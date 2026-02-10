package com.pragma.plazoleta.domain.validator;

import com.pragma.plazoleta.domain.exception.DomainException;
import com.pragma.plazoleta.domain.exception.ErrorCode;
import com.pragma.plazoleta.domain.model.Restaurant;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantDomainValidatorTest {

    private Restaurant buildValidRestaurant() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Restaurante Test");
        restaurant.setNit("123456789");
        restaurant.setAddress("Calle 123");
        restaurant.setPhoneNumber("+573001234567");
        restaurant.setLogoUrl("https://logo.com/logo.png");
        restaurant.setOwnerId(1L);
        return restaurant;
    }

    @Test
    void shouldNotThrowExceptionWhenRestaurantIsValid() {
        Restaurant restaurant = buildValidRestaurant();

        assertDoesNotThrow(() ->
                RestaurantDomainValidator.validateRestaurant(restaurant)
        );
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        Restaurant restaurant = buildValidRestaurant();
        restaurant.setName(null);

        DomainException exception = assertThrows(
                DomainException.class,
                () -> RestaurantDomainValidator.validateRestaurant(restaurant)
        );

        assertEquals(ErrorCode.INVALID_RESTAURANT, exception.getErrorCode());
    }

    @Test
    void shouldThrowExceptionWhenNameIsEmpty() {
        Restaurant restaurant = buildValidRestaurant();
        restaurant.setName(" ");

        DomainException exception = assertThrows(
                DomainException.class,
                () -> RestaurantDomainValidator.validateRestaurant(restaurant)
        );

        assertEquals(ErrorCode.INVALID_RESTAURANT, exception.getErrorCode());
    }

    @Test
    void shouldThrowExceptionWhenNitIsInvalid() {
        Restaurant restaurant = buildValidRestaurant();
        restaurant.setNit("ABC123");

        DomainException exception = assertThrows(
                DomainException.class,
                () -> RestaurantDomainValidator.validateRestaurant(restaurant)
        );

        assertEquals(ErrorCode.INVALID_RESTAURANT, exception.getErrorCode());
    }

    @Test
    void shouldThrowExceptionWhenAddressIsNull() {
        Restaurant restaurant = buildValidRestaurant();
        restaurant.setAddress(null);

        DomainException exception = assertThrows(
                DomainException.class,
                () -> RestaurantDomainValidator.validateRestaurant(restaurant)
        );

        assertEquals(ErrorCode.INVALID_RESTAURANT, exception.getErrorCode());
    }

    @Test
    void shouldThrowExceptionWhenAddressIsEmpty() {
        Restaurant restaurant = buildValidRestaurant();
        restaurant.setAddress(" ");

        DomainException exception = assertThrows(
                DomainException.class,
                () -> RestaurantDomainValidator.validateRestaurant(restaurant)
        );

        assertEquals(ErrorCode.INVALID_RESTAURANT, exception.getErrorCode());
    }

    @Test
    void shouldThrowExceptionWhenPhoneNumberIsInvalid() {
        Restaurant restaurant = buildValidRestaurant();
        restaurant.setPhoneNumber("12345");

        DomainException exception = assertThrows(
                DomainException.class,
                () -> RestaurantDomainValidator.validateRestaurant(restaurant)
        );

        assertEquals(ErrorCode.INVALID_RESTAURANT, exception.getErrorCode());
    }

    @Test
    void shouldThrowExceptionWhenLogoUrlIsNull() {
        Restaurant restaurant = buildValidRestaurant();
        restaurant.setLogoUrl(null);

        DomainException exception = assertThrows(
                DomainException.class,
                () -> RestaurantDomainValidator.validateRestaurant(restaurant)
        );

        assertEquals(ErrorCode.INVALID_RESTAURANT, exception.getErrorCode());
    }

    @Test
    void shouldThrowExceptionWhenLogoUrlIsEmpty() {
        Restaurant restaurant = buildValidRestaurant();
        restaurant.setLogoUrl(" ");

        DomainException exception = assertThrows(
                DomainException.class,
                () -> RestaurantDomainValidator.validateRestaurant(restaurant)
        );

        assertEquals(ErrorCode.INVALID_RESTAURANT, exception.getErrorCode());
    }
    @Test
    void shouldThrowExceptionWhenOwnerIdIsNull() {
        Restaurant restaurant = buildValidRestaurant();
        restaurant.setOwnerId(null);

        DomainException exception = assertThrows(
                DomainException.class,
                () -> RestaurantDomainValidator.validateRestaurant(restaurant)
        );

        assertEquals(ErrorCode.INVALID_RESTAURANT, exception.getErrorCode());
    }

    @Test
    void shouldThrowExceptionWhenOwnerIdIsInvalid() {
        Restaurant restaurant = buildValidRestaurant();
        restaurant.setOwnerId(0L);

        DomainException exception = assertThrows(
                DomainException.class,
                () -> RestaurantDomainValidator.validateRestaurant(restaurant)
        );

        assertEquals(ErrorCode.INVALID_RESTAURANT, exception.getErrorCode());
    }
}
