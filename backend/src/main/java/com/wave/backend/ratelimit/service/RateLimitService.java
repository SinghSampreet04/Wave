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

        System.out.println();
        System.out.println("========== RATE LIMIT ==========");
        System.out.println("Key: " + key);
        System.out.println("Max Requests: " + maxRequests);
        System.out.println("Window Seconds: " + windowSeconds);

        try {

            Long currentRequests = redisTemplate.opsForValue().increment(key);

            System.out.println("Current Requests: " + currentRequests);

            if (currentRequests == null) {
                System.out.println("Redis returned NULL.");
                System.out.println("===============================");
                return false;
            }

            if (currentRequests == 1) {

                boolean expirationSet = Boolean.TRUE.equals(
                        redisTemplate.expire(
                                key,
                                Duration.ofSeconds(windowSeconds)
                        )
                );

                System.out.println("Expiration Set: " + expirationSet);

            }

            Long ttl = redisTemplate.getExpire(key);

            System.out.println("TTL: " + ttl);

            boolean allowed = currentRequests <= maxRequests;

            System.out.println("Allowed: " + allowed);
            System.out.println("===============================");
            System.out.println();

            return allowed;

        } catch (Exception ex) {

            System.out.println("Rate Limit Exception:");
            ex.printStackTrace();

            System.out.println("===============================");
            System.out.println();

            throw ex;

        }

    }

    public long getRemainingTime(String key) {

        Long ttl = redisTemplate.getExpire(key);

        System.out.println("Remaining TTL for key '" + key + "' = " + ttl);

        return ttl == null ? 0 : ttl;

    }

}