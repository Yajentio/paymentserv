package com.example.paymentgateway.paymentgateway.Entity;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "payment_details")
public class PaymentEntity {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "payment_id", nullable = false, unique = true)
    private Long id;
    
    @Column(name = "razorpay_payment_id", nullable = false, unique = true)
    private String razorpayPaymentId;  // 🔥 Actual Razorpay payment ID


    @Column(name = "payment_name")
    private String name;

    @Column(nullable = false)
    private long amount;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(nullable = false)
    private String status;

    @Column(name = "order_id", nullable = false)
    private String orderId;

    private String description;
    
    @Column(name = "payment_method")
    private String paymentMethod; // CARD / UPI / NETBANKING


    @Column(name = "card_type")
    private String cardType; // CREDIT / DEBIT / NA

    @Column(name = "created_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime createdAt;

    public PaymentEntity() {}

    public PaymentEntity(Long id, String name, long amount, String currency, String status, String orderId, String description, OffsetDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.orderId = orderId;
        this.description = description;
        this.createdAt = createdAt;
    }
}