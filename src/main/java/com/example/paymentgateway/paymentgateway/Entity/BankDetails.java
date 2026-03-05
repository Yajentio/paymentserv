package com.example.paymentgateway.paymentgateway.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name ="bank_details")
@Data
public class BankDetails {
	
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Column(name="bank_name")
    private String bankName;
	
	@Column(name="account_number")
    private String accountNumber;
	
	@Column(name="ifsc_code")
    private String ifscCode;
	
	@Column(name="branch")
    private String branch;
	
	@Column(name="upi_id")
    private String upiId;

}




