package com.example.dashboard.client;

import com.example.dashboard.client.AnalyticsClient;
import com.example.dashboard.dto.ApiUsageStats;
import com.example.dashboard.dto.DashboardResponse;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class AnalyticsClientFallbackFactory implements FallbackFactory<AnalyticsClient> {

    @Override
    public AnalyticsClient create(Throwable cause) {

        return new AnalyticsClient() {

            @Override
            public DashboardResponse getSummary() {

                return DashboardResponse.builder()
                        .totalHourlyRecords(0)
                        .totalDailyRecords(0)
                        .build();
            }

            @Override
            public ApiUsageStats getApiStats(String apiId) {

                ApiUsageStats stats = new ApiUsageStats();
                stats.setApiId(apiId);
                stats.setTotalHits(0);
                return stats;
            }
        };
    }
}