package com.pragma.plazoleta.infrastructure.out.jpa.rest;

import com.pragma.plazoleta.domain.exception.DomainException;
import com.pragma.plazoleta.domain.exception.ErrorCode;
import com.pragma.plazoleta.domain.model.Trazabilidad;
import com.pragma.plazoleta.domain.spi.ITrazabilidadPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class TrazabilidadServiceRestAdapter implements ITrazabilidadPersistencePort {

    private final RestTemplate restTemplate;

    @Value("${trazabilidad.service.url}")
    private String trazabilidadService;

    @Override
    public void saveLog(Trazabilidad trazabilidad) {

        String url = trazabilidadService + "/api/v1/logs";

        try{
            restTemplate.postForEntity(
                    url,trazabilidad, Void.class);
        }catch (RestClientException e){
            throw new DomainException(ErrorCode.EXTERNAL_SERVICE_ERROR, "Error comunicating with service trazabilidad");
        }

    }
}
