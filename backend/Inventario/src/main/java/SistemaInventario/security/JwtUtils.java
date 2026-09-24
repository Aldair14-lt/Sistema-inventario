package SistemaInventario.security;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {

    @Value("${app.jwtSecret:changeitplease}")
    private String jwtSecret;

    @Value("${app.jwtExpirationMs:3600000}")
    private int jwtExpirationMs;

    public String generateToken(String subject) {
        try {
            long now = Instant.now().toEpochMilli();
            long exp = now + jwtExpirationMs;
            String headerJson = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
            String payloadJson = String.format("{\"sub\":\"%s\",\"iat\":%d,\"exp\":%d}", escape(subject), now, exp);
            String header = Base64.getUrlEncoder().withoutPadding().encodeToString(headerJson.getBytes(StandardCharsets.UTF_8));
            String payload = Base64.getUrlEncoder().withoutPadding().encodeToString(payloadJson.getBytes(StandardCharsets.UTF_8));
            String signingInput = header + "." + payload;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            String sig = Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(signingInput.getBytes(StandardCharsets.UTF_8)));
            return signingInput + "." + sig;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getSubject(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length < 2) return null;
            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            String sub = extractStringField(payloadJson, "sub");
            String expStr = extractNumericField(payloadJson, "exp");
            if (expStr != null) {
                long exp = Long.parseLong(expStr);
                if (Instant.now().toEpochMilli() > exp) return null;
            }
            return sub;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean validateToken(String token, org.springframework.security.core.userdetails.UserDetails userDetails) {
        try {
            if (token == null) return false;
            String[] parts = token.split("\\.");
            if (parts.length != 3) return false;
            String header = parts[0];
            String payload = parts[1];
            String signature = parts[2];

            // verify signature
            String signingInput = header + "." + payload;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            String expectedSig = Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(signingInput.getBytes(StandardCharsets.UTF_8)));
            if (!expectedSig.equals(signature)) return false;

            // verify subject and expiry
            String payloadJson = new String(Base64.getUrlDecoder().decode(payload), StandardCharsets.UTF_8);
            String sub = extractStringField(payloadJson, "sub");
            if (sub == null) return false;
            if (!sub.equals(userDetails.getUsername())) return false;
            String expStr = extractNumericField(payloadJson, "exp");
            if (expStr != null) {
                long exp = Long.parseLong(expStr);
                if (Instant.now().toEpochMilli() > exp) return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static String extractStringField(String json, String field) {
        String pattern = "\"" + field + "\"\s*:\s*\"";
        int idx = json.indexOf(pattern);
        if (idx < 0) return null;
        int start = idx + pattern.length();
        int end = json.indexOf('"', start);
        if (end < 0) return null;
        return json.substring(start, end);
    }

    private static String extractNumericField(String json, String field) {
        String pattern = "\"" + field + "\"\s*:\s*";
        int idx = json.indexOf(pattern);
        if (idx < 0) return null;
        int start = idx + pattern.length();
        int end = start;
        while (end < json.length() && (Character.isDigit(json.charAt(end)) || json.charAt(end) == '-')) end++;
        if (end == start) return null;
        return json.substring(start, end);
    }

    private static String escape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

}


