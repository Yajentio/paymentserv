package com.example.paymentgateway.paymentgateway.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.paymentgateway.paymentgateway.Entity.BankDetails;
import com.example.paymentgateway.paymentgateway.Repository.BankDetailsRepository;

@Service
public class BankDetailsService {
	
	@Autowired
    private BankDetailsRepository repository;

    @Autowired
    private EmailService emailService;

    public BankDetails saveBankDetails(BankDetails bankDetails) {
        return repository.save(bankDetails);
    }

    public List<BankDetails> getAllBankDetails() {
        return repository.findAll();
    }

  
    // Sends the latest bank details to the student email
//    public void sendBankDetailsToStudent(String email) {
//        BankDetails details = repository.findAll()
//                .stream()
//                .findFirst()
//                .orElseThrow(() -> new RuntimeException("No bank details found"));
//        emailService.sendBankDetailsMail(email, details);
//    }
}




