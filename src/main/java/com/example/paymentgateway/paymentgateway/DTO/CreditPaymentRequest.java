package com.example.paymentgateway.paymentgateway.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditPaymentRequest {
	    private int amount;        // in rupees
	    private String customerName;
	    private String description;
	}



