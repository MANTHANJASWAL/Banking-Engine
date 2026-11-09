package com.fintect.Banking_Engine.modules.auth;

import jakarta.validation.constraints.NotBlank;

/**
 * @author Manthan Jaswal (Roll: 3235)
 */
public record LoginRequestDto(
        @NotBlank(message = "Username is required")
        String username,

        @NotBlank(message = "Password is required")
        String rawPassword
) {}