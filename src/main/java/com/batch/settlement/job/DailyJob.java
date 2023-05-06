package com.batch.settlement.job;

import com.bank.TransactionApi;
import com.batch.settlement.dto.TransactionRequest;
import com.batch.settlement.entity.DailySettlement;
import com.batch.settlement.entity.TransactionHistory;
import com.batch.settlement.job.vaildator.DailyJobParameterValidator;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

/**
 * 일별 정산 - 일별 정산이 끝났으면 API를 호출하여 정산 금액 송금 처리
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class DailyJob {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager tm;
    private final EntityManagerFactory emf;
    private final JdbcTemplate jdbcTemplate;
    private final DailyJobParameterValidator dailyJobParameterValidator;
    private final TransactionApi transactionApi;


    @Value("${api.payment.chunk-size}")
    private int chunkSize;

    @Bean(name = "dailySettlementJob")
    public Job dailySettlementJob() {
        log.info("em={}", emf);
        return new JobBuilder("dailySettlementJob", jobRepository)
                .validator(dailyJobParameterValidator)
                .start(insertTotalPaymentAmountOneDay(null))
                .next(sendPaymentViaAPI())
                .build();
    }

    @Bean
    @JobScope
    public Step sendPaymentViaAPI() {
        return new StepBuilder("sendPaymentViaAPI", jobRepository)
                .<DailySettlement, TransactionHistory>chunk(chunkSize, tm)
                .reader(dailySettlementReader(null))
                .processor(dailySettlementProcessor())
                .writer(dailySettlementWriter())
                .build();
    }


    @Bean
    public ItemProcessor<DailySettlement, TransactionHistory> dailySettlementProcessor() {
        return (item) -> {
            // api 요청 및 결과 받기
            // TODO 에러가 발생하면 어떻게 처리할 것인가? -> 테스트도 한번 해보자
            TransactionRequest requestParameters = new TransactionRequest(item.getStoreId(), "SINHAN", "0000-000000-00", item.getSettlementAmount());
            TransactionHistory history = transactionApi.request(requestParameters);
            return history;
        };
    }

    @Bean
    public ItemWriter<TransactionHistory> dailySettlementWriter() {
        return new JpaItemWriterBuilder<TransactionHistory>()
                .entityManagerFactory(emf)
                .build();
    }


    @Bean
    @StepScope
    public JpaPagingItemReader<DailySettlement> dailySettlementReader(@Value("#{jobParameters[targetDate]}") String targetDate) {
        LocalDate parsedDate = LocalDate.parse(targetDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        return new JpaPagingItemReaderBuilder<DailySettlement>()
                .name("dailySettlementReader")
                .entityManagerFactory(emf)
                .pageSize(chunkSize)
                .queryString("select d from DailySettlement d where d.settlementDate = :targetDate")
                .parameterValues(Collections.singletonMap("targetDate", parsedDate))
                .build();
    }

    @Bean
    @JobScope
    public Step insertTotalPaymentAmountOneDay(@Value("#{jobParameters[targetDate]}") String targetDate) {
        return new StepBuilder("paymentSum", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    log.info("Execute daily settlement.");
                    String query = "insert into daily_settlement (delivery_fee, delivery_profit, order_count, order_price, settlement_amount, settlement_date, store_id, user_payment) " +
                            "select sum(s.delivery_fee), sum(s.delivery_profit), count(*), sum(s.order_price), sum(s.settlement_amount), ?, s.store_id, sum(s.user_payment) " +
                            "from Settlement s " +
                            "where date(s.settlement_datetime) = ? " +
                            "group by store_id, date(settlement_datetime)";
                    jdbcTemplate.update(query, targetDate, targetDate);
                    return RepeatStatus.FINISHED;
                }, tm)
                .build();
    }


    @Bean
    public ItemWriter<DailySettlement> paymentItemWriter() {
        return new JpaItemWriterBuilder<DailySettlement>()
                .entityManagerFactory(emf)
                .build();
    }
}
