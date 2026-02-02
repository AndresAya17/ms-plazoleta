package com.pragma.plazoleta.application.dto.request;

import com.pragma.plazoleta.domain.constants.DomainConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantEmployeeRequestDto {

    @NotBlank(message = "Name is required")
    @Size(max = 50, message = "Name must not exceed 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must not exceed 50 characters")
    private String lastName;

    @NotBlank(message = "Identity document is required")
    @Pattern(
            regexp = DomainConstants.DOCUMENT_NUMBER_REGEX,
            message = "Identity document must contain only numbers"
    )
    private String documentNumber;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = DomainConstants.PHONE_NUMBER_REGEX,
            message = "Phone number must be numeric and contain between 7 and 13 digits"
    )
    private String phoneNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Email format is not valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must contain at least 6 characters")
    private String password;
}
