package com.pragma.plazoleta.infrastructure.out.jpa.adapter;

import com.pragma.plazoleta.application.dto.response.IsOwnerResponseDto;
import com.pragma.plazoleta.application.dto.response.RolUserResponseDto;
import com.pragma.plazoleta.domain.model.Rol;
import com.pragma.plazoleta.domain.spi.IUserValidationPort;
import com.pragma.plazoleta.domain.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class UserRolJpaAdapter implements IUserValidationPort {

    private final RestTemplate restTemplate;

    @Value("${users.service.url}")
    private String usersServiceUrl;



    @Override
    public Rol getUserRol(Long id) {
        String url = usersServiceUrl + "/api/v1/usuario/" + id + "/rol";
        try {
            RolUserResponseDto response =
                    restTemplate.getForObject(url, RolUserResponseDto.class);

            return Rol.valueOf(response.getRol());
        } catch (HttpClientErrorException.NotFound ex){
            throw new UserNotFoundException(id);
        }
    }
}
