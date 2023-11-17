package com.example.whale.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class JwtService {

    @Value("${jwt.refresh.expiration}")
    private long refreshTokenExpiration;
    @Value("${jwt.refresh.header}")
    private String REFRESH_TOKEN_HEADER;

    private final long THOUSAND_SECONDS = 1000;

    public ResponseCookie setRefreshTokenInCookie(String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN_HEADER, refreshToken)
                .httpOnly(false)
                .secure(false)
                .sameSite("None")
                .maxAge(refreshTokenExpiration * THOUSAND_SECONDS)
                .path("/")
                .build();

        return cookie;
    }

}
