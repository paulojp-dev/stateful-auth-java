package com.microservice.statefulauthapi.core.service;

import com.microservice.statefulauthapi.core.dto.AuthRequest;
import com.microservice.statefulauthapi.core.dto.AuthUserResponse;
import com.microservice.statefulauthapi.core.dto.TokenDTO;
import com.microservice.statefulauthapi.core.model.User;
import com.microservice.statefulauthapi.core.repository.UserRepository;
import com.microservice.statefulauthapi.infra.exception.AuthenticationException;
import com.microservice.statefulauthapi.infra.exception.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public TokenDTO login(AuthRequest request) {
        var user = findByUsername(request.username());
        var accessToken = tokenService.createToken(user.getUsername());
        validatePassword(request.password(), user.getPassword());
        return new TokenDTO(accessToken);
    }

    private User findByUsername(String username) {
        return userRepository
            .findByUsername(username)
            .orElseThrow(() -> new ValidationException("User not found!"));
    }

    private void validatePassword(String password, String encodedPassword) {
        if (isEmpty(password)) {
            throw new ValidationException("The password must be informed!");
        }
        if (!passwordEncoder.matches(password, encodedPassword)) {
            throw new ValidationException("Invalid password!");
        }
    }

    public TokenDTO validateToken(String token) {
        validateExistingToken(token);
        var isValid = tokenService.validateToken(token);
        if (isValid) {
            return new TokenDTO(token);
        }
        throw new AuthenticationException("Invalid token!");
    }

    public void logout(String token) {
        tokenService.deleteRedisToken(token);
    }

    public AuthUserResponse getAuthenticatedUser(String token) {
        var tokenData = tokenService.getTokenData(token);
        var user = findByUsername(tokenData.username());
        return new AuthUserResponse(user.getId(), user.getUsername());
    }

    private void validateExistingToken(String token) {
        if (isEmpty(token)) {
            throw new ValidationException("The access token must be informed!");
        }
    }
}
