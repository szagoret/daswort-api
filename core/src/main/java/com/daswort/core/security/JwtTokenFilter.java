package com.daswort.core.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static java.util.Optional.*;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER_VALUE_PREFIX = "Bearer ";
    private final AuthenticationManager authManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        final Optional<String> optJwt = ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .flatMap((h -> {
                    if (h.startsWith(AUTH_HEADER_VALUE_PREFIX)) {
                        return of(h.substring(AUTH_HEADER_VALUE_PREFIX.length()));
                    }
                    return empty();
                }));
        if (optJwt.isPresent()) {
            final String jwt = optJwt.get();
            final JwtAuthenticationToken authRequest = new JwtAuthenticationToken(jwt);
            final Authentication authResponse = authManager.authenticate(authRequest);
            if (authResponse != null && authResponse.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(authResponse);
            }
        }
        filterChain.doFilter(request, response);
    }

}
