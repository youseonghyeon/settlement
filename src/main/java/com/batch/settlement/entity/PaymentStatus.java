package com.settlement.dataharvester.domain.entity;

public enum PaymentStatus {
    PAID("결제 완료"),
    CANCELLED("결제 취소"),
    PENDING("결제 대기중"),
    REFUNDED("결제 환불");

    private final String value;

    PaymentStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
