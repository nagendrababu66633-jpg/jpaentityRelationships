package org.example.jpaentityrelationships.service;

import org.example.jpaentityrelationships.dto.LoginRequest;
import org.example.jpaentityrelationships.dto.RegisterRequest;
import org.example.jpaentityrelationships.dto.RegisterResponse;
import org.example.jpaentityrelationships.dto.TokenResponse;
import org.example.jpaentityrelationships.entity.RefreshToken;
import org.example.jpaentityrelationships.entity.User;
import org.example.jpaentityrelationships.repository.UserRepository;
import org.example.jpaentityrelationships.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            RefreshTokenService refreshTokenService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    // =====================================================
    // REGISTER
    // =====================================================

    @Transactional
    public RegisterResponse register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        if (request.getMobileNumber() != null
                && userRepository.existsByMobileNumber(
                request.getMobileNumber())) {

            throw new RuntimeException("Mobile number already exists");
        }

        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setMobileNumber(request.getMobileNumber());

        // Encrypt password
        user.setPasswordHash(
                passwordEncoder.encode(request.getPassword())
        );

        // Normal registration creates USER
        user.setRole("USER");

        user.setStatus("ACTIVE");
        user.setEnabled(true);

        User savedUser = userRepository.save(user);

        RegisterResponse response = new RegisterResponse();

        response.setId(savedUser.getId());
        response.setFirstName(savedUser.getFirstName());
        response.setLastName(savedUser.getLastName());
        response.setEmail(savedUser.getEmail());
        response.setMobileNumber(savedUser.getMobileNumber());
        response.setRole(savedUser.getRole());

        return response;
    }

    // =====================================================
    // LOGIN
    // =====================================================

    @Transactional
    public TokenResponse login(LoginRequest request) {

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invalid email or password"
                        )
                );

        // Check password
        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPasswordHash())) {

            throw new RuntimeException(
                    "Invalid email or password"
            );
        }

        // Check account
        if (!user.isEnabled()
                || !"ACTIVE".equalsIgnoreCase(
                user.getStatus())) {

            throw new RuntimeException(
                    "User account is disabled"
            );
        }

        // Generate access token
        String accessToken =
                jwtService.generateAccessToken(user);

        // Generate refresh token
        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(user);

        return new TokenResponse(
                accessToken,
                refreshToken.getToken(),
                "Bearer",
                900
        );
    }

    // =====================================================
    // REFRESH TOKEN
    // =====================================================

    @Transactional
    public TokenResponse refresh(String rawRefreshToken) {

        RefreshToken refreshToken =
                refreshTokenService.verifyRefreshToken(
                        rawRefreshToken
                );

        User user = refreshToken.getUser();

        String newAccessToken =
                jwtService.generateAccessToken(user);

        return new TokenResponse(
                newAccessToken,
                rawRefreshToken,
                "Bearer",
                900
        );
    }

    // =====================================================
    // LOGOUT
    // =====================================================

    @Transactional
    public void logout(String refreshToken) {

        refreshTokenService.revokeToken(
                refreshToken
        );
    }
}