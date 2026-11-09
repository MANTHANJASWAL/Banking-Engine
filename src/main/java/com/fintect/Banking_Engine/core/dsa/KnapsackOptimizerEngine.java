package com.fintect.Banking_Engine.core.dsa;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Manthan Jaswal (Roll: 3235)
 * Pure Java Data Structure using 0/1 Knapsack DP to maximize portfolio yield under a strict risk/capital cap.
 */
public class KnapsackOptimizerEngine {

    public record AssetItem(String assetId, int riskWeight, double expectedYield) {}

    public static List<AssetItem> optimizePortfolio(List<AssetItem> availableAssets, int maxRiskCapacity) {
        int n = availableAssets.size();
        double[][] dp = new double[n + 1][maxRiskCapacity + 1];

        for (int i = 1; i <= n; i++) {
            AssetItem item = availableAssets.get(i - 1);
            for (int w = 0; w <= maxRiskCapacity; w++) {
                if (item.riskWeight() <= w) {
                    dp[i][w] = Math.max(dp[i - 1][w], dp[i - 1][w - item.riskWeight()] + item.expectedYield());
                } else {
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }

        List<AssetItem> selectedAssets = new ArrayList<>();
        int w = maxRiskCapacity;
        for (int i = n; i > 0 && dp[i][w] > 0; i--) {
            if (dp[i][w] != dp[i - 1][w]) {
                AssetItem item = availableAssets.get(i - 1);
                selectedAssets.add(item);
                w -= item.riskWeight();
            }
        }
        return selectedAssets;
    }
}