package com.example.paymentgateway.paymentgateway.Controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.paymentgateway.paymentgateway.Service.RazorpayService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor           
@Slf4j 
public class OrderController {
 
   
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(OrderController.class);

	
	private final RazorpayService razorpayService;


	
	@Value("${razorpay.api.key}")
	    private String razorpayKey;

	    @PostMapping("/create-order")
	    public ResponseEntity<?> createOrder(@RequestParam Long amount) {

	        try {

	            String orderId = razorpayService.createOrder(amount);

	            Map<String, Object> response = new HashMap<>();
	            response.put("orderId", orderId);
	            response.put("amount", amount * 100);
	            response.put("currency", "INR");
	            response.put("key", razorpayKey);

	            return ResponseEntity.ok(response);

	        } catch (Exception e) {

	            Map<String, String> error = new HashMap<>();
	            error.put("error", "Order creation failed");

	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body(error);
	        }
	        
	       
	    }
	}

