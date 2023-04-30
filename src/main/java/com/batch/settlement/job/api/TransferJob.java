package com.batch.settlement.job.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TransferJob {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager tm;


    @Bean(name = "wireTransferJob")
    public Job wireTransferJob() {
        return new JobBuilder("wireTransferJob", jobRepository)
                .start(wireTransferStep())
                .build();
    }

    @Bean
    public Step wireTransferStep () {
        return new StepBuilder("wireTransferStep", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    log.info("WireTransferJob.wireTransferStep");
                    return RepeatStatus.FINISHED;
                }), tm)
                .build();
    }



}
