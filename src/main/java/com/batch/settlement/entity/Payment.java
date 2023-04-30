package com.batch.settlement.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;

    private Long paymentAmount;

    private String paymentMethod;

    private LocalDateTime paymentDatetime;

    private String paymentStatus;

    private Long sellerId;

    private Long buyerId;


    public static Payment createRandom() {
        Payment p = new Payment();
        p.productId = r();
        p.paymentAmount = r();
        p.paymentMethod = "CREDIT_CARD";
        p.paymentDatetime = LocalDateTime.now();
        p.paymentStatus = "PAID";
        p.sellerId = r();
        p.buyerId = r();
        return p;
    }

    private static Long r() {
        Double v = (Double) Math.random() * 10000;
        return v.longValue();
    }

}
