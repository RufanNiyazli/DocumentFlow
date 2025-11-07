package com.project.documentflow.controller;

import com.project.documentflow.dto.AuthResponse;
import com.project.documentflow.dto.LoginRequest;
import com.project.documentflow.dto.RefreshTokenRequest;
import com.project.documentflow.dto.RegisterRequest;

public interface IAuthController {
    public String register(RegisterRequest registerRequest);

    public AuthResponse refresh(RefreshTokenRequest request);

    public AuthResponse login(LoginRequest loginRequest);
}
