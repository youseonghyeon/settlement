package com.batch.settlement.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "weekly_settlement")
public class WeeklySettlement {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "year")
    private Long year;

    @Column(name = "week")
    private Long week;

    @Column(name = "store_id")
    private Long storeId;

    @Column(name = "order_count")
    private Long orderCount;

    @Column(name = "order_price")
    private Long orderPrice;

    @Column(name = "delivery_fee")
    private Long deliveryFee;

    @Column(name = "commission")
    private Long commission;

    @Column(name = "store_profit")
    private Long storeProfit;

    @Column(name = "delivery_profit")
    private Long deliveryProfit;

    @Column(name = "user_payment")
    private Long userPayment;

    @Column(name = "settlement_amount")
    private Long settlementAmount;

}
