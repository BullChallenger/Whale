package com.example.whale.security.filter;

import com.example.whale.domain.RefreshToken;
import com.example.whale.repository.RefreshTokenRepository;
import com.example.whale.security.provider.JwtProvider;
import com.example.whale.service.LoginService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final LoginService loginService;

    @Value("${jwt.access.header}")
    private String ACCESS_TOKEN_HEADER;
    @Value("${jwt.refresh.expiration}")
    private Long REFRESH_TOKEN_EXPIRATION;
    private static final int VALID = 1;
    private static final int EXPIRED = 0;
    private static final int INVALID = -1;
    private static final String EMPTY_EMAIL = "EMPTY_EMAIL";
    private static final String[] NO_CHECK_URI_LIST = {
            "/", "/api/login", "/api/users/signUp", "/error"
    };

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException
    {
        if (Arrays.asList(NO_CHECK_URI_LIST).contains(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessTokenInRequest = jwtProvider.extractAccessToken(request)
                .orElseThrow(() -> new JwtException("AccessToken 이 존재하지 않습니다."));
        String refreshTokenInRequest = jwtProvider.extractRefreshToken(request)
                .orElseThrow(() -> new JwtException("RefreshToken 이 존재하지 않습니다."));

        Long userId;
        String email = EMPTY_EMAIL;
        String authorities;

        Optional<RefreshToken> refreshTokenInRedis = Optional.empty();

        try {
            refreshTokenInRedis = refreshTokenRepository.findById(refreshTokenInRequest);
        } catch (InvalidDataAccessApiUsageException e) {
            if (jwtProvider.isTokenValid(accessTokenInRequest) == VALID) {
                email = jwtProvider.extractEmailInSubject(accessTokenInRequest).orElseThrow(
                        () -> new JwtException("잘못된 Jwt 형식 입니다.")
                );
                authorities = jwtProvider.extractAuthoritiesInClaim(accessTokenInRequest).orElseThrow(
                        () -> new JwtException("잘못된 Jwt 형식 입니다.")
                );
                userId = jwtProvider.extractUserIdInClaim(accessTokenInRequest).orElseThrow(
                        () -> new JwtException("잘못된 Jwt 형식 입니다.")
                );

                String reIssuedRefreshToken = jwtProvider.generateRefreshToken(email, authorities, userId);

                response.setHeader(HttpHeaders.SET_COOKIE, jwtProvider.setRefreshTokenInCookie(reIssuedRefreshToken).toString());
                refreshTokenRepository.save(new RefreshToken(reIssuedRefreshToken, String.valueOf(userId), REFRESH_TOKEN_EXPIRATION));
            } else {
                throw new JwtException("재로그인 필요");
            }
        }

        if (jwtProvider.isTokenValid(accessTokenInRequest) == EXPIRED) {
            // RefreshToken 확인 후 재발급 여부 결정
            if (refreshTokenInRedis.isPresent()) {
                String refreshToken = refreshTokenInRedis.get().getRefreshToken();
                if (isSameRefreshToken(refreshToken, refreshTokenInRequest)) {
                    if (jwtProvider.isTokenValid(refreshToken) == VALID) {
                        // AccessToken 재발급
                        email = jwtProvider.extractEmailInSubject(refreshToken).orElseThrow(
                                () -> new JwtException("잘못된 Jwt 형식 입니다.")
                        );
                        authorities = jwtProvider.extractAuthoritiesInClaim(refreshToken).orElseThrow(
                                () -> new JwtException("잘못된 Jwt 형식 입니다.")
                        );
                        userId = jwtProvider.extractUserIdInClaim(refreshToken).orElseThrow(
                                () -> new JwtException("잘못된 Jwt 형식 입니다.")
                        );

                        accessTokenInRequest = jwtProvider.generateAccessToken(email, authorities, userId);
                        response.setHeader(ACCESS_TOKEN_HEADER, accessTokenInRequest);
                    } else {
                        // TODO: 재로그인 필요
                        throw new JwtException("재로그인 필요");
                    }
                }
            }
        } else if (jwtProvider.isTokenValid(accessTokenInRequest) == INVALID) {
            throw new JwtException("유효하지 않은 AccessToken 입니다.");
        }

        if (email.equals(EMPTY_EMAIL)) {
            Optional<String> boxingEmail = jwtProvider.extractEmailInSubject(accessTokenInRequest);
            if (boxingEmail.isPresent()) {
                email = boxingEmail.get();
            }
        }

        UserDetails userDetails = loginService.loadUserByUsername(email);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        doFilter(request, response, filterChain);
    }

    private boolean isSameRefreshToken(String refreshToken, String refreshTokenInRequest) {
        return refreshToken.equals(refreshTokenInRequest);
    }

}
