package com.pragma.plazoleta.infrastructure.out.jpa.adapter;

import com.pragma.plazoleta.application.dto.response.IsOwnerResponseDto;
import com.pragma.plazoleta.domain.spi.IUserOwnerValidationPort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class UserOwnerRestAdapter implements IUserOwnerValidationPort {

    private final RestTemplate restTemplate;

    @Value("${users.service.url}")
    private String usersServiceUrl;

    @Override
    public boolean isOwner(Long userId) {
        String url = usersServiceUrl + "/api/v1/usuario/" + userId;
        IsOwnerResponseDto response =
                restTemplate.getForObject(url, IsOwnerResponseDto.class);

        return response.getIsOwner();
    }
}
