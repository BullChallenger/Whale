package com.example.whale.global.security.handler;

import com.example.whale.global.security.entity.RefreshToken;
import com.example.whale.domain.common.dto.ResponseDTO;
import com.example.whale.domain.user.model.AuthenticationUser;
import com.example.whale.domain.user.dto.LoginDTO.LoginResponseDTO;
import com.example.whale.global.security.repository.RefreshTokenRepository;
import com.example.whale.global.security.provider.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
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
        refreshTokenRepository.save(
                authUser.getEmail(),
                new RefreshToken(refreshToken, String.valueOf(authUser.getId()), refreshTokenExpiration)
        );

        String loginSuccessUserInfo = objectMapper.writeValueAsString(ResponseDTO.ok(LoginResponseDTO.from(authUser)));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(loginSuccessUserInfo);
        response.getWriter().flush();
        response.getWriter().close();
    }

}
