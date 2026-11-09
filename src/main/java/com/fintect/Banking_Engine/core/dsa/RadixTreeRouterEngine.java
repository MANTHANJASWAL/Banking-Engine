package com.fintect.Banking_Engine.core.dsa;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Manthan Jaswal (Roll: 3235)
 * Pure Java Data Structure using a Trie/Radix approach for sub-millisecond route dispatching.
 */
public class RadixTreeRouterEngine {

    private final TrieNode root = new TrieNode();

    private static class TrieNode {
        Map<String, TrieNode> children = new HashMap<>();
        String endpointTarget;
        boolean isEndOfRoute;
    }

    public void insertRoute(String path, String target) {
        TrieNode current = root;
        String[] parts = path.split("/");
        for (String part : parts) {
            if (part.isEmpty()) continue;
            current.children.putIfAbsent(part, new TrieNode());
            current = current.children.get(part);
        }
        current.isEndOfRoute = true;
        current.endpointTarget = target;
    }

    public String matchRoute(String path) {
        TrieNode current = root;
        String[] parts = path.split("/");
        for (String part : parts) {
            if (part.isEmpty()) continue;
            TrieNode node = current.children.get(part);
            if (node == null) return null; // Route not found
            current = node;
        }
        return current.isEndOfRoute ? current.endpointTarget : null;
    }
}