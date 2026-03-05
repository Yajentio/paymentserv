package com.example.paymentgateway.paymentgateway.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.example.paymentgateway.paymentgateway.DTO.UpiQrResponse;
import com.example.paymentgateway.paymentgateway.Entity.OrderEntity;
import com.example.paymentgateway.paymentgateway.Repository.OrderRepository;
import com.razorpay.*;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.razorpay.QrCode;



@Service
@Slf4j
public class RazorpayService {
    
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(RazorpayService.class);

    @Value("${razorpay.api.key}")
    private String apiKey;

    @Value("${razorpay.api.secret}")
    private String apiSecret;

    private final OrderRepository orderRepository;

    public RazorpayService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


        

 // ==============================
    // 1️⃣ CREATE ORDER
    // ==============================

    @Transactional
    public String createOrder(Long amount) {

        try {

            RazorpayClient client = new RazorpayClient(apiKey, apiSecret);

            // Convert to paise safely
            long amountInPaise = amount * 100;

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amountInPaise);
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "rcpt_" + System.currentTimeMillis());
            orderRequest.put("payment_capture", 1);

            Order razorpayOrder = client.orders.create(orderRequest);

            // Save into DB
            OrderEntity entity = new OrderEntity(
                    razorpayOrder.get("id"),                 // Razorpay order id
                    amountInPaise,                           // Store exact amount sent
                    razorpayOrder.get("currency"),           // Currency
                    razorpayOrder.get("receipt"),            // Receipt
                    razorpayOrder.get("status"),             // REAL Razorpay status
                    Instant.now()
            );

            orderRepository.save(entity);

            return razorpayOrder.get("id");

        } catch (RazorpayException e) {
            log.error("Razorpay order creation failed", e);
            throw new RuntimeException("Razorpay order creation failed");
        }
    }
    
    
    public UpiQrResponse generateUpiQr(int amountPaise, String description) {

        try {
            String url = "https://api.razorpay.com/v1/qr_codes";

            String auth = apiKey + ":" + apiSecret;
            String encodedAuth = Base64.getEncoder()
                    .encodeToString(auth.getBytes(StandardCharsets.UTF_8));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Basic " + encodedAuth);

            Map<String, Object> body = new HashMap<>();
            body.put("type", "upi_qr");
            body.put("name", "YTA Internship Academy");
            body.put("usage", "single_use");
            body.put("fixed_amount", true);
            body.put("payment_amount", amountPaise);
            body.put("description", description);
            body.put("close_by", Instant.now().getEpochSecond() + 3600);

            HttpEntity<Map<String, Object>> entity =
                    new HttpEntity<>(body, headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> response =
                    restTemplate.postForEntity(url, entity, Map.class);

            Map<String, Object> resp = response.getBody();
            Map<String, Object> image = (Map<String, Object>) resp.get("image");

            return new UpiQrResponse(
                    resp.get("id").toString(),
                    image.get("content").toString()
            );

        } catch (Exception e) {
            log.error("QR generation failed", e);
            throw new RuntimeException("QR generation failed", e);
        }
    }

    public String getApiKey() {
        return apiKey;
    }

}

