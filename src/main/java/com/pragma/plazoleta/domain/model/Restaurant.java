package com.pragma.plazoleta.domain.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Restaurant {
    private Long id;
    private String name;
    private String nit;
    private String address;
    private String PhoneNumber;
    private String logoUrl;
    private Long ownerId;
}
