package com.pragma.plazoleta.application.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantResponseDto {
    private Long id;
    private String name;
    private String nit;
    private String address;
    private String phoneNumber;
    private String logoUrl;
    private Long ownerId;
}
