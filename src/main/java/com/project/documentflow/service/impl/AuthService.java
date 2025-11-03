package com.project.documentflow.service.impl;

import com.project.documentflow.dto.AuthResponse;
import com.project.documentflow.dto.LoginRequest;
import com.project.documentflow.dto.RegisterRequest;
import com.project.documentflow.entity.RefreshToken;
import com.project.documentflow.entity.User;
import com.project.documentflow.repository.RefreshTokenRepository;
import com.project.documentflow.repository.UserRepository;
import com.project.documentflow.security.JwtService;
import com.project.documentflow.service.IAuthService;
import com.project.documentflow.service.IRefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class AuthService implements IAuthService {
    private final UserRepository userRepository;
    private final IRefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("This Email exist!");
        }
        User user = User.builder()
                .email(registerRequest.getEmail())
                .role(registerRequest.getRole())
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))

                .build();


        userRepository.save(user);


        return "You successfully registered!";
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid email or password!");
        }
        User user = userRepository.findUserByEmail(loginRequest.getEmail()).orElseThrow(() -> new RuntimeException("This user not exist!"));
        String accessToken = jwtService.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return new AuthResponse(accessToken, refreshToken.getToken());
    }
}
