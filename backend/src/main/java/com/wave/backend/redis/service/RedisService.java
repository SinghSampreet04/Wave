package com.wave.backend.redis.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    private final StringRedisTemplate stringRedisTemplate;

    public RedisService(
            StringRedisTemplate stringRedisTemplate
    ) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void save(
            String key,
            String value
    ) {

        stringRedisTemplate
                .opsForValue()
                .set(key, value);

    }

    public String get(
            String key
    ) {

        return stringRedisTemplate
                .opsForValue()
                .get(key);

    }

    public void delete(
            String key
    ) {

        stringRedisTemplate.delete(key);

    }

}