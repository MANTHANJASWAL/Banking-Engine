package com.fintect.Banking_Engine.modules.auth;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;

/**
 * @author Manthan Jaswal (Roll: 3235)
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody RegisterRequestDto request) {
        // Capture the raw string from your service
        String serviceResponse = authService.register(request);

        // Wrap it in a JSON-friendly Map
        Map<String, String> jsonResponse = new HashMap<>();
        jsonResponse.put("message", serviceResponse);

        return ResponseEntity.ok(jsonResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDto> refresh(@RequestBody RefreshRequestDto request) {
        return ResponseEntity.ok(authService.refresh(request));
    }
}