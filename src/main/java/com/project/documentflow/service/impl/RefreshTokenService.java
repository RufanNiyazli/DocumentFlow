package com.project.documentflow.service.impl;

import com.project.documentflow.entity.RefreshToken;
import com.project.documentflow.entity.User;
import com.project.documentflow.repository.RefreshTokenRepository;
import com.project.documentflow.service.IRefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService implements IRefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedAt(LocalDateTime.now());
        refreshToken.setExpiredAt(LocalDateTime.now().plusHours(2));
        refreshToken.setUser(user);
        refreshTokenRepository.save(refreshToken);


        return refreshToken;
    }
}
