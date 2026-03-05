package com.example.paymentgateway.paymentgateway.Controller;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.paymentgateway.paymentgateway.DTO.CreditPaymentRequest;
import com.example.paymentgateway.paymentgateway.Response.CreditPaymentResponse;
import com.example.paymentgateway.paymentgateway.DTO.UpiQrRequest;
import com.example.paymentgateway.paymentgateway.DTO.UpiQrResponse;
import com.example.paymentgateway.paymentgateway.Entity.PaymentEntity;
import com.example.paymentgateway.paymentgateway.Repository.PaymentRepository;
import com.example.paymentgateway.paymentgateway.Service.PaymentService;
import com.example.paymentgateway.paymentgateway.Service.RazorpayService;
import com.example.paymentgateway.paymentgateway.Service.ReceiptService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;
    private final RazorpayService rzp;
    private final ReceiptService receiptService;

    

//       
 


    // ✅ CREATE ORDER (Credit + Debit + UPI)
    @PostMapping("/credit")
    public ResponseEntity<Map<String, Object>> createPayment(
    @RequestBody Map<String, Object> request) {


   // int amount = (int) request.get("amount");
    	Long amount = ((Number) request.get("amount")).longValue();
    if (amount <= 0) {
    return ResponseEntity.badRequest()
    .body(Map.of("error", "Invalid amount"));
    }


    String orderId = rzp.createOrder(amount);


    return ResponseEntity.ok(Map.of(
    "key", rzp.getApiKey(),
    "orderId", orderId,
    "amount", amount * 100,
    "currency", "INR",
    "name", request.getOrDefault("name", "Customer"),
    "description", request.getOrDefault("description", "Payment")
    ));
    }
    
    


    // ✅ VERIFY PAYMENT (UPI / DEBIT / CREDIT)
    @PostMapping("/verify")
    public ResponseEntity<Map<String, String>> verifyPayment(
    @RequestBody Map<String, String> payload) {


    boolean verified = paymentService.verifyPayment(payload);
    if (verified) {
    return ResponseEntity.ok(
    Map.of("message", "Payment verified successfully"));
    }
    return ResponseEntity.badRequest()
    .body(Map.of("error", "Payment verification failed"));
    
    
    }
    
    
    

    @GetMapping("/receipt/{paymentId}")
    public ResponseEntity<byte[]> downloadReceipt(
            @PathVariable Long paymentId) {

        try {

            PaymentEntity payment = paymentRepository
                    .findById(paymentId)
                    .orElseThrow(() ->
                            new RuntimeException("Payment not found"));

            byte[] pdf = receiptService.generateReceipt(payment);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=receipt_" + paymentId + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);

        } catch (Exception e) {

            return ResponseEntity.internalServerError().build();
        }
    }

   

        @GetMapping("/getAllPayment")
        public List<PaymentEntity> getAllPayments() {
            return paymentService.getAllPayments();
        }

        @PostMapping("/savePayment")
        public PaymentEntity savePayment(@RequestBody PaymentEntity p) {
            return paymentService.savePaymentEntity(p);
        }

        
        @PutMapping("/updatePaymentEntity/{id}")
        public PaymentEntity updatePayment(@PathVariable Long id, @RequestBody PaymentEntity updatedPayment) {
            return paymentService.updatePayment(id, updatedPayment);
        }

        @DeleteMapping("/deletePaymentEntity/{id}")
        public ResponseEntity<Map<String, String>> deletePayment(@PathVariable Long id) {
            return ResponseEntity.ok(paymentService.deletePayment(id));
        }
        
        
        
    }
