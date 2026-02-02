package com.pragma.plazoleta.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EmployeeUserRequestDto {
    private String firstName;
    private String lastName;
    private String documentNumber;
    private String phoneNumber;
    private String email;
    private String password;
    private String rol;
}
