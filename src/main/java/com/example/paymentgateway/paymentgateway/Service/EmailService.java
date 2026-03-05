package com.example.paymentgateway.paymentgateway.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.paymentgateway.paymentgateway.Entity.BankDetails;

@Service
public class EmailService {
	
	@Autowired
    private JavaMailSender mailSender;

    public void sendBankDetailsMail(String toEmail, BankDetails details) {
        String subject = "Student Fee Payment - Bank Account Details";
        String body = "Dear Student,\n\nPlease transfer your fee to the following bank account:\n\n" +
                "Bank Name   : " + details.getBankName() + "\n" +
                "Account No  : " + details.getAccountNumber() + "\n" +
                "IFSC Code   : " + details.getIfscCode() + "\n" +
                "Branch      : " + details.getBranch() + "\n" +
                "UPI ID      : " + details.getUpiId() + "\n\n" +
                "After payment, please send us the transaction ID for confirmation.\n\nThank You,\nYTA Intership Academy";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}



