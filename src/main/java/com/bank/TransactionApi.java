package com.bank;

import com.batch.settlement.dto.TransactionRequest;
import com.batch.settlement.entity.TransactionHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionApi {

    private Long seq = 1L;

    public TransactionHistory request(TransactionRequest t) {
        seq++;
//        if (seq % 12345 == 0) {
//            throw new RuntimeException("API ERROR");
//        }

        if (seq % 700 == 0) {
            // ERROR case
            return new TransactionHistory(t.getStoreId(),
                    t.getAccountNumber(),
                    t.getBankName(),
                    LocalDateTime.now(),
                    t.getAmount().intValue(),
                    50,
                    "FAIL");

        }


        return new TransactionHistory(t.getStoreId(),
                t.getAccountNumber(),
                t.getBankName(),
                LocalDateTime.now(),
                t.getAmount().intValue(),
                50,
                "SUCCESS");
    }


}
