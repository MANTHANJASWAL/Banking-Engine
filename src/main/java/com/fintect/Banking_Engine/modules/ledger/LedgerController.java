package com.fintect.Banking_Engine.modules.ledger;

import com.fintect.Banking_Engine.core.exceptions.ApiErrorResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

/**
 * @author Manthan Jaswal (Roll: 3235)
 */
@RestController
@RequestMapping("/api/v1")
public class LedgerController {

    private final LedgerService ledgerService;

    public LedgerController(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }

    @PostMapping("/accounts")
    public ResponseEntity<Account> createAccount(@RequestBody AccountRequestDto request) {
        return ResponseEntity.ok(ledgerService.createAccount(request));
    }

    // NEW ENDPOINT: React hits this via JWT instead of a hardcoded UUID string
    @GetMapping("/accounts/me")
    public ResponseEntity<Account> getMyAccount() {
        return ResponseEntity.ok(ledgerService.getAccount());
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable UUID id) {
        return ResponseEntity.ok(ledgerService.getAccountById(id));
    }

    @PostMapping("/transactions")
    public ResponseEntity<Transaction> transfer(@Valid @RequestBody TransferRequestDto request) {
        return ResponseEntity.ok(ledgerService.executeTransfer(request));
    }

    @GetMapping("/transactions/{id}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable UUID id) {
        return ResponseEntity.ok(ledgerService.getTransaction(id));
    }

    @GetMapping("/ledger/audit-integrity")
    public ResponseEntity<IntegrityResponseDto> auditLedger() {
        return ResponseEntity.ok(ledgerService.auditIntegrity());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiErrorResponse> handleInsufficientFunds(IllegalStateException ex) {
        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Transaction Failed",
                "Ledger Error: " + ex.getMessage(),
                Instant.now()
        );
        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}