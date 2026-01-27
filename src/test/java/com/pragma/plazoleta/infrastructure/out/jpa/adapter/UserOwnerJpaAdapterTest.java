package com.pragma.plazoleta.infrastructure.out.jpa.adapter;

import com.pragma.plazoleta.application.dto.response.IsOwnerResponseDto;
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
public class UserOwnerJpaAdapterTest {
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private UserOwnerJpaAdapter userOwnerJpaAdapter;

    @BeforeEach
    void setUp() {
        // Simula @Value("${users.service.url}")
        ReflectionTestUtils.setField(
                userOwnerJpaAdapter,
                "usersServiceUrl",
                "http://localhost:8081"
        );
    }

    @Test
    void shouldReturnTrueWhenUserIsOwner() {
        // arrange
        Long userId = 1L;
        String expectedUrl = "http://localhost:8081/api/v1/usuario/" + userId;

        IsOwnerResponseDto responseDto = new IsOwnerResponseDto(true);

        when(restTemplate.getForObject(expectedUrl, IsOwnerResponseDto.class))
                .thenReturn(responseDto);

        // act
        boolean result = userOwnerJpaAdapter.isOwner(userId);

        // assert
        assertTrue(result);
        verify(restTemplate, times(1))
                .getForObject(expectedUrl, IsOwnerResponseDto.class);
        verifyNoMoreInteractions(restTemplate);
    }

    @Test
    void shouldReturnFalseWhenUserIsNotOwner() {
        // arrange
        Long userId = 2L;
        String expectedUrl = "http://localhost:8081/api/v1/usuario/" + userId;

        IsOwnerResponseDto responseDto = new IsOwnerResponseDto(false);

        when(restTemplate.getForObject(expectedUrl, IsOwnerResponseDto.class))
                .thenReturn(responseDto);

        // act
        boolean result = userOwnerJpaAdapter.isOwner(userId);

        // assert
        assertFalse(result);
        verify(restTemplate)
                .getForObject(expectedUrl, IsOwnerResponseDto.class);
    }
}
