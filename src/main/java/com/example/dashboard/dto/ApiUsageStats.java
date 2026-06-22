package com.example.dashboard.dto;

import lombok.Data;

@Data
public class ApiUsageStats {

    private String apiId;
    private long totalHits;
}