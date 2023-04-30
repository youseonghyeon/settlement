package com.batch.settlement.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DailySettlement {

    @Id
    private Long sellerId;
    private Long paymentAmount;
    private LocalDateTime settlementDatetime;

    public DailySettlement(Long sellerId, Long paymentAmount) {
        this.sellerId = sellerId;
        this.paymentAmount = paymentAmount;
    }
}

