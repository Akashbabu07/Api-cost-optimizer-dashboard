package com.example.dashboard.util;

public class CacheKeyUtil {

    public static String dashboardSummary() {
        return "dashboard:v1:summary";
    }

    public static String dashboardApiStats(String apiId) {
        return "dashboard:v1:api:" + apiId;
    }
}