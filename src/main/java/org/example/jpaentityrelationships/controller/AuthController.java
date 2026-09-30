package org.example.jpaentityrelationships.controller;

import jakarta.validation.Valid;

import org.example.jpaentityrelationships.dto.LoginRequest;
import org.example.jpaentityrelationships.dto.RefreshTokenRequest;
import org.example.jpaentityrelationships.dto.RegisterRequest;
import org.example.jpaentityrelationships.dto.RegisterResponse;
import org.example.jpaentityrelationships.dto.TokenResponse;
import org.example.jpaentityrelationships.service.AuthService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(
            AuthService authService) {

        this.authService = authService;
    }

    // =====================================================
    // REGISTER
    // =====================================================

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        RegisterResponse response =
                authService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =====================================================
    // LOGIN
    // =====================================================

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(
            @Valid @RequestBody LoginRequest request) {

        TokenResponse response =
                authService.login(request);

        return ResponseEntity.ok(response);
    }

    // =====================================================
    // REFRESH TOKEN
    // =====================================================

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(
            @Valid @RequestBody RefreshTokenRequest request) {

        TokenResponse response =
                authService.refresh(
                        request.getRefreshToken()
                );

        return ResponseEntity.ok(response);
    }

    // =====================================================
    // LOGOUT
    // =====================================================

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @Valid @RequestBody RefreshTokenRequest request) {

        authService.logout(
                request.getRefreshToken()
        );

        return ResponseEntity.noContent().build();
    }
}