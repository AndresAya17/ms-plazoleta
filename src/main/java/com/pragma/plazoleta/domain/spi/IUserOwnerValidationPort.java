package com.pragma.plazoleta.domain.spi;

public interface IUserOwnerValidationPort {
    boolean isOwner(Long userId);
}
