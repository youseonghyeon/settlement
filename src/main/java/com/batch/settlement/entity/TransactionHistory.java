package com.batch.settlement.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class TransactionHistory {

    @Id
    @GeneratedValue
    private Long id;

    private Long storeId;

    private String accountNumber;

    private String bankName;

    private LocalDateTime paymentTime;

    private int amount;

    private int fee;

    private String status;

    public TransactionHistory(Long storeId, String accountNumber, String bankName, LocalDateTime paymentTime, int amount, int fee, String status) {
        this.storeId = storeId;
        this.accountNumber = accountNumber;
        this.bankName = bankName;
        this.paymentTime = paymentTime;
        this.amount = amount;
        this.fee = fee;
        this.status = status;
    }

}
