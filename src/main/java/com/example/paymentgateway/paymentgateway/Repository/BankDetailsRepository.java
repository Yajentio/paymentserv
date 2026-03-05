package com.example.paymentgateway.paymentgateway.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.paymentgateway.paymentgateway.Entity.BankDetails;


@Repository
public interface BankDetailsRepository extends JpaRepository<BankDetails, Long> {

}

