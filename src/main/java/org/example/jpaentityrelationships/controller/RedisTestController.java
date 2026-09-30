package org.example.jpaentityrelationships.controller;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/redis")
public class RedisTestController {

    private final RedisTemplate<String, Object> redisTemplate;

    // Added for Redis Health Check
    private final RedisConnectionFactory redisConnectionFactory;

    public RedisTestController(
            RedisTemplate<String, Object> redisTemplate,
            RedisConnectionFactory redisConnectionFactory) {

        this.redisTemplate = redisTemplate;

        // Added for Redis Health Check
        this.redisConnectionFactory =
                redisConnectionFactory;
    }

    @PostMapping("/test")
    public String setValue(
            @RequestParam String key,
            @RequestParam String value) {

        redisTemplate.opsForValue().set(
                key,
                value,
                10,
                TimeUnit.MINUTES
        );

        return "Redis value stored successfully";
    }

    @GetMapping("/test")
    public Object getValue(
            @RequestParam String key) {

        Object value =
                redisTemplate.opsForValue().get(key);

        if (value == null) {
            return "Cache Miss";
        }

        return value;
    }

    @DeleteMapping("/test")
    public String deleteValue(
            @RequestParam String key) {

        redisTemplate.delete(key);

        return "Redis key deleted successfully";
    }

    // =========================================================
    // REDIS HEALTH CHECK - ADDED
    // =========================================================

    @GetMapping("/health")
    public String redisHealth() {

        try {

            String result =
                    redisConnectionFactory
                            .getConnection()
                            .ping();

            return "Redis is UP - " + result;

        } catch (Exception e) {

            return "Redis is DOWN";
        }
    }
}