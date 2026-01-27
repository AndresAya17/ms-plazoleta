package com.pragma.plazoleta.infrastructure.out.jpa.adapter;

import com.pragma.plazoleta.application.dto.response.IsOwnerResponseDto;
import com.pragma.plazoleta.application.dto.response.RolUserResponseDto;
import com.pragma.plazoleta.domain.model.Rol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserRolJpaAdapterTest {
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private UserRolJpaAdapter userRolJpaAdapter;

    @BeforeEach
    void setUp() {
        // Simula @Value("${users.service.url}")
        ReflectionTestUtils.setField(
                userRolJpaAdapter,
                "usersServiceUrl",
                "http://localhost:8081"
        );
    }

    @Test
    void shouldReturnRoleWhenUserIsProprietary() {
        // arrange
        Long userId = 1L;
        String expectedUrl = "http://localhost:8081/api/v1/usuario/" + userId + "/rol";

        RolUserResponseDto responseDto =
                new RolUserResponseDto("PROPIETARIO");

        when(restTemplate.getForObject(expectedUrl, RolUserResponseDto.class))
                .thenReturn(responseDto);

        // act
        Rol result = userRolJpaAdapter.getUserRol(userId);

        // assert
        assertEquals(Rol.PROPIETARIO, result);
        verify(restTemplate)
                .getForObject(expectedUrl, RolUserResponseDto.class);
        verifyNoMoreInteractions(restTemplate);
    }

    @Test
    void shouldReturnAdministratorRole() {
        // arrange
        Long userId = 2L;
        String expectedUrl = "http://localhost:8081/api/v1/usuario/" + userId + "/rol";

        RolUserResponseDto responseDto =
                new RolUserResponseDto("ADMINISTRADOR");

        when(restTemplate.getForObject(expectedUrl, RolUserResponseDto.class))
                .thenReturn(responseDto);

        // act
        Rol result = userRolJpaAdapter.getUserRol(userId);

        // assert
        assertEquals(Rol.ADMINISTRADOR, result);
        verify(restTemplate)
                .getForObject(expectedUrl, RolUserResponseDto.class);
    }
}
