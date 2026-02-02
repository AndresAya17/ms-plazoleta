package com.pragma.plazoleta.domain.model;


import com.pragma.plazoleta.domain.exception.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class RestaurantTest {

    @Test
    void shouldSetAndGetAllFieldsCorrectly() {
        Restaurant restaurant = new Restaurant();

        restaurant.setId(1L);
        restaurant.setName("Restaurante Test");
        restaurant.setNit("123456789");
        restaurant.setAddress("Calle 123");
        restaurant.setPhoneNumber("+573001234567");
        restaurant.setLogoUrl("https://logo.com/logo.png");
        restaurant.setOwnerId(10L);

        assertEquals(1L, restaurant.getId());
        assertEquals("Restaurante Test", restaurant.getName());
        assertEquals("123456789", restaurant.getNit());
        assertEquals("Calle 123", restaurant.getAddress());
        assertEquals("+573001234567", restaurant.getPhoneNumber());
        assertEquals("https://logo.com/logo.png", restaurant.getLogoUrl());
        assertEquals(10L, restaurant.getOwnerId());
    }

    @Test
    void shouldAllowNullValues() {
        Restaurant restaurant = new Restaurant();

        assertNull(restaurant.getId());
        assertNull(restaurant.getName());
        assertNull(restaurant.getNit());
        assertNull(restaurant.getAddress());
        assertNull(restaurant.getPhoneNumber());
        assertNull(restaurant.getLogoUrl());
        assertNull(restaurant.getOwnerId());
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(null);

        DomainException exception = assertThrows(
                DomainException.class,
                restaurant::validateName
        );

        assertEquals(ErrorCode.INVALID_RESTAURANT, exception.getErrorCode());
        assertEquals("Restaurant name is required", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNameIsEmpty() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("   ");

        DomainException exception = assertThrows(
                DomainException.class,
                restaurant::validateName
        );

        assertEquals(ErrorCode.INVALID_RESTAURANT, exception.getErrorCode());
        assertEquals("Restaurant name is required", exception.getMessage());
    }

    @Test
    void shouldNotThrowExceptionWhenNameIsValid() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("My Restaurant");

        assertDoesNotThrow(restaurant::validateName);
    }


    @Test
    void shouldThrowExceptionWhenNitIsInvalid() {
        Restaurant restaurant = new Restaurant();
        restaurant.setNit("ABC123");

        DomainException exception = assertThrows(
                DomainException.class,
                restaurant::validateNit
        );

        assertEquals(ErrorCode.INVALID_RESTAURANT, exception.getErrorCode());
        assertEquals("Restaurant nit is required", exception.getMessage());
    }

    @Test
    void shouldNotThrowExceptionWhenNitIsValid() {
        Restaurant restaurant = new Restaurant();
        restaurant.setNit("123456789");

        assertDoesNotThrow(restaurant::validateNit);
    }


    @Test
    void shouldThrowExceptionWhenAddressIsNull() {
        Restaurant restaurant = new Restaurant();
        restaurant.setAddress(null);

        DomainException exception = assertThrows(
                DomainException.class,
                restaurant::validateAddress
        );

        assertEquals(ErrorCode.INVALID_RESTAURANT, exception.getErrorCode());
        assertEquals("Restaurant address is required", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAddressIsEmpty() {
        Restaurant restaurant = new Restaurant();
        restaurant.setAddress("   ");

        DomainException exception = assertThrows(
                DomainException.class,
                restaurant::validateAddress
        );

        assertEquals(ErrorCode.INVALID_RESTAURANT, exception.getErrorCode());
        assertEquals("Restaurant address is required", exception.getMessage());
    }

    @Test
    void shouldNotThrowExceptionWhenAddressIsValid() {
        Restaurant restaurant = new Restaurant();
        restaurant.setAddress("Street 123");

        assertDoesNotThrow(restaurant::validateAddress);
    }

    @Test
    void shouldThrowExceptionWhenPhoneNumberIsInvalid() {
        Restaurant restaurant = new Restaurant();
        restaurant.setPhoneNumber("ABC999");

        DomainException exception = assertThrows(
                DomainException.class,
                restaurant::validatePhoneNumber
        );

        assertEquals(ErrorCode.INVALID_RESTAURANT, exception.getErrorCode());
        assertEquals("Restaurant phone is required", exception.getMessage());
    }

    @Test
    void shouldNotThrowExceptionWhenPhoneNumberIsValid() {
        Restaurant restaurant = new Restaurant();
        restaurant.setPhoneNumber("+573001234567");

        assertDoesNotThrow(restaurant::validatePhoneNumber);
    }


    @Test
    void shouldThrowExceptionWhenLogoUrlIsNull() {
        Restaurant restaurant = new Restaurant();
        restaurant.setLogoUrl(null);

        DomainException exception = assertThrows(
                DomainException.class,
                restaurant::validateLogoUrl
        );

        assertEquals(ErrorCode.INVALID_RESTAURANT, exception.getErrorCode());
        assertEquals("Restaurant logo_url is required", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenLogoUrlIsEmpty() {
        Restaurant restaurant = new Restaurant();
        restaurant.setLogoUrl("   ");

        DomainException exception = assertThrows(
                DomainException.class,
                restaurant::validateLogoUrl
        );

        assertEquals(ErrorCode.INVALID_RESTAURANT, exception.getErrorCode());
        assertEquals("Restaurant logo_url is required", exception.getMessage());
    }

    @Test
    void shouldNotThrowExceptionWhenLogoUrlIsValid() {
        Restaurant restaurant = new Restaurant();
        restaurant.setLogoUrl("http://image.com/logo.png");

        assertDoesNotThrow(restaurant::validateLogoUrl);
    }


    @Test
    void shouldThrowExceptionWhenOwnerIdIsNull() {
        Restaurant restaurant = new Restaurant();
        restaurant.setOwnerId(null);

        DomainException exception = assertThrows(
                DomainException.class,
                restaurant::validateOwnerId
        );

        assertEquals(ErrorCode.INVALID_RESTAURANT, exception.getErrorCode());
        assertEquals("Restaurant owner is required", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenOwnerIdIsZero() {
        Restaurant restaurant = new Restaurant();
        restaurant.setOwnerId(0L);

        DomainException exception = assertThrows(
                DomainException.class,
                restaurant::validateOwnerId
        );

        assertEquals(ErrorCode.INVALID_RESTAURANT, exception.getErrorCode());
        assertEquals("Restaurant owner is required", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenOwnerIdIsNegative() {
        Restaurant restaurant = new Restaurant();
        restaurant.setOwnerId(-5L);

        DomainException exception = assertThrows(
                DomainException.class,
                restaurant::validateOwnerId
        );

        assertEquals(ErrorCode.INVALID_RESTAURANT, exception.getErrorCode());
        assertEquals("Restaurant owner is required", exception.getMessage());
    }

    @Test
    void shouldNotThrowExceptionWhenOwnerIdIsValid() {
        Restaurant restaurant = new Restaurant();
        restaurant.setOwnerId(1L);

        assertDoesNotThrow(restaurant::validateOwnerId);
    }

    @Test
    void shouldValidateRestaurantSuccessfullyWhenAllFieldsAreValid() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("My Restaurant");
        restaurant.setNit("123456789");
        restaurant.setAddress("Street 123");
        restaurant.setPhoneNumber("+573001234567");
        restaurant.setLogoUrl("http://image.com/logo.png");
        restaurant.setOwnerId(1L);

        assertDoesNotThrow(restaurant::validateRestaurant);
    }
}
