package com.example.dashboard.service;

import com.example.dashboard.dto.ApiUsageStats;
import com.example.dashboard.dto.DashboardResponse;

public interface DashboardService {

    DashboardResponse getSummary();

    ApiUsageStats getApiStats(String apiId);
}