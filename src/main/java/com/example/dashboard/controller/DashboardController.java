package com.example.dashboard.controller;

import com.example.dashboard.dto.ApiUsageStats;
import com.example.dashboard.dto.DashboardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.example.dashboard.service.DashboardService;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    public DashboardResponse getSummary() {
        return dashboardService.getSummary();
    }

    @GetMapping("/api-stats")
    public ApiUsageStats getApiStats(@RequestParam String apiId) {
        return dashboardService.getApiStats(apiId);
    }
}