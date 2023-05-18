package uk.gov.hmcts.reform.roleassignmentbatch.config;

import com.microsoft.applicationinsights.TelemetryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppInsightsConfig {

    @Bean
    public TelemetryClient telemetryClient() {
        return new TelemetryClient();
    }
}