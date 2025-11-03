package com.project.documentflow.service;

import com.project.documentflow.dto.AuthResponse;
import com.project.documentflow.dto.LoginRequest;
import com.project.documentflow.dto.RegisterRequest;

public interface IAuthService {
    public String register(RegisterRequest registerRequest);

    public AuthResponse login(LoginRequest loginRequest);

}
