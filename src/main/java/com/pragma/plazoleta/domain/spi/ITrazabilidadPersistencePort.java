package com.pragma.plazoleta.domain.spi;

import com.pragma.plazoleta.domain.model.Trazabilidad;

public interface ITrazabilidadPersistencePort {

    void saveLog(Trazabilidad trazabilidad);

}
