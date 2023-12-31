package com.example.whale.security.handler;

import com.example.whale.domain.RefreshToken;
import com.example.whale.dto.ResponseDTO;
import com.example.whale.dto.user.AuthenticationUser;
import com.example.whale.dto.user.LoginDTO.LoginResponseDTO;
import com.example.whale.repository.RefreshTokenRepository;
import com.example.whale.security.provider.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.access.header}")
    private String ACCESS_TOKEN_HEADER;

    @Value("${jwt.refresh.expiration}")
    private long refreshTokenExpiration;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_OK);
        String accessToken = jwtProvider.generateAccessToken(authentication);
        response.setHeader(ACCESS_TOKEN_HEADER, accessToken);

        AuthenticationUser authUser = (AuthenticationUser) authentication.getPrincipal();

        String refreshToken = jwtProvider.generateRefreshToken(authentication);
        refreshTokenRepository.save(new RefreshToken(refreshToken, String.valueOf(authUser.getId()), refreshTokenExpiration));
        ResponseCookie cookie = jwtProvider.setRefreshTokenInCookie(refreshToken);
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        String loginSuccessUserInfo = objectMapper.writeValueAsString(ResponseDTO.ok(LoginResponseDTO.from(authUser)));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(loginSuccessUserInfo);
        response.getWriter().flush();
        response.getWriter().close();
    }

}
