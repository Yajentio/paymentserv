package com.example.paymentgateway.paymentgateway.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.paymentgateway.paymentgateway.Entity.OrderEntity;



@Repository
public interface OrderRepository  extends JpaRepository<OrderEntity, String> {


}