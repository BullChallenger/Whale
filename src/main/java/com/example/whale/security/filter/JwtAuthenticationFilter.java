package com.example.whale.security.filter;

import com.example.whale.domain.RefreshToken;
import com.example.whale.repository.RefreshTokenRepository;
import com.example.whale.security.provider.JwtProvider;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.access.header}")
    private String ACCESS_TOKEN_HEADER;
    private static final int VALID = 1;
    private static final int EXPIRED = 0;
    private static final int INVALID = 0;
    private static final String[] NO_CHECK_URI_LIST = {"/", "/api/login", "/api/users/signUp"};

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException
    {
        String requestURI = request.getRequestURI();

        if (Arrays.asList(NO_CHECK_URI_LIST).contains(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessTokenInRequest = jwtProvider.extractAccessToken(request)
                .orElseThrow(() -> new JwtException("AccessToken 이 존재하지 않습니다."));
        String refreshTokenInRequest = jwtProvider.extractRefreshToken(request)
                .orElseThrow(() -> new JwtException("RefreshToken 이 존재하지 않습니다."));
        Optional<RefreshToken> refreshTokenInRedis = refreshTokenRepository.findById(refreshTokenInRequest);

        if (jwtProvider.isTokenValid(accessTokenInRequest) == EXPIRED) {
            // TODO: RefreshToken 확인 후 재발급 여부 결정
            if (refreshTokenInRedis.isPresent()) {
                String refreshToken = refreshTokenInRedis.get().getRefreshToken();
                if (isSameRefreshToken(refreshToken, refreshTokenInRequest)) {
                    if (jwtProvider.isTokenValid(refreshToken) == VALID) {
                        // TODO: AccessToken 재발급
                        String email = jwtProvider.extractEmailInSubject(refreshToken).orElseThrow(
                                () -> new JwtException("잘못된 Jwt 형식 입니다.")
                        );
                        String authorities = jwtProvider.extractAuthoritiesInClaim(refreshToken).orElseThrow(
                                () -> new JwtException("잘못된 Jwt 형식 입니다.")
                        );
                        String accessToken = jwtProvider.generateAccessToken(email, authorities);

                        response.setHeader(ACCESS_TOKEN_HEADER, accessToken);
                    } else {
                        // TODO: 재로그인 필요
                    }
                }
            }
        } else if (jwtProvider.isTokenValid(accessTokenInRequest) == INVALID) {
            throw new JwtException("유효하지 않은 AccessToken 입니다.");
        }

        doFilter(request, response, filterChain);
    }

    private boolean isSameRefreshToken(String refreshToken, String refreshTokenInRequest) {
        return refreshToken.equals(refreshTokenInRequest);
    }

}
