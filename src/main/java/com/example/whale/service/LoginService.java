package com.example.whale.service;

import com.example.whale.domain.UserEntity;
import com.example.whale.dto.AuthenticationUser;
import com.example.whale.repository.UserRepository;
import com.example.whale.util.RoleUtil;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService  {

    private final UserRepository userRepository;
    private final RoleUtil roleUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username).orElseThrow(
                () -> new EntityNotFoundException("사용자를 찾을 수 없습니다.")
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
