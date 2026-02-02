package com.pragma.plazoleta.application.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDishStatusRequestDto {

    @NotNull(message = "Status is required")
    private Boolean active;

}
