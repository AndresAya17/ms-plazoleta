package com.pragma.plazoleta.application.dto.request;

import com.pragma.plazoleta.domain.constants.DomainConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantRequestDto {

    @NotBlank(message = "Restaurant name is required")
    private String name;

    @NotBlank(message = "NIT is required")
    @Pattern(
            regexp = DomainConstants.DOCUMENT_NUMBER_REGEX,
            message = "NIT must contain only numeric characters"
    )
    private String nit;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = DomainConstants.PHONE_NUMBER_REGEX,
            message = "Phone number must be numeric, may start with '+', and must not exceed 13 characters"
    )
    private String phoneNumber;

    @NotBlank(message = "Logo URL is required")
    private String logoUrl;

    @NotNull(message = "Owner id is required")
    private Long ownerId;
}
