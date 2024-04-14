package com.microservice.statefulauthapi.core.controller;

import com.microservice.statefulauthapi.core.dto.AuthRequest;
import com.microservice.statefulauthapi.core.dto.AuthUserResponse;
import com.microservice.statefulauthapi.core.dto.TokenDTO;
import com.microservice.statefulauthapi.core.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@AllArgsConstructor
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("login")
    public TokenDTO login(@RequestBody AuthRequest request) {
        return authService.login(request);
    }

    @PostMapping("token/validate")
    public TokenDTO validateToken(@RequestHeader String token) {
        return authService.validateToken(token);
    }

    @PostMapping("logout")
    public HashMap<String, String> logout(@RequestHeader String token) {
        authService.logout(token);
        var response = new HashMap<String, String>();
        var ok = HttpStatus.OK;
        response.put("status", ok.name());
        response.put("code", String.valueOf(ok.value()));
        return response;
    }

    @GetMapping("user")
    public AuthUserResponse getAuthenticatedUser(@RequestHeader String token) {
        return authService.getAuthenticatedUser(token);
    }
}
