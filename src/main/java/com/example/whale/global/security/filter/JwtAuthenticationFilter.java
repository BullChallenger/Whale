package com.example.whale.global.security.filter;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.whale.global.security.provider.JwtProvider;
import com.example.whale.global.security.repository.RefreshTokenRepository;
import com.example.whale.global.security.service.LoginService;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final LoginService loginService;

    @Value("${jwt.access.header}")
    private String ACCESS_TOKEN_HEADER;

    private static final int EXPIRED = 0;
    private static final int INVALID = -1;
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

        // Refresh Token 을 저장할 때 Key 값이 이메일인 경우
        if (jwtProvider.isTokenValid(accessTokenInRequest) == EXPIRED) {
            String reIssuedAccessToken = ifAccessTokenIsExpired(accessTokenInRequest);
            response.setHeader(ACCESS_TOKEN_HEADER, reIssuedAccessToken);
        } else if (jwtProvider.isTokenValid(accessTokenInRequest) == INVALID) {
            throw new JwtException("유효하지 않은 액세스 토큰입니다. 재로그인 필요");
        }

        UserDetails userDetails = loginService.loadUserByUsername(
                jwtProvider.extractEmailInSubject(accessTokenInRequest).get()
        );

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
                null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        doFilter(request, response, filterChain);
    }

    private String ifAccessTokenIsExpired(String accessTokenInRequest) {
        String email = jwtProvider.extractEmailInSubject(accessTokenInRequest).orElseThrow(
                () -> new JwtException("잘못된 Jwt 형식 입니다.")
        );

        if (!refreshTokenRepository.isRefreshTokenExists(email)) {
            throw new JwtException("재로그인 필요");
        }

		return jwtProvider.generateAccessToken(
                jwtProvider.extractEmailInSubject(accessTokenInRequest).orElseThrow(),
                jwtProvider.extractAuthoritiesInClaim(accessTokenInRequest).orElseThrow(),
                jwtProvider.extractUserIdInClaim(accessTokenInRequest).orElseThrow()
        );
    }

}
