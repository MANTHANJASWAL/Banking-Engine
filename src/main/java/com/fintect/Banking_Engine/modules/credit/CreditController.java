package com.fintect.Banking_Engine.modules.credit;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Manthan Jaswal (Roll: 3235)
 */
@RestController
@RequestMapping("/api/v1/credit")
public class CreditController {

    private final CreditService creditService;

    public CreditController(CreditService creditService) {
        this.creditService = creditService;
    }

    @PostMapping("/evaluate")
    public ResponseEntity<CreditEvalResponseDto> evaluate(@RequestBody CreditEvalRequestDto request) {
        return ResponseEntity.ok(creditService.evaluateCreditRules(request));
    }

    @GetMapping("/decisions/{id}")
    public ResponseEntity<java.util.List<String>> traceDecision(@PathVariable String id) {
        return ResponseEntity.ok(creditService.traceDecision(id));
    }

    @PostMapping("/disburse")
    public ResponseEntity<String> disburse(@RequestBody DisburseRequestDto request) {
        return ResponseEntity.ok(creditService.disburseFunds(request));
    }
}