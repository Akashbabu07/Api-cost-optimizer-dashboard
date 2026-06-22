package com.example.dashboard.client;

import com.example.dashboard.dto.ApiUsageStats;
import com.example.dashboard.dto.DashboardResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "analytics-service",
        url = "${analytics.service.url}",
        fallbackFactory = AnalyticsClientFallbackFactory.class
)
public interface AnalyticsClient {

    @GetMapping("/analytics/summary")
    DashboardResponse getSummary();

    @GetMapping("/analytics/api-stats")
    ApiUsageStats getApiStats(@RequestParam String apiId);
}