package com.example.dashboard.service.impl;

import com.example.dashboard.client.AnalyticsClient;
import com.example.dashboard.config.CacheTtlConfig;
import com.example.dashboard.dto.ApiUsageStats;
import com.example.dashboard.dto.DashboardResponse;
import com.example.dashboard.service.DashboardService;
import com.example.dashboard.util.CacheKeyUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final AnalyticsClient analyticsClient;
    private final RedisTemplate<String, Object> redisTemplate;
    private final CacheTtlConfig.TtlProperties ttl;

    @Override
    public DashboardResponse getSummary() {

        String key = CacheKeyUtil.dashboardSummary();

        DashboardResponse cached =
                (DashboardResponse) redisTemplate.opsForValue().get(key);

        if (cached != null) {
            return cached;
        }

        try {

            DashboardResponse response = analyticsClient.getSummary();

            // ✅ FIX: validate response before caching
            validateDashboardResponse(response);

            redisTemplate.opsForValue()
                    .set(key, response, ttl.dashboardSummary());

            return response;

        } catch (Exception ex) {

            DashboardResponse fallback =
                    (DashboardResponse) redisTemplate.opsForValue().get(key);

            if (fallback != null) {
                return fallback;
            }

            throw new RuntimeException(
                    "Analytics Service unavailable",
                    ex
            );
        }
    }

    @Override
    public ApiUsageStats getApiStats(String apiId) {

        String key = CacheKeyUtil.dashboardApiStats(apiId);

        ApiUsageStats cached =
                (ApiUsageStats) redisTemplate.opsForValue().get(key);

        if (cached != null) {
            return cached;
        }

        try {

            ApiUsageStats response =
                    analyticsClient.getApiStats(apiId);

            // optional safety check (recommended)
            validateApiStats(response);

            redisTemplate.opsForValue()
                    .set(key, response, ttl.apiStats());

            return response;

        } catch (Exception ex) {

            ApiUsageStats fallback =
                    (ApiUsageStats) redisTemplate.opsForValue().get(key);

            if (fallback != null) {
                return fallback;
            }

            throw new RuntimeException(
                    "Analytics Service unavailable",
                    ex
            );
        }
    }


    private void validateDashboardResponse(DashboardResponse response) {
        if (response == null) {
            throw new IllegalArgumentException("Dashboard response is null");
        }

        if (response.getTotalHourlyRecords() < 0 ||
                response.getTotalDailyRecords() < 0) {
            throw new IllegalArgumentException("Invalid dashboard metrics received");
        }
    }

    private void validateApiStats(ApiUsageStats response) {
        if (response == null) {
            throw new IllegalArgumentException("API stats response is null");
        }

        if (response.getTotalHits() < 0) {
            throw new IllegalArgumentException("Invalid API stats received");
        }
    }
}