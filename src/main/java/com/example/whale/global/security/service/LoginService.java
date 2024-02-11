package com.example.whale.global.security.service;

import com.example.whale.domain.user.entity.UserEntity;
import com.example.whale.domain.user.model.AuthenticationUser;
import com.example.whale.domain.user.repository.CacheUserRepository;
import com.example.whale.domain.user.repository.UserRepository;
import com.example.whale.global.util.RoleUtil;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
            return AuthenticationUser.of(op.get(), roleUtil.addAllAuthorities(op.get().getRoles()));
        }

        throw new BadCredentialsException(NOT_FOUND_ENTITY_EXCEPTION);
    }

}
