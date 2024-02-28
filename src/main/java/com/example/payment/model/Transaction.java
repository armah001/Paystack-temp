package com.example.payment.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Transaction {
    @Id
    @GeneratedValue
    private Long id;

    private String transactionId;

    private String status;

    private double amount;

    private LocalDateTime transactionDateTime;

    private String paymentMethod;

    //change this to user
    private String customer;
}
