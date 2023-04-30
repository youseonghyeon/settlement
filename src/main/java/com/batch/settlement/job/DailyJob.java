package com.batch.settlement.job;

import com.batch.settlement.dto.TransferForm;
import com.batch.settlement.entity.DailySettlement;
import com.batch.settlement.entity.Payment;
import com.batch.settlement.service.TransferService;
import com.batch.settlement.tasklet.DailyTasklet;
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
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 일별 정산 - 일별 정산이 끝났으면 API를 호출하여 정산 금액 송금 처리
 * 주별 정산
 * 월별 정산
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class DailyJob {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager tm;
    private final EntityManagerFactory em;
    private final DailyTasklet dailyTasklet;
    private final JdbcTemplate jdbcTemplate;
    private final TransferService transferService;

    @Bean(name = "dailySettlementJob")
    public Job dailySettlementJob() {
        log.info("em={}", em);
        return new JobBuilder("dailySettlementJob", jobRepository)
                .start(insertTotalPaymentAmountOneDay(null))
                .next(transferTotalPaymentAmountOneDay(null))
                .build();
    }


    @Bean
    @JobScope
    public Step transferTotalPaymentAmountOneDay(@Value("#{jobParameters[targetDate]}") String targetDate) {
        return new StepBuilder("transferTotalPaymentAmountOneDay", jobRepository)
                .<DailySettlement, DailySettlement>chunk(100, tm)
                .reader(dailySettlementReader(targetDate))
                .processor(dailySettlementProcessor())
                .writer(new ItemWriter<DailySettlement>() {
                    @Override
                    public void write(Chunk<? extends DailySettlement> chunk) throws Exception {
                        List<? extends DailySettlement> items = chunk.getItems();
                        
                    }
                })
                .build();

    }

    @Bean
    @StepScope
    public ItemProcessor<? super DailySettlement, ? extends DailySettlement> dailySettlementProcessor() {
        return dailySettlement -> {
            TransferForm transferForm = new TransferForm(-1L, dailySettlement.getSellerId(), dailySettlement.getPaymentAmount(), dailySettlement.getPaymentAmount() / 100 * 9);
            boolean transferStatus = transferService.transferApi(transferForm);
            return dailySettlement;
        };
    }


    @Bean
    @StepScope
    public JpaPagingItemReader<DailySettlement> dailySettlementReader(@Value("#{jobParameters[targetDate]}") String targetDate) {
        return new JpaPagingItemReaderBuilder<DailySettlement>()
                .name("dailySettlementReader")
                .entityManagerFactory(em)
                .pageSize(100)
                .queryString("select d from DailySettlement d where d.settlementDatetime = '" + targetDate + "'")

                .build();
    }

    @Bean(destroyMethod = "")
    @JobScope
    public Step insertTotalPaymentAmountOneDay(@Value("#{jobParameters[targetDate]}") String targetDate) {
        LocalDate ParsedDate = LocalDate.parse(targetDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate nextDate = ParsedDate.plusDays(1);
        String startDate = ParsedDate.toString() + " 00:00:00";
        String endDate = nextDate.toString() + " 00:00:00";

        return new StepBuilder("paymentSum", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    log.info("SQL 실행");
                    String sql = "insert into daily_settlement " +
                            "(select seller_id, sum(payment_amount) as payment_amount, '" + startDate + "' " +
                            "from payment " +
                            "where payment_datetime between '" + startDate + "' and '" + endDate + "' " +
                            "group by seller_id);";
                    log.info("sql={}", sql);
                    jdbcTemplate.execute(sql);
                    return RepeatStatus.FINISHED;
                }, tm)
                .build();
    }


    @Bean
    public ItemReader<Payment> paymentItemReader() {
        return new JpaPagingItemReaderBuilder<Payment>()
                .name("paymentItemReader")
                .entityManagerFactory(em)
                .pageSize(100)
                .queryString("select p from Payment p")
                .build();

    }

    @Bean
    public ItemWriter<DailySettlement> paymentItemWriter() {
        return new JpaItemWriterBuilder<DailySettlement>()
                .entityManagerFactory(em)
                .build();
    }
}
