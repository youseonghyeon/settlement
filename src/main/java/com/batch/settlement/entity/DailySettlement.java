package com.batch.settlement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "daily_settlement")
@ToString
public class DailySettlement {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "settlement_date")
    private LocalDate settlementDate;

    @Column(name = "store_id")
    private Long storeId;

    @Column(name = "order_count")
    private Long orderCount;

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
