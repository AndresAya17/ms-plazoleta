package com.pragma.plazoleta.domain.spi;

public interface ISmsPersistencePort {
    void sendSms(String to, String message);
}
