package com.fintect.Banking_Engine.modules.ledger;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * @author Manthan Jaswal (Roll: 3235)
 */
public record StatementResponseDto(
        UUID entryId,
        UUID transactionId,
        Instant timestamp,
        LedgerEntry.EntryDirection direction,
        BigDecimal amount
) {}