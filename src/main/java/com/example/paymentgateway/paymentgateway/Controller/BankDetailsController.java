package com.example.paymentgateway.paymentgateway.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.paymentgateway.paymentgateway.Entity.BankDetails;
import com.example.paymentgateway.paymentgateway.Service.BankDetailsService;

@RestController
public class BankDetailsController {

	
	@Autowired
    private BankDetailsService service;

    @PostMapping("/banksave")
    public BankDetails saveBankDetails(@RequestBody BankDetails bankDetails) {
        return service.saveBankDetails(bankDetails);
    }

    @GetMapping("/getallbankdetails")
    public List<BankDetails> getAllBankDetails() {
        return service.getAllBankDetails();
    }

    

    // Send bank details to student's email
//    @PostMapping("/send-email")
//    public String sendBankDetailsEmail(@RequestParam String email) {
//        service.sendBankDetailsToStudent(email);
//        return "Bank details sent successfully to: " + email;
//    }
}


