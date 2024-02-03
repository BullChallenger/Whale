package com.example.whale.global.security.provider;

import com.example.whale.domain.user.model.AuthenticationUser;
import com.example.whale.global.util.AuthenticationUtil;
import com.example.whale.global.util.CookieUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtProvider {

    private static final String ISSUER = "whale_project";
    private static final String AUTHORITY_KEY = "authority";
    private static final String USER_ID = "USER_ID";
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
        long now = new Date().getTime();
        AuthenticationUser authUser = AuthenticationUtil.convertAuthentication(authentication);

        return Jwts.builder()
                .signWith(secretKey, SIGNATURE_ALGORITHM)
                .setHeader(setTokenHeader())
                .setSubject(authUser.getEmail())
                .setIssuer(ISSUER)
                .setIssuedAt(new Date())
                .setExpiration(calculateAccessTokenExpiration(now))
                .setAudience(authUser.getUsername())
                .claim(AUTHORITY_KEY, getAuthorities(authentication))
                .claim(USER_ID, authUser.getId())
                .compact();
    }

    public String generateAccessToken(String email, String authorities, Long userId) {
        long now = new Date().getTime();

        return Jwts.builder()
                .signWith(secretKey, SIGNATURE_ALGORITHM)
                .setHeader(setTokenHeader())
                .setSubject(email)
                .setIssuer(ISSUER)
                .setIssuedAt(new Date())
                .setExpiration(calculateAccessTokenExpiration(now))
                .setAudience(email)
                .claim(AUTHORITY_KEY, authorities)
                .claim(USER_ID, userId)
                .compact();
    }

    private String getAuthorities(Authentication authentication) {
        return authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(COMMA));
    }

    private Date calculateAccessTokenExpiration(long now) {
        System.out.println(new Date(now + accessTokenExpiration));
        return new Date(now + accessTokenExpiration);
    }

    public String generateRefreshToken(Authentication authentication) {
        long now = new Date().getTime();
        AuthenticationUser authUser = (AuthenticationUser) authentication.getPrincipal();

        return Jwts.builder()
                .signWith(secretKey, SIGNATURE_ALGORITHM)
                .setHeader(setTokenHeader())
                .setSubject(authUser.getEmail())
                .setIssuer(ISSUER)
                .setIssuedAt(new Date())
                .setExpiration(calculateRefreshTokenExpiration(now))
                .claim(AUTHORITY_KEY, getAuthorities(authentication))
                .claim(USER_ID, authUser.getId())
                .compact();
    }

    public String generateRefreshToken(String email, String authorities, Long userId) {
        long now = new Date().getTime();

        return Jwts.builder()
                .signWith(secretKey, SIGNATURE_ALGORITHM)
                .setHeader(setTokenHeader())
                .setSubject(email)
                .setIssuer(ISSUER)
                .setIssuedAt(new Date())
                .setExpiration(calculateRefreshTokenExpiration(now))
                .claim(AUTHORITY_KEY, authorities)
                .claim(USER_ID, userId)
                .compact();
    }

    private Date calculateRefreshTokenExpiration(long now) {
        return new Date(now + refreshTokenExpiration);
    }

    public ResponseCookie setRefreshTokenInCookie(String refreshToken) {

        return ResponseCookie.from(REFRESH_TOKEN_HEADER, refreshToken)
                .httpOnly(false)
                .secure(false)
                .sameSite("None")
                .maxAge(refreshTokenExpiration * THOUSAND_SECONDS)
                .path("/")
                .build();
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

    public Optional<String> extractAuthoritiesInClaim(String token) {
        return extractClaim(token, AUTHORITY_KEY, String.class);
    }

    public Optional<Long> extractUserIdInClaim(String token) {
        return extractClaim(token, USER_ID, Long.class);
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
