package com.richmond.whatsforlunch.users.controller;

import com.richmond.whatsforlunch.common.controller.StandardResponse;
import com.richmond.whatsforlunch.users.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller to generate token.
 */
@RestController
@RequestMapping("/api/v1/tokens")
@RequiredArgsConstructor
@Slf4j
public class TokenController {

    private final TokenService tokenService;

    /**
     * Generate JWT token
     * @param authentication user details
     * @return string token
     */
    @PostMapping
    public StandardResponse<ResponseUserToken> token(Authentication authentication) {
        log.debug("Token requested for user: '{}'", authentication.getName());
        String token = tokenService.generateToken(authentication);
        return new StandardResponse<>(new ResponseUserToken(token));
    }

}

record ResponseUserToken (String accessToken) { }
