package com.project.documentflow.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.propertyeditors.ReaderEditor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenCacheService {
    private final RedisTemplate<String, Object> redisTemplate;

    public void saveToken(String key, Object value, long ttlSecond) {
        redisTemplate.opsForValue().set(key, value, ttlSecond, TimeUnit.SECONDS);
    }

    public Object getToken(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteToken(String key) {
        redisTemplate.delete(key);
    }
}
