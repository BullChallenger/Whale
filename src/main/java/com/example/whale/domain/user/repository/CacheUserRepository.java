package com.example.whale.domain.user.repository;

import com.example.whale.domain.user.entity.UserEntity;
import com.example.whale.domain.user.model.AuthenticationUser;
import com.example.whale.global.util.RoleUtil;
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
                roleUtil.addAllAuthorities(userEntity.getRoles())
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
