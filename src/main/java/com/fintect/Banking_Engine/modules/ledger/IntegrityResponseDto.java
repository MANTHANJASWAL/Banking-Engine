package com.fintect.Banking_Engine.modules.ledger;

import java.time.Instant;
import java.util.UUID;

/**
 * @author Manthan Jaswal (Roll: 3235)
 * Date: 30/1/26
 */
public record IntegrityResponseDto(
        UUID accountId,           // Identifies which specific ledger was checked
        String computedMerkleRoot,
        boolean isValid,
        int entriesChecked,
        Instant timestamp         // Crucial for banking audit trails
) {
}