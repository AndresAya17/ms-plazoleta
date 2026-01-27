package com.pragma.plazoleta.domain.spi;

import com.pragma.plazoleta.domain.model.Rol;

public interface IUserValidationPort {
    Rol getUserRol(Long id);
}
