package com.fintect.Banking_Engine.modules.ledger;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accounts")
public class StatementController {

    private final LedgerEntryRepository ledgerEntryRepository;
    private final LedgerService ledgerService;

    // Inject LedgerService here alongside the repository
    public StatementController(LedgerEntryRepository ledgerEntryRepository, LedgerService ledgerService) {
        this.ledgerEntryRepository = ledgerEntryRepository;
        this.ledgerService = ledgerService;
    }
    @Transactional(readOnly = true)
    @GetMapping("/{accountId}/statements")
    public ResponseEntity<Page<StatementResponseDto>> getStatements(
            @PathVariable UUID accountId, // Updated to UUID
            @RequestParam(required = false) LedgerEntry.EntryDirection direction,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<LedgerEntry> entries = ledgerEntryRepository.findFilteredStatements(
                accountId,
                direction,
                startDate,
                endDate,
                PageRequest.of(page, size)
        );

        Page<StatementResponseDto> response = entries.map(entry -> new StatementResponseDto(
                entry.getId(),
                entry.getTransaction().getId(),
                entry.getTransaction().getTimestamp(),
                entry.getDirection(),
                entry.getAmount()
        ));

        return ResponseEntity.ok(response);
    }

    // New Deposit Endpoint
    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<String> deposit(
            @PathVariable UUID accountId,
            @RequestBody TransferRequestDto request) {

        ledgerService.processDeposit(accountId, request.amount(), request.idempotencyKey());
        return ResponseEntity.ok("Deposit successful");
    }

    // New Withdrawal Endpoint
    @PostMapping("/{accountId}/withdraw")
    public ResponseEntity<String> withdraw(
            @PathVariable UUID accountId,
            @RequestBody TransferRequestDto request) {

        ledgerService.processWithdrawal(accountId, request.amount(), request.idempotencyKey());
        return ResponseEntity.ok("Withdrawal successful");
    }
}