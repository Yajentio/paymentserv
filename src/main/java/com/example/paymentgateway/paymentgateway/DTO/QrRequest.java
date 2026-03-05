package com.example.paymentgateway.paymentgateway.DTO;

import lombok.Data;

@Data
public class QrRequest {
    private double amount;
    private String orderId;
}

