package com.springprojects.reactivetransaction.model;

import lombok.Data;

@Data
public class ApiResponse {
    private String statusCode;
    private RandomUser data;
    private String message;
    private boolean success;
}