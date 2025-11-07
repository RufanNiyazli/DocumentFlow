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
    private final TokenCacheService tokenCacheService;
    private static final long REFRESH_TOKEN_TTL_SECONDS = 2 * 60 * 60; // 2 saat

    @Override
    public RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedAt(LocalDateTime.now());
        refreshToken.setExpiredAt(LocalDateTime.now().plusHours(2));
        refreshToken.setUser(user);
        refreshTokenRepository.save(refreshToken);

        tokenCacheService.saveToken(
                "refresh_token:" + refreshToken.getToken(),
                user.getId(),
                REFRESH_TOKEN_TTL_SECONDS
        );


        return refreshToken;
    }

    @Override
    public boolean validateToken(String token) {
        Object userId = tokenCacheService.getToken("refresh_token:" + token);
        if (userId != null) return true;
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token).orElseThrow(() -> new RuntimeException("Token not found!"));
        if (refreshToken.getExpiredAt().isBefore(LocalDateTime.now())) return false;
        tokenCacheService.saveToken("refresh_token:" + token, refreshToken.getUser().getId(), REFRESH_TOKEN_TTL_SECONDS);


        return true;
    }
    // Eger Redis tokeni taparsa — cox suretli netice verecek.
    // Eger tapmazsa — DB-dan yoxlayacaq və etibarlıdırsa, Redis-ə yenidən add edəcek.
}
