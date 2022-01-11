package com.daswort.core.security;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class IssuedJwt {
    String value;
    Instant expiration;
}
