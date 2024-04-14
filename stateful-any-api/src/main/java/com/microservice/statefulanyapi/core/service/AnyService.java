package com.microservice.statefulanyapi.core.service;

import com.microservice.statefulanyapi.core.dto.AnyResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AnyService {

    private final TokenService tokenService;

    public AnyResponse getData(String token) {
        tokenService.validateToken(token);
        var authUser = tokenService.getAuthenticatedUser(token);
        var ok = HttpStatus.OK;
        return new AnyResponse(ok.name(), ok.value(), authUser);
    }
}
