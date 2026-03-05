package com.example.paymentgateway.paymentgateway.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.paymentgateway.paymentgateway.Entity.OrderEntity;
import com.example.paymentgateway.paymentgateway.Entity.PaymentEntity;
import com.example.paymentgateway.paymentgateway.Repository.OrderRepository;
import com.example.paymentgateway.paymentgateway.Repository.PaymentRepository;
import com.example.paymentgateway.paymentgateway.Utils.Utils;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;



@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    @Value("${razorpay.api.secret}")
    private String apiSecret;

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

//   
   
//        @Transactional
//        public boolean verifyPayment(Map<String, String> payload) {
//
//            String orderId = payload.get("razorpay_order_id");
//            String paymentId = payload.get("razorpay_payment_id");
//            String signature = payload.get("razorpay_signature");
//
//            if (orderId == null || paymentId == null || signature == null) {
//                return false;
//            }
//
//            try {
//            	
//                String expectedSignature =
//                        Utils.getHash(orderId + "|" + paymentId, apiSecret);
//
//                if (!expectedSignature.equals(signature)) {
//                    return false;
//                }
//
//                // ✅ UPDATE ORDER
//                orderRepository.findById(orderId).ifPresent(order -> {
//                    order.setOd_status("paid");
//                    orderRepository.save(order);
//                });
//
//                PaymentEntity payment = new PaymentEntity();
//                payment.setOrderId(orderId);
//                payment.setName(payload.getOrDefault("name", "Unknown"));
//                payment.setAmount(Integer.parseInt(payload.getOrDefault("amount", "0")));
//                payment.setCurrency(payload.getOrDefault("currency", "INR"));
//                payment.setStatus(payload.getOrDefault("status", "paid"));
//                payment.setDescription(payload.getOrDefault("description", "-"));
//                payment.setCreatedAt(OffsetDateTime.now());
//
//                // 🔥 PAYMENT TYPE LOGIC
//                String method = payload.get("method"); // card / upi
//                String cardType = payload.get("card_type"); // debit / credit
//
//                if ("card".equalsIgnoreCase(method)) {
//                    payment.setPaymentMethod("CARD");
//                    payment.setCardType(
//                            cardType != null ? cardType.toUpperCase() : "UNKNOWN");
//                } else if ("upi".equalsIgnoreCase(method)) {
//                    payment.setPaymentMethod("UPI");
//                    payment.setCardType("NA");
//                } else {
//                    payment.setPaymentMethod("UNKNOWN");
//                    payment.setCardType("NA");
//                }
//
//                paymentRepository.save(payment);
//
//                log.info("Payment saved: {} {}", payment.getPaymentMethod(), payment.getCardType());
//                return true;
//
//            } catch (Exception e) {
//                log.error("Verification failed", e);
//                return false;
//            }
//        }
//  
    
    @Transactional
    public boolean verifyPayment(Map<String, String> payload) {

        String orderId = payload.get("razorpay_order_id");
        String paymentId = payload.get("razorpay_payment_id");
        String signature = payload.get("razorpay_signature");
        
        System.out.println("OrderId: " + orderId);
        System.out.println("PaymentId: " + paymentId);
        System.out.println("Signature: " + signature);

        if (orderId == null || paymentId == null || signature == null) {
            return false;
        }

        try {
            // 🔥 1. Verify Signature
            String expectedSignature =
                    Utils.getHash(orderId + "|" + paymentId, apiSecret);

            if (!expectedSignature.equals(signature)) {
                log.error("Signature mismatch");
                return false;
            }

            // 🔥 2. Fetch Order from DB (Never trust frontend amount)
            OrderEntity order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found"));

            if (!"CREATED".equalsIgnoreCase(order.getOd_status())) {
                log.warn("Order already processed");
                return true;
            }

            order.setOd_status("PAID");
            orderRepository.save(order);

            // 🔥 3. Prevent Duplicate Payment
            if (paymentRepository.existsByRazorpayPaymentId(paymentId)) {
                log.warn("Duplicate payment");
                return true;
            }

            // 🔥 4. Save Payment
            PaymentEntity payment = new PaymentEntity();
            payment.setOrderId(orderId);
            payment.setRazorpayPaymentId(paymentId);
            payment.setAmount(order.getOd_amount()); // 🔐 Secure
            payment.setCurrency(order.getOd_currency());
            payment.setStatus("SUCCESS");
            payment.setCreatedAt(OffsetDateTime.now());

            String method = payload.get("method");
            String cardType = payload.get("card_type");

            if ("card".equalsIgnoreCase(method)) {
                payment.setPaymentMethod("CARD");
                payment.setCardType(cardType != null ? cardType.toUpperCase() : "UNKNOWN");
            } else if ("upi".equalsIgnoreCase(method)) {
                payment.setPaymentMethod("UPI");
                payment.setCardType("NA");
            } else {
                payment.setPaymentMethod("UNKNOWN");
                payment.setCardType("NA");
            }

            paymentRepository.save(payment);

            log.info("Payment verified successfully");
            return true;

        } catch (Exception e) {
            log.error("Verification failed", e);
            return false;
        }
    }
    
    
  

    public List<PaymentEntity> getAllPayments() {
        return paymentRepository.findAll();
    }

    public PaymentEntity savePaymentEntity(PaymentEntity p) {
        p.setCreatedAt(OffsetDateTime.now());
        return paymentRepository.save(p);
    }

    public PaymentEntity getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }

    public PaymentEntity updatePayment(Long id, PaymentEntity updatedPayment) {
        PaymentEntity existing = getPaymentById(id);
        existing.setName(updatedPayment.getName());
        existing.setAmount(updatedPayment.getAmount());
        existing.setCurrency(updatedPayment.getCurrency());
        existing.setStatus(updatedPayment.getStatus());
        existing.setOrderId(updatedPayment.getOrderId());
        existing.setDescription(updatedPayment.getDescription());
        return paymentRepository.save(existing);
    }

    public Map<String, String> deletePayment(Long id) {
        Map<String, String> response = new HashMap<>();

        if (paymentRepository.existsById(id)) {
            paymentRepository.deleteById(id);
            response.put("message", "Payment deleted successfully.");
        } else {
            response.put("message", "Payment not found.");
        }

        return response;
    }
    

}