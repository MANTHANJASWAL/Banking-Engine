package com.fintect.Banking_Engine.modules.ledger;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author Manthan Jaswal (Roll: 3235)
 */
public record TransferRequestDto(
        @NotNull(message = "Source account ID is required")
        UUID fromAccountId,

        @NotNull(message = "Destination account ID is required")
        UUID toAccountId,

        @NotNull(message = "Transfer amount cannot be null")
        @DecimalMin(value = "0.01", message = "Transfer amount must be strictly greater than zero")
        BigDecimal amount,

        @NotBlank(message = "Idempotency key is required to prevent duplicate transfers")
        String idempotencyKey
) {}