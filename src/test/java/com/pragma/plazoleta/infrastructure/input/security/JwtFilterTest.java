package com.pragma.plazoleta.infrastructure.input.security;

import com.pragma.plazoleta.domain.spi.IJwtPersistencePort;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JwtFilterTest {

    private IJwtPersistencePort persistencePort;
    private JwtFilter jwtFilter;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        persistencePort = mock(IJwtPersistencePort.class);
        jwtFilter = new JwtFilter(persistencePort);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldContinueFilterChainWhenAuthorizationHeaderIsMissing() throws Exception {
        // arrange
        when(request.getHeader("Authorization")).thenReturn(null);

        // act
        jwtFilter.doFilter(request, response, filterChain);

        // assert
        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(persistencePort);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
    @Test
    void shouldContinueFilterChainWhenAuthorizationHeaderIsNotBearer() throws Exception {
        // arrange
        when(request.getHeader("Authorization")).thenReturn("Basic abc123");

        // act
        jwtFilter.doFilter(request, response, filterChain);

        // assert
        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(persistencePort);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
    @Test
    void shouldNotAuthenticateWhenTokenIsInvalid() throws Exception {
        // arrange
        String token = "invalid-token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(persistencePort.validateToken(token)).thenReturn(false);

        // act
        jwtFilter.doFilter(request, response, filterChain);

        // assert
        verify(persistencePort).validateToken(token);
        verify(filterChain).doFilter(request, response);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(request, never()).setAttribute(eq("auth.userId"), any());
        verify(request, never()).setAttribute(eq("auth.rol"), any());
    }

    @Test
    void shouldAuthenticateAndSetRequestAttributesWhenTokenIsValid() throws Exception {
        // arrange
        String token = "valid-token";
        Long userId = 10L;
        String rol = "OWNER";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(persistencePort.validateToken(token)).thenReturn(true);
        when(persistencePort.getUserId(token)).thenReturn(userId);
        when(persistencePort.getRol(token)).thenReturn(rol);

        // act
        jwtFilter.doFilter(request, response, filterChain);

        // assert
        verify(persistencePort).validateToken(token);
        verify(persistencePort).getUserId(token);
        verify(persistencePort).getRol(token);

        verify(request).setAttribute("auth.userId", userId);
        verify(request).setAttribute("auth.rol", rol);

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        assertNotNull(authentication);
        assertEquals(userId, authentication.getPrincipal());
        assertTrue(authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + rol)));

        verify(filterChain).doFilter(request, response);
    }
}
