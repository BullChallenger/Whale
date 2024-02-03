package com.example.whale.service;

import com.example.whale.domain.UserEntity;
import com.example.whale.dto.user.AuthenticationUser;
import com.example.whale.repository.CacheUserRepository;
import com.example.whale.repository.UserRepository;
import com.example.whale.util.RoleUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginService implements UserDetailsService  {

    private static final String NOT_FOUND_ENTITY_EXCEPTION = "사용자를 찾을 수 없습니다.";

    private final CacheUserRepository cacheUserRepository;
    private final UserRepository userRepository;
    private final RoleUtil roleUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AuthenticationUser> cacheAuthenticationUser = cacheUserRepository.findCustomerByKey("USER: " + username);

        if (cacheAuthenticationUser.isPresent()) {
            return cacheAuthenticationUser.get();
        }

        Optional<UserEntity> op = userRepository.findByEmail(username);
        if (op.isPresent()) {
            cacheUserRepository.saveAuthenticationUserInCache(op.get());
            return AuthenticationUser.of(op.get(), roleUtil.addAuthorities(op.get().getRole()));
        }

        throw new BadCredentialsException(NOT_FOUND_ENTITY_EXCEPTION);
    }

}
