package org.example.jpaentityrelationships.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.example.jpaentityrelationships.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    // Value is in seconds
    @Value("${jwt.access-expiration}")
    private long accessExpiration;

    // =====================================================
    // GENERATE ACCESS TOKEN
    // =====================================================

    public String generateAccessToken(User user) {

        String role = user.getRole();

        if (role == null || role.isBlank()) {
            role = "USER";
        }

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("userId", user.getId())
                .claim("role", role.toUpperCase())
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + (accessExpiration * 1000)
                        )
                )
                .signWith(getSigningKey())
                .compact();
    }

    // =====================================================
    // EXTRACT EMAIL
    // =====================================================

    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    // =====================================================
    // EXTRACT ROLE
    // =====================================================

    public String extractRole(String token) {

        String role = getClaims(token)
                .get("role", String.class);

        if (role == null || role.isBlank()) {
            return "USER";
        }

        return role.toUpperCase();
    }

    // =====================================================
    // EXTRACT USER ID
    // =====================================================

    public Long extractUserId(String token) {

        Number userId = getClaims(token)
                .get("userId", Number.class);

        if (userId == null) {
            return null;
        }

        return userId.longValue();
    }

    // =====================================================
    // VALIDATE TOKEN
    // =====================================================

    public boolean isTokenValid(String token) {

        try {

            Claims claims = getClaims(token);

            Date expiration = claims.getExpiration();

            return expiration != null
                    && expiration.after(new Date());

        } catch (Exception e) {

            System.out.println(
                    "JWT VALIDATION ERROR = " + e.getMessage()
            );

            return false;
        }
    }

    // =====================================================
    // GET CLAIMS
    // =====================================================

    private Claims getClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // =====================================================
    // SIGNING KEY
    // =====================================================

    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }
}