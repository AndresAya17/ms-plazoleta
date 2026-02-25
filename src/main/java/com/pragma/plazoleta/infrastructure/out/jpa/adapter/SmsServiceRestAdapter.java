package com.pragma.plazoleta.infrastructure.out.jpa.adapter;

import com.pragma.plazoleta.application.dto.request.SmsRequestDto;
import com.pragma.plazoleta.domain.exception.DomainException;
import com.pragma.plazoleta.domain.exception.ErrorCode;
import com.pragma.plazoleta.domain.spi.ISmsPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


@Component
@RequiredArgsConstructor
public class SmsServiceRestAdapter implements ISmsPersistencePort {

    private final RestTemplate restTemplate;

    @Value("${sms.service.url}")
    private String smsServiceUrl;

    @Override
    public void sendSms(String to, String message) {

        String url = smsServiceUrl +
                "/api/v1/messages/";

        SmsRequestDto request = new SmsRequestDto(to, message);

        try {
            restTemplate.postForEntity(url, request, Void.class);
        } catch (RestClientException ex) {
            ex.printStackTrace();
            throw new DomainException(ErrorCode.EXTERNAL_SERVICE_ERROR,"Error communicating with SMS service");
        }

    }
}
