package org.example.jpaentityrelationships.service;

import org.example.jpaentityrelationships.entity.RefreshToken;
import org.example.jpaentityrelationships.entity.User;
import org.example.jpaentityrelationships.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository) {

        this.refreshTokenRepository =
                refreshTokenRepository;
    }

    // =========================
    // CREATE REFRESH TOKEN
    // =========================

    public RefreshToken createRefreshToken(User user) {

        // Remove previous refresh token
        refreshTokenRepository.deleteByUser(user);

        RefreshToken refreshToken =
                new RefreshToken();

        String token =
                UUID.randomUUID().toString();

        refreshToken.setToken(token);

        refreshToken.setUser(user);

        refreshToken.setExpiryDate(
                Instant.now()
                        .plusMillis(refreshExpiration)
        );

        refreshToken.setRevoked(false);

        return refreshTokenRepository.save(
                refreshToken
        );
    }

    // =========================
    // VERIFY REFRESH TOKEN
    // =========================

    public RefreshToken verifyRefreshToken(
            String token) {

        RefreshToken refreshToken =
                refreshTokenRepository
                        .findByToken(token)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Invalid refresh token"
                                )
                        );

        if (refreshToken.isRevoked()) {

            throw new RuntimeException(
                    "Refresh token has been revoked"
            );
        }

        if (refreshToken
                .getExpiryDate()
                .isBefore(Instant.now())) {

            refreshTokenRepository
                    .delete(refreshToken);

            throw new RuntimeException(
                    "Refresh token has expired"
            );
        }

        return refreshToken;
    }

    // =========================
    // REVOKE TOKEN
    // =========================

    public void revokeToken(String token) {

        refreshTokenRepository
                .findByToken(token)
                .ifPresent(refreshToken -> {

                    refreshToken.setRevoked(true);

                    refreshTokenRepository.save(
                            refreshToken
                    );
                });
    }
}