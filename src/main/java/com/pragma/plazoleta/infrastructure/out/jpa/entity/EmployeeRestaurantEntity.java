package com.pragma.plazoleta.infrastructure.out.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "employee_restaurant")
@Getter
@Setter
public class EmployeeRestaurantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long employeeUserId;

    @Column(nullable = false)
    private Long restaurantId;
}
