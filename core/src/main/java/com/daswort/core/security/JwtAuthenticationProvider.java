package com.daswort.core.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class JwtAuthenticationProvider implements AuthenticationProvider {
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationProvider.class);

    private final UserDetailsService userDetailsService;
    private final JwtManager jwtManager;

    public JwtAuthenticationProvider(UserDetailsService userDetailsService, JwtManager jwtManager) {
        this.userDetailsService = checkNotNull(userDetailsService);
        this.jwtManager = jwtManager;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        checkNotNull(authentication);
        checkArgument(authentication instanceof JwtAuthenticationToken);

        final JwtAuthenticationToken jwtAuthToken = (JwtAuthenticationToken) authentication;
        final String jwt = (String) jwtAuthToken.getCredentials();
        final Optional<String> optSubject = jwtManager.getSubject(jwt);
        if (optSubject.isEmpty()) return createUnauthenticatedResponse(jwtAuthToken);

        final String subject = optSubject.get();
        try {
            final UserDetails userDetails = userDetailsService.loadUserByUsername(subject);
            if (userDetails.isEnabled()) {
                return new JwtAuthenticationToken(userDetails.getUsername(), userDetails.getAuthorities());
            } else {
                log.debug("User is disabled");
            }
        } catch (UsernameNotFoundException e) {
            log.debug("User not found by username", e);
        }
        return createUnauthenticatedResponse(jwtAuthToken);
    }

    private Authentication createUnauthenticatedResponse(Authentication authentication) {
        authentication.setAuthenticated(false);
        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        checkNotNull(authentication);
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
