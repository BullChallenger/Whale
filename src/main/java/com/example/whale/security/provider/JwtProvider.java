package com.example.whale.security.provider;

import com.example.whale.util.CookieUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtProvider {

    private static final String ISSUER = "whale_project";
    private static final String AUTHORITY_KEY = "authority";
    private static final String COMMA = ",";
    private static final int THOUSAND_SECONDS = 1000;
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;
    private static final String BEARER = "Bearer ";

    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;
    private final Key secretKey;
    private final CookieUtil cookieUtil;

    @Value("${jwt.access.header}")
    private String ACCESS_TOKEN_HEADER;
    @Value("${jwt.refresh.header}")
    private String REFRESH_TOKEN_HEADER;

    public JwtProvider(@Value("${jwt.access.expiration}") long accessTokenExpiration,
                       @Value("${jwt.refresh.expiration}") long refreshTokenExpiration,
                       @Value("${jwt.secret}") String secret,
                       CookieUtil cookieUtil)
    {
        this.accessTokenExpiration = accessTokenExpiration * THOUSAND_SECONDS;
        this.refreshTokenExpiration = refreshTokenExpiration * THOUSAND_SECONDS;
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.cookieUtil = cookieUtil;
    }

    public String generateAccessToken(Authentication authentication) {
        return Jwts.builder()
                .signWith(secretKey, SIGNATURE_ALGORITHM)
                .setHeader(setTokenHeader())
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
                .setHeader(setTokenHeader())
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

    private Map<String, Object> setTokenHeader() {
        Header header = Jwts.header();
        header.setType(Header.JWT_TYPE);
        header.put("alg", SIGNATURE_ALGORITHM);

        return header;
    }

    public int isTokenValid(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return 1;
        } catch (ExpiredJwtException e) {
            log.error("만료된 토큰입니다.", e);
            return 0;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("유효하지 않은 토큰입니다.", e);
            return -1;
        }
    }

    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(ACCESS_TOKEN_HEADER))
                .filter(accessToken -> accessToken.startsWith(BEARER))
                .map(accessToken -> accessToken.replace(BEARER, ""));
    }

    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        Optional<Cookie> cookie = cookieUtil.getCookie(request, REFRESH_TOKEN_HEADER);
        return cookie.map(Cookie::getValue);
    }

    public Optional<String> extractEmailInSubject(String token) {
        return extractClaim(token, Claims.SUBJECT, String.class);
    }

    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private <T> Optional<T> extractClaim(String token, String claimName, Class<T> type) {
        try {
            Claims claims = extractClaims(token);
            return Optional.ofNullable(claims.get(claimName, type));
        } catch (ExpiredJwtException e) {
            Claims claims = e.getClaims();
            return Optional.ofNullable(claims.get(claimName, type));
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtException("유효하지 않은 토큰입니다.");
        }
    }

}
