package com.batch.settlement.job;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
class DailyJobTest {

    @Autowired
    private JobTestUtils jobTestUtils;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void dailyJobTest() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobLauncherTestUtils dailySettlementJob = jobTestUtils.getJobTester("dailySettlementJob");
        JobLauncher jobLauncher = dailySettlementJob.getJobLauncher();

        JobParameters jobParameters = jobTestUtils.getRandomJobParameters();
        JobParameters jobParameters2 = new JobParametersBuilder(jobParameters)
                .addString("targetDate", "2023-04-23")
                .toJobParameters();
        jobLauncher.run(dailySettlementJob.getJob(), jobParameters2);
    }

    @Test
    void trunRcate() {
        jdbcTemplate.execute("truncate table daily_settlement");
    }




}
