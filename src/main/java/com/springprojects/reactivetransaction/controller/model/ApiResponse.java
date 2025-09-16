package com.springprojects.reactivetransaction.controller.model;

import lombok.Data;
import java.util.List;

@Data
public class ApiResponse {
    private String statusCode;
    private RandomUser data;
    private String message;
    private boolean success;
}