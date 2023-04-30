package com.batch.settlement.dto;

import com.batch.settlement.entity.Payment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentV0 {

    private Long productId;
    private long paymentAmount;
}
