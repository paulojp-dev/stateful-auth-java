package com.microservice.statefulauthapi.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.statefulauthapi.core.dto.TokenData;
import com.microservice.statefulauthapi.infra.exception.AuthenticationException;
import com.microservice.statefulauthapi.infra.exception.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@AllArgsConstructor
public class TokenService {

    private static final String EMPTY_SPACE = " ";
    private static final Integer TOKEN_INDEX = 1;
    private static final Long ONE_DAY_IN_SECONDS = 86400L;

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public String createToken(String username) {
        var accessToken = UUID.randomUUID().toString();
        var data = new TokenData(username);
        var jsonData = getJsonData(data);
        redisTemplate.opsForValue().set(accessToken, jsonData);
        redisTemplate.expireAt(accessToken, Instant.now().plusSeconds(ONE_DAY_IN_SECONDS));
        return accessToken;
    }

    private String getJsonData(Object payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (Exception e) {
            return "";
        }
    }

    public TokenData getTokenData(String token) {
        var accessToken = extractToken(token);
        var jsonData = getRedisTokenValue(accessToken);
        try {
            return objectMapper.readValue(jsonData, TokenData.class);
        } catch (Exception e) {
            throw new AuthenticationException("Error for extracting the authenticated user: " + e.getMessage());
        }
    }

    public boolean validateToken(String token) {
        var accessToken = extractToken(token);
        var jsonData = getRedisTokenValue(accessToken);
        return !isEmpty(jsonData);
    }

    private String getRedisTokenValue(String token) {
        return redisTemplate.opsForValue().get(token);
    }

    public void deleteRedisToken(String token) {
        var accessToken = extractToken(token);
        redisTemplate.delete(accessToken);
    }

    private String extractToken(String token) {
        if (isEmpty(token)) {
            throw new ValidationException("The access token was not informed.");
        }
        if (token.contains(EMPTY_SPACE)) {
            return token.split(EMPTY_SPACE)[TOKEN_INDEX];
        }
        return token;
    }
}
