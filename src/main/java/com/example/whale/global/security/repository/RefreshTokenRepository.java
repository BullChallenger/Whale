package com.example.whale.global.security.repository;

import com.example.whale.global.security.entity.RefreshToken;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {

    @Value("${jwt.refresh.expiration}")
    private long refreshTokenExpiration;

    private final RedisTemplate<String, Object> redisTemplateForRefreshToken;

    public void save(String email, RefreshToken refreshToken) {
        ValueOperations<String, Object> valueOperations = redisTemplateForRefreshToken.opsForValue();
        valueOperations.set(email, refreshToken);
        redisTemplateForRefreshToken.expire(refreshToken.getRefreshToken(), refreshTokenExpiration, TimeUnit.SECONDS);
    }

    public Optional<RefreshToken> findById(String email) {
        ValueOperations<String, Object> valueOperations = redisTemplateForRefreshToken.opsForValue();
        Optional<RefreshToken> refreshToken = (Optional<RefreshToken>) valueOperations.get(email);

        if (refreshToken.isEmpty()) {
            return Optional.empty();
        }

        return refreshToken;
    }

    public boolean isRefreshTokenExists(String email) {
        return redisTemplateForRefreshToken.hasKey(email);
    }

}
