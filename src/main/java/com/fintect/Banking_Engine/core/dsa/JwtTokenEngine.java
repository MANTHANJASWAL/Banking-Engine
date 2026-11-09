package com.fintect.Banking_Engine.core.dsa;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author Manthan Jaswal (Roll: 3235)
 * Pure Java Data Structure / Engine for stateless JWT generation and validation using HMAC-SHA256.
 */
public class JwtTokenEngine {

    private static final String SECRET = "EnterpriseBankingEngineSuperSecretKey3235!";

    public static String generateToken(String username, long expirationMillis) {
        String header = Base64.getUrlEncoder().withoutPadding().encodeToString(
                "{\"alg\":\"HS256\",\"typ\":\"JWT\"}".getBytes(StandardCharsets.UTF_8)
        );

        long exp = System.currentTimeMillis() + expirationMillis;
        String payloadJson = String.format("{\"sub\":\"%s\",\"exp\":%d}", username, exp / 1000);
        String payload = Base64.getUrlEncoder().withoutPadding().encodeToString(
                payloadJson.getBytes(StandardCharsets.UTF_8)
        );

        String signature = sign(header + "." + payload);
        return header + "." + payload + "." + signature;
    }

    private static String sign(String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] signatureBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(signatureBytes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to sign JWT", e);
        }
    }
}