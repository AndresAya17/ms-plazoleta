package com.pragma.plazoleta.domain.spi;

public interface IJwtPersistencePort {
    String generateToken(Long userId, String rol);

    boolean validateToken(String token);

    Long getUserId(String token);

    String getRol(String token);

    String getEmail(String token);
}
