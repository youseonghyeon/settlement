package com.batch.settlement.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "settlement")
public class Settlement {

    @Id
    @GeneratedValue
    @Column(name = "settlement_id")
    private Long settlementId;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "store_id")
    private Long storeId;

    @Column(name = "delivery_id")
    private Long deliveryId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "order_datetime")
    private LocalDateTime orderDateTime;

    @Column(name = "delivery_datetime")
    private LocalDateTime deliveryDateTime;

    @Column(name = "settlement_datetime")
    private LocalDateTime settlementDateTime;

    @Column(name = "order_price")
    private Long orderPrice;

    @Column(name = "delivery_fee")
    private Long deliveryFee;

    @Column(name = "delivery_profit")
    private Long deliveryProfit;

    @Column(name = "user_payment")
    private Long userPayment;

    @Column(name = "settlement_amount")
    private Long settlementAmount;
}
