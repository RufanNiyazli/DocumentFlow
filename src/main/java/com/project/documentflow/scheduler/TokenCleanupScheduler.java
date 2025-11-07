package com.project.documentflow.scheduler;

import com.project.documentflow.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TokenCleanupScheduler {
    private final RefreshTokenRepository refreshTokenRepository;

    //her bir saatdan bir yoxlyacaq varsa eger silcek
    @Scheduled(cron = "0 0 * * * *")
    public void deleteExpiredToken() {
        refreshTokenRepository.deleteAllByExpiredAtBefore(LocalDateTime.now());
    }
}
