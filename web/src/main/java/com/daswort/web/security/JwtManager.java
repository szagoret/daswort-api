package com.daswort.web.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;

@Component
public class JwtManager {
    private static final Logger log = LoggerFactory.getLogger(JwtManager.class);

    private final Environment env;

    @Autowired
    public JwtManager(Environment env) {
        this.env = env;
    }

    public Optional<String> getSubject(String token) {
        checkNotNull(token);
        try {
            final String subject = getClaimFromToken(token, Claims::getSubject);
            return Optional.of(subject);
        } catch (JwtException | IllegalArgumentException e) {
            log.debug("JWT token parsing failed", e);
            return Optional.empty();
        }
    }

    public IssuedJwt generateToken(String subject) {
        checkNotNull(subject);
        final Instant now = Instant.now();
        final Instant expiration = now.plus(getExpirationHours(), ChronoUnit.HOURS);
        final var jwtValue = Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiration))
                .signWith(SignatureAlgorithm.HS512, getSigningKey())
                .compact();
        return IssuedJwt.builder()
                .value(jwtValue)
                .expiration(expiration)
                .build();
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .parseClaimsJws(token)
                .getBody();
    }

    private String getSigningKey() {
        return env.getRequiredProperty("SIGNING_KEY");
    }

    private Integer getExpirationHours() {
        return 8;
    }
}
