package com.pragma.plazoleta.infrastructure.input.security;

import com.pragma.plazoleta.domain.spi.IJwtPersistencePort;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final IJwtPersistencePort persistencePort;

    public JwtFilter(IJwtPersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader(AUTH_HEADER);

        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            String token = authHeader.substring(BEARER_PREFIX.length());

            boolean valid = persistencePort.validateToken(token);

            if (valid) {
                Long userId = persistencePort.getUserId(token);
                String rol = persistencePort.getRol(token);
                String email = persistencePort.getEmail(token);


                request.setAttribute("auth.userId", userId);
                request.setAttribute("auth.email", email);

                System.out.println(
                        "Request attributes set -> userId=" + userId + ", rol=" + rol + ", email=" + email
                );

                List<GrantedAuthority> authorities =
                        List.of(new SimpleGrantedAuthority(rol));

                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(
                                userId,
                                null,
                                authorities
                        );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);

                System.out.println("Authentication set in SecurityContext");
            }
        }

        filterChain.doFilter(request, response);
    }
}
