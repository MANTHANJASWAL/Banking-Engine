package com.fintect.Banking_Engine.modules.notify;

public record AdminEmailRequestDto(
        String targetAudience, // e.g., "EMPLOYEES", "ALL_USERS", "SPECIFIC"
        String specificEmail,  // Used only if targetAudience is "SPECIFIC"
        String subject,
        String messageBody
) {}