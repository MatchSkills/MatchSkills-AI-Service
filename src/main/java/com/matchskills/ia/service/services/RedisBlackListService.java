package com.matchskills.ia.service.services;

import com.matchskills.ia.service.exceptions.customs.token.TokenInBlackListException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisBlackListService {

    private final StringRedisTemplate stringRedisTemplate;

    public RedisBlackListService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void verifyIfBlacklisted(String jwtId) {

        if (stringRedisTemplate.opsForValue().get(jwtId) != null){
            throw new TokenInBlackListException();
        }
    }

}
