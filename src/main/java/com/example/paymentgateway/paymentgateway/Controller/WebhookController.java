package com.example.paymentgateway.paymentgateway.Controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import com.example.paymentgateway.paymentgateway.Service.QrCodeService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class WebhookController {

    private final QrCodeService qrCodeService;

    @Value("${razorpay.webhook.secret}")
    private String webhookSecret;

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("X-Razorpay-Signature") String signature) {

        try {

            // ✅ SIGNATURE VERIFICATION
            boolean isValid = verifySignature(payload, signature);

            if (!isValid) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid Signature");
            }

            JSONObject json = new JSONObject(payload);
            String event = json.getString("event");

            // ✅ ONLY SUCCESS EVENT ALLOWED
            if ("payment.captured".equals(event)) {

                JSONObject payment =
                        json.getJSONObject("payload")
                                .getJSONObject("payment")
                                .getJSONObject("entity");

                String orderId = payment.getString("order_id");
                String status = payment.getString("status");

                if ("captured".equals(status)) {
                    qrCodeService.markPaymentSuccess(orderId);
                }
            }

            // Optional failure handling
            if ("payment.failed".equals(event)) {

                JSONObject payment =
                        json.getJSONObject("payload")
                                .getJSONObject("payment")
                                .getJSONObject("entity");

                String orderId = payment.getString("order_id");

                qrCodeService.markPaymentFailed(orderId);
            }

            return ResponseEntity.ok("Webhook Processed");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Webhook Error");
        }
    }

    // ✅ HMAC SHA256 SIGNATURE VERIFY
    private boolean verifySignature(String payload, String actualSignature)
            throws Exception {

        Mac sha256Hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey =
                new SecretKeySpec(webhookSecret.getBytes(), "HmacSHA256");

        sha256Hmac.init(secretKey);

        byte[] hash = sha256Hmac.doFinal(payload.getBytes());

        String generatedSignature =
                Base64.getEncoder().encodeToString(hash);

        return generatedSignature.equals(actualSignature);
    }
}