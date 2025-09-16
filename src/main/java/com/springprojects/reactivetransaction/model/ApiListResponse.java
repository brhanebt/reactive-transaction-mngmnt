package com.springprojects.reactivetransaction.model;

import lombok.Data;
import java.util.List;

@Data
public class ApiListResponse {
    private String statusCode;
    private List<RandomUser> data;
    private String message;
    private boolean success;
}