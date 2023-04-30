package com.batch.settlement.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ElapsedTimeListener implements JobExecutionListener {

    private long startTime;


    @Override
    public void beforeJob(JobExecution jobExecution) {
        startTime = System.currentTimeMillis();
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        long elapsedTime = System.currentTimeMillis() - startTime;
        log.info("elapsedTime = {}", elapsedTime);
    }
}
