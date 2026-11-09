package com.fintect.Banking_Engine.core.dsa;

import java.util.*;

/**
 * @author Manthan Jaswal (Roll: 3235)
 * Pure Java Data Structure using Topological Sort to resolve credit rule dependencies.
 */
public class DagEvaluatorEngine {

    public static List<String> resolveEvaluationOrder(List<String> rules, Map<String, List<String>> dependencies) {
        Map<String, Integer> inDegree = new HashMap<>();
        Map<String, List<String>> adjList = new HashMap<>();

        for (String rule : rules) {
            inDegree.put(rule, 0);
            adjList.put(rule, new ArrayList<>());
        }

        for (Map.Entry<String, List<String>> entry : dependencies.entrySet()) {
            String toNode = entry.getKey();
            for (String fromNode : entry.getValue()) {
                adjList.get(fromNode).add(toNode);
                inDegree.put(toNode, inDegree.get(toNode) + 1);
            }
        }

        Queue<String> queue = new LinkedList<>();
        for (String rule : rules) {
            if (inDegree.get(rule) == 0) queue.offer(rule);
        }

        List<String> evaluationOrder = new ArrayList<>();
        while (!queue.isEmpty()) {
            String current = queue.poll();
            evaluationOrder.add(current);

            for (String neighbor : adjList.get(current)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        if (evaluationOrder.size() != rules.size()) {
            throw new IllegalStateException("Cyclic dependency detected in credit rules DAG");
        }

        return evaluationOrder;
    }
}