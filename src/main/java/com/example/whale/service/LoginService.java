package com.example.whale.service;

import com.example.whale.domain.UserEntity;
import com.example.whale.dto.AuthenticationUser;
import com.example.whale.dto.LoginDTO.LoginRequestDTO;
import com.example.whale.dto.LoginDTO.LoginResponseDTO;
import com.example.whale.repository.UserRepository;
import com.example.whale.util.RoleUtil;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService  {

    private static final String NOT_FOUND_ENTITY_EXCEPTION = "사용자를 찾을 수 없습니다.";
    private static final String BAD_CREDENTIAL_EXCEPTION = "올바르지 않은 비밀번호 입니다.";

    private final UserRepository userRepository;
    private final RoleUtil roleUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username).orElseThrow(
                () -> new EntityNotFoundException(NOT_FOUND_ENTITY_EXCEPTION)
        );

        return AuthenticationUser.of(
                userEntity.getId(),
                userEntity.getEmail(),
                userEntity.getNickname(),
                userEntity.getUsername(),
                userEntity.getPassword(),
                roleUtil.addAuthorities(userEntity.getRole())
        );
    }

    public LoginResponseDTO login(LoginRequestDTO dto) {
        UserEntity userEntity = userRepository.findByEmail(dto.getEmail()).orElseThrow(
                () -> new EntityNotFoundException(NOT_FOUND_ENTITY_EXCEPTION)
        );

        if (!userEntity.getPassword().equals(dto.getPassword())) {
            throw new BadCredentialsException(BAD_CREDENTIAL_EXCEPTION);
        }

        return LoginResponseDTO.from(userEntity);
    }

}
