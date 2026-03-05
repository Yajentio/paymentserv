package com.example.paymentgateway.paymentgateway.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpiQrResponse {
    private String qrId;
    private String imageUrl;
    
   
}