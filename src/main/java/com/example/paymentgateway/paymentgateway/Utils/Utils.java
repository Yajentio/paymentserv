package com.example.paymentgateway.paymentgateway.Utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Mac;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Formatter;

//public final class Utils {
//    private Utils() {}
//
//    public static String getHash(String data, String secret) {
//        try {
//            Mac mac = Mac.getInstance("HmacSHA256");
//            mac.init(new SecretKeySpec(secret.getBytes(), "HmacSHA256"));
//            return Base64.getEncoder().encodeToString(mac.doFinal(data.getBytes()));
//        } catch (Exception e) {
//            throw new RuntimeException("Hash generation failed", e);
//        }
//    }
//    
//    private static String toHexString(byte[] bytes) {
//        StringBuilder sb = new StringBuilder(bytes.length * 2);
//        Formatter formatter = new Formatter(sb);
//        for (byte b : bytes) {
//            formatter.format("%02x", b);
//        }
//        return sb.toString();
//    }
//    
//}

public final class Utils {

    private Utils() {}

    public static String getHash(String data, String secret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(
                    secret.getBytes(StandardCharsets.UTF_8),
                    "HmacSHA256"));

            byte[] hash = mac.doFinal(
                    data.getBytes(StandardCharsets.UTF_8));

            return toHexString(hash);

        } catch (Exception e) {
            throw new RuntimeException("Hash generation failed", e);
        }
    }

    private static String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}


