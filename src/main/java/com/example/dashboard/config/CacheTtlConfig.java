package com.example.dashboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CacheTtlConfig {

    @Bean
    public TtlProperties ttlProperties() {
        return new TtlProperties();
    }

    public static class TtlProperties {

        public Duration dashboardSummary() {
            return Duration.ofMinutes(5);
        }

        public Duration apiStats() {
            return Duration.ofMinutes(3);
        }
    }
}