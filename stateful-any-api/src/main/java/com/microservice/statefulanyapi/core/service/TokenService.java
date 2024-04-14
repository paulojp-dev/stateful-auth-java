package com.microservice.statefulanyapi.core.service;

import com.microservice.statefulanyapi.core.client.TokenClient;
import com.microservice.statefulanyapi.core.dto.AuthUserResponse;
import com.microservice.statefulanyapi.infra.exception.AuthenticationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class TokenService {

    private final TokenClient tokenClient;

    public void validateToken(String token) {
        try {
            log.info("Sending request for validate token {}", token);
            var response = tokenClient.validateToken(token);
            log.info("Token is valid: {}", response.token());
        } catch (Exception e) {
            throw new AuthenticationException("Auth error: " + e.getMessage());
        }
    }

    public AuthUserResponse getAuthenticatedUser(String token) {
        try {
            log.info("Sending request to get auth user {}", token);
            var response = tokenClient.getAuthenticatedUser(token);
            log.info("Auth user found {} with token {}", response.toString(), token);
            return response;
        } catch (Exception e) {
            throw new AuthenticationException("Error to get authenticated user: " + e.getMessage());
        }
    }
}
