package com.fintect.Banking_Engine.modules.openbanking;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Manthan Jaswal (Roll: 3235)
 */
@RestController
@RequestMapping("/api/v1/open-banking")
public class OpenBankingController {

    private final OpenBankingService openBankingService;

    public OpenBankingController(OpenBankingService openBankingService) {
        this.openBankingService = openBankingService;
    }

    @PostMapping("/consent")
    public ResponseEntity<String> grantConsent(@RequestBody ConsentRequestDto request) {
        return ResponseEntity.ok(openBankingService.grantConsent(request));
    }

    @GetMapping("/match-route")
    public ResponseEntity<RouteMatchDto> matchRoute(@RequestParam String path) {
        return ResponseEntity.ok(openBankingService.dispatchRoute(path));
    }

    @GetMapping("/accounts")
    public ResponseEntity<String> fetchScopedAccounts(@RequestHeader("Authorization") String tpaToken) {
        return ResponseEntity.ok("Scoped account data for token: " + tpaToken);
    }
}