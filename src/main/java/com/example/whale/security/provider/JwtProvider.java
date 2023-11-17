package com.example.whale.security.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

    private static final String ISSUER = "whale_project";
    private static final String HEADER_TYP = "JWT";
    private static final String AUTHORITY_KEY = "authority";
    private static final String COMMA = ",";
    private static final int THOUSAND_SECONDS = 1000;
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;
    private final Key secretKey;

    public JwtProvider(@Value("${jwt.access.expiration}") long accessTokenExpiration,
                       @Value("${jwt.refresh.expiration}") long refreshTokenExpiration,
                       @Value("${jwt.secret}") String secret)
    {
        this.accessTokenExpiration = accessTokenExpiration * THOUSAND_SECONDS;
        this.refreshTokenExpiration = refreshTokenExpiration * THOUSAND_SECONDS;
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String generateAccessToken(Authentication authentication) {
        return Jwts.builder()
                .signWith(secretKey, SIGNATURE_ALGORITHM)
                .setHeader(setAccessTokenHeader())
                .setClaims(setAccessTokenClaims(authentication))
                .compact();
    }

    private Claims setAccessTokenClaims(Authentication authentication) {
        long now = new Date().getTime();

        return  (Claims) Jwts.claims()
                                .setSubject(authentication.getName())
                                .setIssuer(ISSUER)
                                .setIssuedAt(new Date())
                                .setExpiration(calculateAccessTokenExpiration(now))
                                .setAudience(authentication.getName())
                                .put(AUTHORITY_KEY, getAuthorities(authentication));
    }

    private Map<String, Object> setAccessTokenHeader() {
        Header header = Jwts.header();
        header.setType(HEADER_TYP);

        return header;
    }

    private String getAuthorities(Authentication authentication) {
        String authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(COMMA));
        return authorities;
    }

    private Date calculateAccessTokenExpiration(long now) {
        return new Date(now + accessTokenExpiration);
    }

    public String generateRefreshToken(Authentication authentication) {

        return Jwts.builder()
                .signWith(secretKey, SIGNATURE_ALGORITHM)
                .setClaims(setRefreshTokenClaims(authentication))
                .compact();
    }

    private Claims setRefreshTokenClaims(Authentication authentication) {
        long now = new Date().getTime();

        return (Claims) Jwts.claims()
                .setSubject(authentication.getName())
                .setIssuer(ISSUER)
                .setIssuedAt(new Date())
                .setExpiration(calculateRefreshTokenExpiration(now))
                .put(AUTHORITY_KEY, getAuthorities(authentication));
    }

    private Date calculateRefreshTokenExpiration(long now) {
        return new Date(now + refreshTokenExpiration);
    }

}
