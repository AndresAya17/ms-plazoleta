package com.pragma.plazoleta.infrastructure.out.jpa.entity;

import com.pragma.plazoleta.domain.model.DishCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "dish")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DishEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DishCategory dishCategory;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private Long restaurantId;

    @Column(nullable = false)
    private Long ownerId;
}
