package com.example.paymentgateway.paymentgateway.DTO;

import lombok.Data;

@Data
public class UpiQrRequest {
    private int amount; // in rupees
    private String description;
    
}