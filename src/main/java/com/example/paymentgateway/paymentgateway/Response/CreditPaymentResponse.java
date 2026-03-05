package com.example.paymentgateway.paymentgateway.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreditPaymentResponse {
    private String razorpayKey;
    private String orderId;
    private int amount; // in paise
    private String currency;
    private String customerName;
    private String description;
}


