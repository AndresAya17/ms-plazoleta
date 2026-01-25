package com.pragma.plazoleta.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantRequestDto {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "El NIT es obligatorio")
    @Pattern(
            regexp = "\\d+",
            message = "El NIT debe contener solo números"
    )
    private String nit;

    @NotBlank(message = "La dirección es obligatoria")
    private String address;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(
            regexp = "^\\+?\\d{1,13}$",
            message = "El teléfono debe ser numérico, puede iniciar con '+' y tener máximo 13 caracteres"
    )
    private String phoneNumber;

    @NotBlank(message = "La URL del logo es obligatoria")
    private String logoUrl;

    @NotNull(message = "El id del propietario es obligatorio")
    private Long ownerId;
}
