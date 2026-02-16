package com.pragma.plazoleta.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeRestaurantTest {

    @Test
    void shouldCreateEmployeeRestaurantAndSetFieldsCorrectly() {
        Long employeeUserId = 5L;
        Long restaurantId = 10L;

        EmployeeRestaurant employeeRestaurant = new EmployeeRestaurant();

        employeeRestaurant.setEmployeeUserId(employeeUserId);
        employeeRestaurant.setRestaurantId(restaurantId);

        assertEquals(employeeUserId, employeeRestaurant.getEmployeeUserId());
        assertEquals(restaurantId, employeeRestaurant.getRestaurantId());
    }

    @Test
    void shouldAllowNullValuesByDefault() {
        EmployeeRestaurant employeeRestaurant = new EmployeeRestaurant();

        assertNull(employeeRestaurant.getEmployeeUserId());
        assertNull(employeeRestaurant.getRestaurantId());
    }
}
