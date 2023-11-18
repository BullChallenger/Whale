package com.example.whale.security.handler;

import com.example.whale.security.provider.JwtProvider;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;

    @Value("${jwt.access.header}")
    private String ACCESS_TOKEN_HEADER;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_OK);
        String accessToken = jwtProvider.generateAccessToken(authentication);
        response.setHeader(ACCESS_TOKEN_HEADER, accessToken);

        String refreshToken = jwtProvider.generateRefreshToken(authentication);
        ResponseCookie cookie = jwtProvider.setRefreshTokenInCookie(refreshToken);
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
