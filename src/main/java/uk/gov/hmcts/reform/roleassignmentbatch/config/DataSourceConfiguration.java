package uk.gov.hmcts.reform.roleassignmentbatch.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {

    @Bean(name = "primaryDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();

    }

    @Bean(name = "secondaryDataSource")
    @ConfigurationProperties(prefix = "spring.judicial.datasource")
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create().build();

    }


}