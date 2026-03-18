package com.pragma.plazoleta.infrastructure.out.jpa.rest;


import com.pragma.plazoleta.domain.exception.DomainException;
import com.pragma.plazoleta.domain.exception.ErrorCode;
import com.pragma.plazoleta.domain.model.Trazabilidad;
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
class TrazabilidadServiceRestAdapterTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TrazabilidadServiceRestAdapter trazabilidadServiceRestAdapter;

    private final String trazabilidadUrl = "http://localhost:8083";

    @BeforeEach
    void setUp() {
        // Inyectamos el valor de la propiedad @Value manualmente
        ReflectionTestUtils.setField(trazabilidadServiceRestAdapter, "trazabilidadService", trazabilidadUrl);
    }

    @Test
    @DisplayName("Debe enviar el log correctamente vía POST")
    void saveLog_Success() {
        // GIVEN
        Trazabilidad trazabilidad = new Trazabilidad();
        trazabilidad.setOrderId(1001L);
        String url = trazabilidadUrl + "/api/v1/logs";

        // Simulamos una respuesta exitosa (Void)
        when(restTemplate.postForEntity(eq(url), eq(trazabilidad), eq(Void.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

        // WHEN
        trazabilidadServiceRestAdapter.saveLog(trazabilidad);

        // THEN
        verify(restTemplate).postForEntity(url, trazabilidad, Void.class);
    }

    @Test
    @DisplayName("Debe lanzar DomainException cuando falla la comunicación con el servicio de trazabilidad")
    void saveLog_CommunicationError() {
        // GIVEN
        Trazabilidad trazabilidad = new Trazabilidad();
        String url = trazabilidadUrl + "/api/v1/logs";

        // Simula que RestTemplate lanza una excepción de cliente
        when(restTemplate.postForEntity(eq(url), any(), eq(Void.class)))
                .thenThrow(new RestClientException("Connection timed out"));

        // WHEN & THEN
        DomainException exception = assertThrows(DomainException.class, () ->
                trazabilidadServiceRestAdapter.saveLog(trazabilidad)
        );

        // Validamos el código de error y mensaje definido en tu catch
        assertEquals(ErrorCode.EXTERNAL_SERVICE_ERROR, exception.getErrorCode());
        assertEquals("Error comunicating with service trazabilidad", exception.getMessage());
    }

}
