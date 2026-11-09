package com.fintect.Banking_Engine.core.dsa;

import java.util.*;

/**
 * @author Manthan Jaswal (Roll: 3235)
 * Pure Java Data Structure for detecting circular money flows (smurfing loops).
 */
public class TarjansCycleDetectionEngine {

    private int index = 0;
    private final Deque<UUID> stack = new ArrayDeque<>();
    private final Map<UUID, Integer> indices = new HashMap<>();
    private final Map<UUID, Integer> lowlinks = new HashMap<>();
    private final Set<UUID> onStack = new HashSet<>();
    private final List<List<UUID>> stronglyConnectedComponents = new ArrayList<>();

    public List<List<UUID>> findCycles(Map<UUID, List<UUID>> graph) {
        indices.clear();
        lowlinks.clear();
        onStack.clear();
        stack.clear();
        stronglyConnectedComponents.clear();
        index = 0;

        for (UUID node : graph.keySet()) {
            if (!indices.containsKey(node)) {
                strongconnect(node, graph);
            }
        }

        // Filter out single-node components without self-loops
        List<List<UUID>> cycles = new ArrayList<>();
        for (List<UUID> scc : stronglyConnectedComponents) {
            if (scc.size() > 1 || (scc.size() == 1 && graph.getOrDefault(scc.getFirst(), Collections.emptyList()).contains(scc.getFirst()))) {
                cycles.add(scc);
            }
        }
        return cycles;
    }

    private void strongconnect(UUID v, Map<UUID, List<UUID>> graph) {
        indices.put(v, index);
        lowlinks.put(v, index);
        index++;
        stack.push(v);
        onStack.add(v);

        for (UUID w : graph.getOrDefault(v, Collections.emptyList())) {
            if (!indices.containsKey(w)) {
                strongconnect(w, graph);
                lowlinks.put(v, Math.min(lowlinks.get(v), lowlinks.get(w)));
            } else if (onStack.contains(w)) {
                lowlinks.put(v, Math.min(lowlinks.get(v), indices.get(w)));
            }
        }

        if (lowlinks.get(v).equals(indices.get(v))) {
            List<UUID> scc = new ArrayList<>();
            UUID w;
            do {
                w = stack.pop();
                onStack.remove(w);
                scc.add(w);
            } while (!w.equals(v));
            stronglyConnectedComponents.add(scc);
        }
    }
}