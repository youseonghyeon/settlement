package com.batch.settlement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class TransactionRequest {

    private Long storeId;
    private String  bankName;
    private String accountNumber;
    private Long amount;

}
