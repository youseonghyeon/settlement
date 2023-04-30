package com.batch.settlement;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class JobTestUtils {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private JobLauncher jobLauncher;

    public JobLauncherTestUtils getJobTester(String jobName) {
        Job bean = applicationContext.getBean(jobName, Job.class);
        JobLauncherTestUtils jobLauncherTestUtils = new JobLauncherTestUtils();
        jobLauncherTestUtils.setJobLauncher(jobLauncher);
        jobLauncherTestUtils.setJobRepository(jobRepository);
        jobLauncherTestUtils.setJob(bean);
        return jobLauncherTestUtils;
    }

}
