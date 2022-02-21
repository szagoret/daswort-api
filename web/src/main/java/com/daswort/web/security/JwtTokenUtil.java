package com.daswort.web.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
    private final AuthenticationManager authManager;
    private final JwtManager jwtManager;

    public Optional<AuthResponse> authenticateWithPassword(AuthRequest authRequest) {
        final String username = authRequest.getUsername();
        final var authentication = new UsernamePasswordAuthenticationToken(username, authRequest.getPassword());
        final Authentication auth = authManager.authenticate(authentication);
        if (auth != null && auth.isAuthenticated()) {
            final var issuedJwt = jwtManager.generateToken(username);
            final var tokenResponse = AuthResponse.builder()
                    .jwt(issuedJwt.getValue())
                    .expiration(issuedJwt.getExpiration())
                    .username(username)
                    .build();
            return Optional.of(tokenResponse);
        }
        return Optional.empty();
    }
}
