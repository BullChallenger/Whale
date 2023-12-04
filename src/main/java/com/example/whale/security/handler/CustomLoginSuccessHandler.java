package com.example.whale.security.handler;

import com.example.whale.domain.RefreshToken;
import com.example.whale.domain.UserEntity;
import com.example.whale.dto.LoginDTO.LoginResponseDTO;
import com.example.whale.dto.ResponseDTO;
import com.example.whale.repository.RefreshTokenRepository;
import com.example.whale.repository.UserRepository;
import com.example.whale.security.provider.JwtProvider;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.persistence.EntityNotFoundException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.access.header}")
    private String ACCESS_TOKEN_HEADER;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_OK);
        String accessToken = jwtProvider.generateAccessToken(authentication);
        response.setHeader(ACCESS_TOKEN_HEADER, accessToken);

        UserEntity userEntity = userRepository.findByEmail(authentication.getName()).orElseThrow(
                () -> new EntityNotFoundException("해당 유저가 존재하지 않습니다.")
        );

        String refreshToken = jwtProvider.generateRefreshToken(authentication);
        refreshTokenRepository.save(new RefreshToken(refreshToken, userEntity.getId()));
        ResponseCookie cookie = jwtProvider.setRefreshTokenInCookie(refreshToken);
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        String loginSuccessUserInfo = objectMapper.writeValueAsString(ResponseDTO.ok(LoginResponseDTO.from(userEntity)));

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(loginSuccessUserInfo);
        response.getWriter().flush();
        response.getWriter().close();
    }

}
