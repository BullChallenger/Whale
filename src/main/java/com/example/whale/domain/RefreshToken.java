package com.example.whale.domain;

import java.io.Serializable;
import org.springframework.data.redis.core.TimeToLive;

import javax.persistence.Id;

public class RefreshToken implements Serializable {

    @Id
    private String refreshToken;
    private Long userId;
    @TimeToLive
    private long ttl;

    public RefreshToken(String refreshToken, String userId, long ttl) {
        this.refreshToken = refreshToken;
        this.userId = Long.valueOf(userId);
        this.ttl = ttl;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Long getUserId() {
        return userId;
    }

}
