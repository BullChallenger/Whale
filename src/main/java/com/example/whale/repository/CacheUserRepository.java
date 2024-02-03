package com.example.whale.repository;

import com.example.whale.domain.UserEntity;
import com.example.whale.dto.user.AuthenticationUser;
import com.example.whale.util.RoleUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CacheUserRepository {

    private final RoleUtil roleUtil;
    private final RedisTemplate<String, Object> redisTemplateForAuthenticationUser;

    public void saveAuthenticationUserInCache(final UserEntity userEntity) {
        AuthenticationUser authenticationUser = AuthenticationUser.of(
                userEntity,
                roleUtil.addAuthorities(userEntity.getRole())
        );

        String key = generateCacheKey(userEntity.getEmail());

        if (findCustomerByKey(key).isPresent()) {
            redisTemplateForAuthenticationUser.delete(key);
        }

        log.info("Save AuthenticationUser in Redis {} : {}", key, authenticationUser);
        redisTemplateForAuthenticationUser.opsForValue().set(key, authenticationUser);
    }

    public Optional<AuthenticationUser> findCustomerByKey(String key) {
        return Optional.ofNullable((AuthenticationUser)redisTemplateForAuthenticationUser.opsForValue().get(key));
    }

    private String generateCacheKey(String email) {
        return "USER: " + email;
    }

}
