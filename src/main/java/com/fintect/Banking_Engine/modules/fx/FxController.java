package com.fintect.Banking_Engine.modules.fx;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Manthan Jaswal (Roll: 3235)
 */
@RestController
@RequestMapping("/api/v1/fx")
public class FxController {

    private final FxService fxService;

    public FxController(FxService fxService) {
        this.fxService = fxService;
    }

    @GetMapping("/rates")
    public ResponseEntity<List<FxRate>> getRates() {
        return ResponseEntity.ok(fxService.getAllRates());
    }

    @GetMapping("/arbitrage")
    public ResponseEntity<ArbitrageResponseDto> detectArbitrage() {
        return ResponseEntity.ok(fxService.detectArbitrage());
    }

    @PostMapping("/execute-trade")
    public ResponseEntity<String> executeTrade(@RequestBody FxTradeExecutionDto request) {
        return ResponseEntity.ok(fxService.executeArbitrageTrade(request));
    }
}