package com.fintect.Banking_Engine.core.exceptions;

import java.time.Instant;

/**
 * @author Manthan Jaswal (Roll: 3235)
 */
public record ApiErrorResponse(
        int status,
        String error,
        String message,
        Instant timestamp
) {}