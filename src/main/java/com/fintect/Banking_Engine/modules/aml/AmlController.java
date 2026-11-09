package com.fintect.Banking_Engine.modules.aml;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author Manthan Jaswal (Roll: 3334)
 */
@RestController
@RequestMapping("/api/v1/aml")
public class AmlController {

    private final AmlService amlService;

    public AmlController(AmlService amlService) {
        this.amlService = amlService;
    }

    @PostMapping("/detect-cycles")
    public ResponseEntity<CycleResponseDto> detectCycles() {
        return ResponseEntity.ok(amlService.detectSmurfingCycles());
    }

    @GetMapping("/risk/{accountId}")
    public ResponseEntity<RiskScoreResponseDto> getRiskScore(@PathVariable UUID accountId) {
        return ResponseEntity.ok(amlService.calculateRisk(accountId));
    }

    @PutMapping("/accounts/{id}/freeze")
    public ResponseEntity<Void> freezeAccount(@PathVariable UUID id) {
        amlService.freezeAccount(id);
        return ResponseEntity.noContent().build();
    }
}