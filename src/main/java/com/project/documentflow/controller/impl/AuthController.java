package com.project.documentflow.controller.impl;

import com.project.documentflow.controller.IAuthController;
import com.project.documentflow.dto.AuthResponse;
import com.project.documentflow.dto.LoginRequest;
import com.project.documentflow.dto.RefreshTokenRequest;
import com.project.documentflow.dto.RegisterRequest;
import com.project.documentflow.service.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements IAuthController {
    private final IAuthService authService;

    @Override
    @PostMapping("/public/register")
    public String register(@RequestBody RegisterRequest registerRequest) {
        authService.register(registerRequest);
        return "You successfully registered";
    }

    @Override
    @PostMapping("/public/refresh")
    public AuthResponse refresh(@RequestBody RefreshTokenRequest request) {
        return authService.refresh(request);
    }

    @Override
    @PostMapping("/public/login")
    public AuthResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }
}
