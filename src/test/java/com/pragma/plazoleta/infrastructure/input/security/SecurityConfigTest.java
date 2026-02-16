package com.pragma.plazoleta.infrastructure.input.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class SecurityConfigTest {

    @Test
    void shouldBuildSecurityFilterChain() throws Exception {
        JwtFilter jwtFilter = mock(JwtFilter.class);
        SecurityConfig securityConfig = new SecurityConfig(jwtFilter);
        HttpSecurity httpSecurity = mock(HttpSecurity.class, RETURNS_DEEP_STUBS);
        SecurityFilterChain filterChain = securityConfig.filterChain(httpSecurity);
        assertNotNull(filterChain);
        verify(httpSecurity).build();
    }

}
