package com.batch.settlement.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import retry.Retryable;

@RestController
public class TestController {

    @PostMapping
    public void settlementRequest() {

    }

    @GetMapping("/ex")
    @Retryable(include = IllegalStateException.class)
    public String testReq() {

        throw new IllegalStateException("임의 생성 예외");
    }
}
