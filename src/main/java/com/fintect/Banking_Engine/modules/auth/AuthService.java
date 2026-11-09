package com.fintect.Banking_Engine.modules.auth;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Manthan Jaswal (Roll: 3235)
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final Argon2PasswordEncoder passwordEncoder;

    // Notice we removed UserDetailsService from the constructor
    public AuthService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = new Argon2PasswordEncoder(16, 32, 1, 4096, 1);
    }

    public String register(RegisterRequestDto request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username already exists");
        }

        UserEntity user = UserEntity.builder()
                .username(request.username())
                .passwordHash(passwordEncoder.encode(request.rawPassword()))
                .build();

        userRepository.save(user);
        return "User registered successfully";
    }

    public TokenResponseDto login(LoginRequestDto request) {
        UserEntity user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!passwordEncoder.matches(request.rawPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        // Generate tokens using the raw username string
        String accessToken = jwtService.generateToken(user.getUsername(), 1000L * 60 * 15);
        String refreshToken = jwtService.generateToken(user.getUsername(), 1000L * 60 * 60 * 24 * 7);

        return new TokenResponseDto(accessToken, refreshToken);
    }

    public TokenResponseDto refresh(RefreshRequestDto request) {
        String username = jwtService.extractUsername(request.refreshToken());

        // Generate new tokens
        String newAccessToken = jwtService.generateToken(username, 1000L * 60 * 15);
        return new TokenResponseDto(newAccessToken, request.refreshToken());
    }
}