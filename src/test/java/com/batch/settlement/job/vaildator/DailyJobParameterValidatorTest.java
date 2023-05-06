package com.batch.settlement.job.vaildator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class DailyJobParameterValidatorTest {

    @Autowired
    private DailyJobParameterValidator dailyJobParameterValidator;

    @Test
    @DisplayName("날짜 검증 성공 테스트")
    void testValidationSuccess() throws JobParametersInvalidException {
        // given
        JobParameters jobParameters = JOB_파라미터_생성("targetDate", "2021-05-02");
        // then - success
        dailyJobParameterValidator.validate(jobParameters);
    }

    @Test
    @DisplayName("날짜 검증 실패 테스트(날짜 형식이 아닌 경우)")
    void testValidationFailNotSupport() throws JobParametersInvalidException {
        // given
        JobParameters jobParameters = JOB_파라미터_생성("targetDate", "hello world!");

        // then
        assertThrows(JobParametersInvalidException.class, () -> {
            dailyJobParameterValidator.validate(jobParameters);
        });
    }


    @Test
    @DisplayName("날짜 검증 실패 테스트(날짜 범위에서 벗어난 경우)")
    void testValidationFailOutOfRange() throws JobParametersInvalidException {
        // given
        JobParameters jobParameters = JOB_파라미터_생성("targetDate", "2023-65-11");

        // then
        assertThrows(JobParametersInvalidException.class, () -> {
            dailyJobParameterValidator.validate(jobParameters);
        });
    }


    private JobParameters JOB_파라미터_생성(String key, String value) {
        return new JobParametersBuilder()
                .addString(key, value)
                .toJobParameters();
    }

}
