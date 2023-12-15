package com.example.whale.service;

import com.example.whale.domain.UserEntity;
import com.example.whale.dto.user.AuthenticationUser;
import com.example.whale.repository.UserRepository;
import com.example.whale.util.RoleUtil;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginService implements UserDetailsService  {

    private static final String NOT_FOUND_ENTITY_EXCEPTION = "사용자를 찾을 수 없습니다.";

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

}
