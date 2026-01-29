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

        assertThrows(
                InvalidRestaurantNameException.class,
                restaurant::validateName
        );
    }

    @Test
    void shouldThrowExceptionWhenNameIsEmpty() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("   ");

        assertThrows(
                InvalidRestaurantNameException.class,
                restaurant::validateName
        );
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

        assertThrows(
                InvalidRestaurantNitException.class,
                restaurant::validateNit
        );
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

        assertThrows(
                InvalidRestaurantAddressException.class,
                restaurant::validateAddress
        );
    }

    @Test
    void shouldThrowExceptionWhenAddressIsEmpty() {
        Restaurant restaurant = new Restaurant();
        restaurant.setAddress("   ");

        assertThrows(
                InvalidRestaurantAddressException.class,
                restaurant::validateAddress
        );
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

        assertThrows(
                InvalidRestaurantPhoneException.class,
                restaurant::validatePhoneNumber
        );
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

        assertThrows(
                InvalidRestaurantLogoException.class,
                restaurant::validateLogoUrl
        );
    }

    @Test
    void shouldThrowExceptionWhenLogoUrlIsEmpty() {
        Restaurant restaurant = new Restaurant();
        restaurant.setLogoUrl("   ");

        assertThrows(
                InvalidRestaurantLogoException.class,
                restaurant::validateLogoUrl
        );
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

        assertThrows(
                InvalidRestaurantOwnerException.class,
                restaurant::validateOwnerId
        );
    }

    @Test
    void shouldThrowExceptionWhenOwnerIdIsZero() {
        Restaurant restaurant = new Restaurant();
        restaurant.setOwnerId(0L);

        assertThrows(
                InvalidRestaurantOwnerException.class,
                restaurant::validateOwnerId
        );
    }

    @Test
    void shouldThrowExceptionWhenOwnerIdIsNegative() {
        Restaurant restaurant = new Restaurant();
        restaurant.setOwnerId(-5L);

        assertThrows(
                InvalidRestaurantOwnerException.class,
                restaurant::validateOwnerId
        );
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
