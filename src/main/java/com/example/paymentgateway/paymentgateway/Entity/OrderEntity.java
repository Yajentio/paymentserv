package com.example.paymentgateway.paymentgateway.Entity;

import java.time.Instant;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "order_details")
public class OrderEntity {

    @Id
    private String id; 
    
    
    
    private long od_amount;
    private String od_currency;
    private String od_receipt;
    private String od_status;

    @Column(name = "od_created_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant od_created_at;

    public OrderEntity() {}

    public OrderEntity(String id, long amount, String currency, String receipt, String status, Instant created_at) {
        this.id = id;
        this.od_amount = amount;
        this.od_currency = currency;
        this.od_receipt = receipt;
        this.od_status = status;
        this.od_created_at = created_at;
    }
    
 }