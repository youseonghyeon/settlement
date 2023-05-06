package com.batch.settlement.job.vaildator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class DailyJobParameterValidator implements JobParametersValidator {

    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {

        if (parameters == null) {
            throw new JobParametersInvalidException("Job parameters must not be null");
        }

        String targetDateStr = parameters.getString("targetDate");

        if (targetDateStr == null) {
            throw new JobParametersInvalidException("targetDate parameter is missing");
        }

        try {
            LocalDate parsed = LocalDate.parse(targetDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        } catch (DateTimeParseException e) {

            throw new JobParametersInvalidException("Invalid target date format: " + targetDateStr);
        }
    }
}
