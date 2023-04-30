package com.batch.settlement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransferForm {
    private Long senderId;
    private Long receiverId;
    private Long amount;
    private Long fee;
}
