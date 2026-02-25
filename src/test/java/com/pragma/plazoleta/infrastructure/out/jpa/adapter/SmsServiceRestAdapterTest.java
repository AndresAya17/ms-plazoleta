package com.pragma.plazoleta.infrastructure.out.jpa.adapter;

import com.pragma.plazoleta.domain.exception.DomainException;
import com.pragma.plazoleta.domain.exception.ErrorCode;
import com.pragma.plazoleta.application.dto.request.SmsRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SmsServiceRestAdapterTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private SmsServiceRestAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Inyectar manualmente el @Value
        ReflectionTestUtils.setField(
                adapter,
                "smsServiceUrl",
                "http://localhost:8082"
        );
    }

    @Test
    void shouldSendSmsSuccessfully() {

        String to = "+573001234567";
        String message = "Test message";

        adapter.sendSms(to, message);

        verify(restTemplate).postForEntity(
                eq("http://localhost:8082/api/v1/messages/"),
                any(SmsRequestDto.class),
                eq(Void.class)
        );
    }

    @Test
    void shouldThrowDomainExceptionWhenRestClientFails() {

        String to = "+573001234567";
        String message = "Test message";

        when(restTemplate.postForEntity(
                anyString(),
                any(SmsRequestDto.class),
                eq(Void.class)
        )).thenThrow(new RestClientException("Connection error"));

        DomainException ex = assertThrows(
                DomainException.class,
                () -> adapter.sendSms(to, message)
        );

        assertEquals(ErrorCode.EXTERNAL_SERVICE_ERROR, ex.getErrorCode());
        assertEquals("Error communicating with SMS service", ex.getMessage());
    }
}
