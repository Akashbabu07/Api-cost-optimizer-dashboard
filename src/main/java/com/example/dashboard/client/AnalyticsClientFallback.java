package com.example.dashboard.client;

import com.example.dashboard.dto.ApiUsageStats;
import com.example.dashboard.dto.DashboardResponse;
import org.springframework.stereotype.Component;

@Component
public class AnalyticsClientFallback implements AnalyticsClient {

    @Override
    public DashboardResponse getSummary() {

        return DashboardResponse.builder()
                .totalHourlyRecords(-1)
                .totalDailyRecords(-1)
                .build();
    }

    @Override
    public ApiUsageStats getApiStats(String apiId) {

        ApiUsageStats stats = new ApiUsageStats();
        stats.setApiId(apiId);
        stats.setTotalHits(-1);
        return stats;
    }
}