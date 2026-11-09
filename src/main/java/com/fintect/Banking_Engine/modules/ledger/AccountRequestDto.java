package com.fintect.Banking_Engine.modules.ledger;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record AccountRequestDto(
        @NotBlank(message = "Account holder name is required")
        @Size(max = 100, message = "Account holder name cannot exceed 100 characters")
        String accountHolderName,

        @NotBlank(message = "Currency is required")
        @Pattern(regexp = "^(USD|INR)$", message = "Only USD and INR are supported")
        String currency,

        @NotNull(message = "Initial balance cannot be null")
        @PositiveOrZero(message = "Initial balance must be zero or positive")
        BigDecimal initialBalance
) {
}