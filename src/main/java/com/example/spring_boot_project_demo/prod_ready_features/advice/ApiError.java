package com.example.spring_boot_project_demo.prod_ready_features.advice;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ApiError {
    private LocalDateTime timestamp;
    private String Error;
    private HttpStatus status;
    public ApiError() {
        this.timestamp = LocalDateTime.now();
    }
    public ApiError(String error, HttpStatus status) {
        this.Error = error;
        this.status = status;
    }
}
