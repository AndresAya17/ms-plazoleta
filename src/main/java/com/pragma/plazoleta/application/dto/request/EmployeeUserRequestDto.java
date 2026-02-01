package com.pragma.plazoleta.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmployeeUserRequestDto {
    private String firstName;
    private String lastName;
    private String documentNumber;
    private String phoneNumber;
    private String email;
    private String password;
}
