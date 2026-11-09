package com.fintect.Banking_Engine.modules.invest;

import com.fintect.Banking_Engine.core.dsa.KnapsackOptimizerEngine;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Manthan Jaswal (Roll: 3235)
 */
@Service
public class InvestService {

    public String initializePortfolio(PortfolioInitDto request) {
        return "Portfolio '" + request.portfolioName() + "' initialized with capital: " + request.initialCapital();
    }

    public OptimizationResponseDto optimize(OptimizationRequestDto request) {
        List<KnapsackOptimizerEngine.AssetItem> items = request.assets().stream()
                .map(a -> new KnapsackOptimizerEngine.AssetItem(a.assetId(), a.riskScore(), a.expectedYield()))
                .collect(Collectors.toList());

        List<KnapsackOptimizerEngine.AssetItem> optimized = KnapsackOptimizerEngine.optimizePortfolio(items, request.maxRiskCapacity());

        List<AssetDto> resultAssets = optimized.stream()
                .map(o -> new AssetDto(o.assetId(), o.riskWeight(), o.expectedYield()))
                .collect(Collectors.toList());

        double totalYield = optimized.stream().mapToDouble(KnapsackOptimizerEngine.AssetItem::expectedYield).sum();
        return new OptimizationResponseDto(resultAssets, totalYield);
    }

    public String rebalancePortfolio(RebalanceRequestDto request) {
        return "Dispatching automated buy/sell orders to match target allocation for portfolio: " + request.portfolioId();
    }
}