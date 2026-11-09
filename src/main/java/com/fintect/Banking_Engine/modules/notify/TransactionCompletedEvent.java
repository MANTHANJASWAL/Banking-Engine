package com.fintect.Banking_Engine.modules.notify;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author Manthan Jaswal (Roll: 3235)
 */
public record TransactionCompletedEvent(
        UUID transactionId,
        UUID accountId,
        String action, // e.g., "DEPOSIT", "WITHDRAWAL", "TRANSFER"
        BigDecimal amount
) {}