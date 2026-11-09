package com.fintect.Banking_Engine.core.dsa;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Manthan Jaswal (Roll: 3235)
 * Pure Java Data Structure for calculating node degree centrality (AML Risk Score).
 */
public class GraphCentralityEngine {

    public static double calculateDegreeCentrality(UUID targetNode, Map<UUID, List<UUID>> outEdges, Map<UUID, List<UUID>> inEdges) {
        int outDegree = outEdges.getOrDefault(targetNode, List.of()).size();
        int inDegree = inEdges.getOrDefault(targetNode, List.of()).size();

        int totalNodes = outEdges.size();
        if (totalNodes <= 1) return 0.0;

        // Simplified Centrality: (InDegree + OutDegree) / (TotalNodes - 1)
        return (double) (inDegree + outDegree) / (totalNodes - 1);
    }
}