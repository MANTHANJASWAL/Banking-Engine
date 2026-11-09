package com.fintect.Banking_Engine.core.dsa;

import java.util.*;

/**
 * @author Manthan Jaswal (Roll: 3235)
 * Pure Java Data Structure detecting negative-weight cycles for FX Arbitrage.
 */
public class BellmanFordArbitrageEngine {

    public record Edge(String source, String target, double rate, double weight) {}

    public static List<String> findArbitrageCycle(List<Edge> edges, Set<String> currencies, String startingCurrency) {
        Map<String, Double> distances = new HashMap<>();
        Map<String, String> predecessors = new HashMap<>();

        for (String currency : currencies) {
            distances.put(currency, Double.MAX_VALUE);
        }
        distances.put(startingCurrency, 0.0);

        // Relax edges |V| - 1 times
        for (int i = 0; i < currencies.size() - 1; i++) {
            for (Edge edge : edges) {
                if (distances.get(edge.source()) != Double.MAX_VALUE &&
                        distances.get(edge.source()) + edge.weight() < distances.get(edge.target())) {
                    distances.put(edge.target(), distances.get(edge.source()) + edge.weight());
                    predecessors.put(edge.target(), edge.source());
                }
            }
        }

        // Check for negative-weight cycles
        for (Edge edge : edges) {
            if (distances.get(edge.source()) != Double.MAX_VALUE &&
                    distances.get(edge.source()) + edge.weight() < distances.get(edge.target()) - 1e-9) { // 1e-9 for floating point precision

                // Cycle detected, retrace it
                List<String> cycle = new ArrayList<>();
                String current = edge.target();

                // Go back |V| times to ensure we are inside the cycle
                for (int i = 0; i < currencies.size(); i++) {
                    current = predecessors.get(current);
                    if (current == null) return Collections.emptyList();
                }

                String cycleStart = current;
                cycle.add(cycleStart);
                current = predecessors.get(cycleStart);

                while (!current.equals(cycleStart)) {
                    cycle.add(current);
                    current = predecessors.get(current);
                }
                cycle.add(cycleStart);
                Collections.reverse(cycle);
                return cycle;
            }
        }
        return Collections.emptyList(); // No arbitrage found
    }
}