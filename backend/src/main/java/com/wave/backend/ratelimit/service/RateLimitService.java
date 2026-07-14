package com.wave.backend.ratelimit.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimitService {

    private final StringRedisTemplate redisTemplate;

    public RateLimitService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean isAllowed(
            String key,
            int maxRequests,
            long windowSeconds
    ) {

        Long currentRequests = redisTemplate.opsForValue().increment(key);

        if (currentRequests == null) {
            return false;
        }

        if (currentRequests == 1) {
            redisTemplate.expire(
                    key,
                    Duration.ofSeconds(windowSeconds)
            );
        }

        return currentRequests <= maxRequests;
    }

    public long getRemainingTime(String key) {

        Long ttl = redisTemplate.getExpire(key);

        return ttl == null ? 0 : ttl;

    }

}