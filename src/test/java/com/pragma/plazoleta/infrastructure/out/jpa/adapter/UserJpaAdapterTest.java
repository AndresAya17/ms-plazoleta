package com.pragma.plazoleta.infrastructure.out.jpa.adapter;

import com.pragma.plazoleta.application.dto.response.EmployeeEmailResponseDto;
import com.pragma.plazoleta.domain.exception.DomainException;
import com.pragma.plazoleta.domain.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserJpaAdapterTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private UserJpaAdapter userAdapter;

    private final String userServiceUrl = "http://localhost:8080";

    @BeforeEach
    void setUp() {
        // Si usas @Value para la URL, puedes inyectarla manualmente con ReflectionTestUtils
        ReflectionTestUtils.setField(userAdapter, "userServiceUrl", userServiceUrl);
    }

    @Test
    @DisplayName("Debe retornar el email cuando la respuesta del servicio externo es exitosa")
    void getEmailByUserId_Success() {
        // GIVEN
        Long userId = 1L;
        String expectedEmail = "test@example.com";
        String url = userServiceUrl + "/api/v1/user/employee/" + userId + "/email";

        EmployeeEmailResponseDto responseDto = new EmployeeEmailResponseDto();
        responseDto.setEmail(expectedEmail);
        ResponseEntity<EmployeeEmailResponseDto> responseEntity = new ResponseEntity<>(responseDto, HttpStatus.OK);

        when(restTemplate.getForEntity(url, EmployeeEmailResponseDto.class))
                .thenReturn(responseEntity);

        // WHEN
        String result = userAdapter.getEmailByUserId(userId);

        // THEN
        assertEquals(expectedEmail, result);
        verify(restTemplate).getForEntity(url, EmployeeEmailResponseDto.class);
    }

    @Test
    @DisplayName("Debe lanzar DomainException cuando el servicio externo retorna error o cuerpo nulo")
    void getEmailByUserId_InvalidResponse() {
        // GIVEN
        Long userId = 1L;
        String url = userServiceUrl + "/api/v1/user/employee/" + userId + "/email";

        // Simula una respuesta con código 404
        ResponseEntity<EmployeeEmailResponseDto> responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);

        when(restTemplate.getForEntity(url, EmployeeEmailResponseDto.class))
                .thenReturn(responseEntity);

        // WHEN & THEN
        DomainException exception = assertThrows(DomainException.class, () ->
                userAdapter.getEmailByUserId(userId)
        );

        assertEquals(ErrorCode.EXTERNAL_SERVICE_ERROR, exception.getErrorCode());
        assertEquals("Invalid response from user service", exception.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar DomainException cuando hay un fallo de comunicación (RestClientException)")
    void getEmailByUserId_CommunicationError() {
        // GIVEN
        Long userId = 1L;
        String url = userServiceUrl + "/api/v1/user/employee/" + userId + "/email";

        // Simula que RestTemplate lanza una excepción de red
        when(restTemplate.getForEntity(url, EmployeeEmailResponseDto.class))
                .thenThrow(new RestClientException("Connection refused"));

        // WHEN & THEN
        DomainException exception = assertThrows(DomainException.class, () ->
                userAdapter.getEmailByUserId(userId)
        );

        assertEquals(ErrorCode.EXTERNAL_SERVICE_ERROR, exception.getErrorCode());
        assertEquals("Error communicating with user service", exception.getMessage());
    }

}
