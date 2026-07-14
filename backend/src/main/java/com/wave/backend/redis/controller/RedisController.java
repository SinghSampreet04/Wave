package com.wave.backend.redis.controller;

import com.wave.backend.redis.service.RedisService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/redis")
public class RedisController {

    private final RedisService redisService;

    public RedisController(
            RedisService redisService
    ) {
        this.redisService = redisService;
    }

    @PostMapping("/set")
    public String setValue(
            @RequestParam String key,
            @RequestParam String value
    ) {

        redisService.save(
                key,
                value
        );

        return "Saved successfully.";

    }

    @GetMapping("/get")
    public String getValue(
            @RequestParam String key
    ) {

        String value = redisService.get(key);

        if (value == null) {
            return "Key not found.";
        }

        return value;

    }

    @DeleteMapping("/delete")
    public String deleteValue(
            @RequestParam String key
    ) {

        redisService.delete(key);

        return "Deleted successfully.";

    }

}