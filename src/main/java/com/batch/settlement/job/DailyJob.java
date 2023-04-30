package com.batch.settlement.job;

import org.springframework.context.annotation.Bean;

/**
 * 일별 정산 - 일별 정산이 끝났으면 API를 호출하여 정산 금액 송금 처리
 * 주별 정산
 * 월별 정산
 */
public class DailySettlementJob {

    @Bean(name = "dailySettlementJob")
    public void dailySettlementJob() {

    }

}
