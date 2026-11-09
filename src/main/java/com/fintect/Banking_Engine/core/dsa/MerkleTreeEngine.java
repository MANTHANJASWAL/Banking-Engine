package com.fintect.Banking_Engine.core.dsa;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;

/**
 * @author Manthan Jaswal (Roll: 3335)
 * Pure Java Data Structure for validating Ledger integrity.
 */
public class MerkleTreeEngine {

    public static String calculateMerkleRoot(List<String> hashes) {
        if (hashes == null || hashes.isEmpty()) {
            return null;
        }
        if (hashes.size() == 1) {
            return hashes.getFirst();
        }

        List<String> parentHashes = new ArrayList<>();
        for (int i = 0; i < hashes.size(); i += 2) {
            String left = hashes.get(i);
            String right = (i + 1 < hashes.size()) ? hashes.get(i + 1) : left;
            parentHashes.add(hashPair(left, right));
        }
        return calculateMerkleRoot(parentHashes);
    }

    public static String generateHash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes());
            return HexFormat.of().formatHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    private static String hashPair(String left, String right) {
        return generateHash(left + right);
    }
}