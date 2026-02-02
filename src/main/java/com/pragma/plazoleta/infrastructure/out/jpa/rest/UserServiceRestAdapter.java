package com.pragma.plazoleta.infrastructure.out.jpa.rest;

import com.pragma.plazoleta.application.dto.request.EmployeeUserRequestDto;
import com.pragma.plazoleta.application.dto.response.EmployeeResponseDto;
import com.pragma.plazoleta.domain.spi.IUserPersistencePort;
import com.pragma.plazoleta.infrastructure.exception.ExternalServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class UserServiceRestAdapter implements IUserPersistencePort {

    private final RestTemplate restTemplate;

    @Value("${users.service.url}")
    private String usersServiceUrl;


    @Override
    public Long createEmployee(EmployeeUserRequestDto employeeUserRequestDto) {
        String url = usersServiceUrl + "/api/v1/usuario/employee";

        ResponseEntity<EmployeeResponseDto> response =
                restTemplate.postForEntity(
                        url,
                        employeeUserRequestDto,
                        EmployeeResponseDto.class
                );

        if (!response.getStatusCode().is2xxSuccessful()
                || response.getBody() == null) {
            throw new ExternalServiceException("Error creating employee user");
        }

        return response.getBody().getEmployeeUserId();
    }
}
