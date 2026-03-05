package com.example.paymentgateway.paymentgateway.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.paymentgateway.paymentgateway.Entity.PaymentEntity;




@Repository
public interface PaymentRepository  extends JpaRepository<PaymentEntity, Long>{

	boolean existsByRazorpayPaymentId(String razorpayPaymentId);

	
}
