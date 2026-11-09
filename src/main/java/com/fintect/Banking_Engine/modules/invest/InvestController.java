package com.fintect.Banking_Engine.modules.invest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Manthan Jaswal (Roll: 3235)
 */
@RestController
@RequestMapping("/api/v1/invest")
public class InvestController {

    private final InvestService investService;

    public InvestController(InvestService investService) {
        this.investService = investService;
    }

    @PostMapping("/portfolios")
    public ResponseEntity<String> initPortfolio(@RequestBody PortfolioInitDto request) {
        return ResponseEntity.ok(investService.initializePortfolio(request));
    }

    @PostMapping("/optimize")
    public ResponseEntity<OptimizationResponseDto> optimizePortfolio(@RequestBody OptimizationRequestDto request) {
        return ResponseEntity.ok(investService.optimize(request));
    }

    @PostMapping("/rebalance")
    public ResponseEntity<String> rebalance(@RequestBody RebalanceRequestDto request) {
        return ResponseEntity.ok(investService.rebalancePortfolio(request));
    }
}