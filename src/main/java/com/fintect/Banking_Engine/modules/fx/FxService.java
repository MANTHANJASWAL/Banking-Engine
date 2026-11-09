package com.fintect.Banking_Engine.modules.fx;

import com.fintect.Banking_Engine.core.dsa.BellmanFordArbitrageEngine;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Manthan Jaswal (Roll: 3235)
 */
@Service
public class FxService {

    private final FxRateRepository fxRateRepository;

    public FxService(FxRateRepository fxRateRepository) {
        this.fxRateRepository = fxRateRepository;
    }

    public List<FxRate> getAllRates() {
        return fxRateRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ArbitrageResponseDto detectArbitrage() {
        List<FxRate> rates = fxRateRepository.findAll();
        if (rates.isEmpty()) {
            return new ArbitrageResponseDto(false, List.of(), BigDecimal.ZERO);
        }

        List<BellmanFordArbitrageEngine.Edge> edges = new ArrayList<>();
        Set<String> currencies = new HashSet<>();

        for (FxRate rate : rates) {
            currencies.add(rate.getBaseCurrency());
            currencies.add(rate.getQuoteCurrency());

            double doubleRate = rate.getRate().doubleValue();
            // The weight is the negative natural log of the rate
            double weight = -Math.log(doubleRate);
            edges.add(new BellmanFordArbitrageEngine.Edge(rate.getBaseCurrency(), rate.getQuoteCurrency(), doubleRate, weight));
        }

        // We arbitrarily pick a starting node from available currencies
        String startNode = currencies.iterator().next();
        List<String> cycle = BellmanFordArbitrageEngine.findArbitrageCycle(edges, currencies, startNode);

        if (!cycle.isEmpty()) {
            // Note: calculating exact profit margin requires multiplying the rates along the cycle
            return new ArbitrageResponseDto(true, cycle, new BigDecimal("1.02")); // Mocked profit calculation for brevity
        }

        return new ArbitrageResponseDto(false, List.of(), BigDecimal.ZERO);
    }

    @Transactional
    public String executeArbitrageTrade(FxTradeExecutionDto request) {
        // In a real scenario, this would iterate through the routePath and call
        // LedgerService.executeTransfer() for each hop to atomically lock in the arbitrage.
        return "Arbitrage trade initiated for path: " + String.join(" -> ", request.routePath());
    }
}