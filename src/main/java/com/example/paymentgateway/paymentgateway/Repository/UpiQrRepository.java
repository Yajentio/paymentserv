package com.example.paymentgateway.paymentgateway.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.paymentgateway.paymentgateway.Entity.UpiQrTransaction;
import java.util.Optional;

@Repository
public interface UpiQrRepository
        extends JpaRepository<UpiQrTransaction, Long> {

	Optional<UpiQrTransaction> findByOrderId(String orderId);
}







