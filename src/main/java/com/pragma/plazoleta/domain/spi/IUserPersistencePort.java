package com.pragma.plazoleta.domain.spi;

public interface IUserPersistencePort {

    String getClientPhoneByUserId(Long userId);
}
