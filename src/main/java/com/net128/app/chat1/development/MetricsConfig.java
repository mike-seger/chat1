package com.net128.app.chat1.development;

import io.prometheus.client.CollectorRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {
    @Bean
    public CollectorRegistry prometheusCollectorRegistry() {
        return CollectorRegistry.defaultRegistry;
    }
}
