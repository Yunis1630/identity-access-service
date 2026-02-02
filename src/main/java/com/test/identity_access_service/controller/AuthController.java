package com.test.identity_access_service.controller;

import com.test.identity_access_service.dto.LoginDTO;
import com.test.identity_access_service.dto.RefreshTokenDTO;
import com.test.identity_access_service.payload.LoginPayload;
import com.test.identity_access_service.payload.RefreshTokenPayload;
import com.test.identity_access_service.payload.RegisterPayload;
import com.test.identity_access_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public void register(@RequestBody RegisterPayload payload) {
        authService.register(payload);
    }

    @PostMapping("/login")
    public LoginDTO login(@RequestBody LoginPayload payload) {
        return authService.login(payload);
    }

    @PostMapping("/refresh-token")
    public RefreshTokenDTO refreshToken(@RequestBody RefreshTokenPayload payload) {
        return authService.refreshToken(payload);
    }

}