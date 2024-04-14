package com.microservice.statefulanyapi.core.client;

import com.microservice.statefulanyapi.core.dto.AuthUserResponse;
import com.microservice.statefulanyapi.core.dto.TokenDTO;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/api/auth")
public interface TokenClient {

    @PostExchange("token/validate")
    TokenDTO validateToken(@RequestHeader String token);

    @GetExchange("user")
    AuthUserResponse getAuthenticatedUser(@RequestHeader String token);
}
