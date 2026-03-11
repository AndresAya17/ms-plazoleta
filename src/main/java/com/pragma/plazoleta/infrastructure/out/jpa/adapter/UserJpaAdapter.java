package com.pragma.plazoleta.infrastructure.out.jpa.adapter;

import com.pragma.plazoleta.application.dto.response.ClientPhoneResponseDto;
import com.pragma.plazoleta.application.dto.response.EmployeeEmailResponseDto;
import com.pragma.plazoleta.domain.exception.DomainException;
import com.pragma.plazoleta.domain.exception.ErrorCode;
import com.pragma.plazoleta.domain.spi.IUserPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class UserJpaAdapter implements IUserPersistencePort {

    private final RestTemplate restTemplate;

    @Value("${users.service.url}")
    private String userServiceUrl;

    @Override
    public String getClientPhoneByUserId(Long userId) {

        String url = userServiceUrl +
                "/api/v1/user/client/" + userId + "/phone";

        try {

            ResponseEntity<ClientPhoneResponseDto> response =
                    restTemplate.getForEntity(url, ClientPhoneResponseDto.class);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new DomainException(
                        ErrorCode.EXTERNAL_SERVICE_ERROR,
                        "Invalid response from user service"
                );
            }

            return response.getBody().getPhoneNumber();

        } catch (RestClientException ex) {
            throw new DomainException(
                    ErrorCode.EXTERNAL_SERVICE_ERROR,
                    "Error communicating with user service"
            );
        }
    }

    @Override
    public String getEmailByUserId(Long userId) {
        String url = userServiceUrl +
                "/api/v1/user/employee/" + userId + "/email";

        try {

            ResponseEntity<EmployeeEmailResponseDto> response =
                    restTemplate.getForEntity(url, EmployeeEmailResponseDto.class);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new DomainException(
                        ErrorCode.EXTERNAL_SERVICE_ERROR,
                        "Invalid response from user service"
                );
            }

            return response.getBody().getEmail();

        } catch (RestClientException ex) {
            throw new DomainException(
                    ErrorCode.EXTERNAL_SERVICE_ERROR,
                    "Error communicating with user service"
            );
        }
    }
}
