package uk.gov.hmcts.reform.roleassignmentbatch.batch;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Configuration
@EnableTransactionManagement
public class DataSourceConfig {

    @Value("${spring.datasource.url}")
    String url;

    @Value("${spring.datasource.username}")
    String userName;

    @Value("${spring.datasource.password}")
    String password;

    @Value("${spring.datasource.min-idle}")
    int idleConnections;

    @Value("${spring.datasource.max-life}")
    int maxLife;

    @Value("${spring.datasource.idle-timeout}")
    int idleTimeOut;

    @Value("${spring.datasource.maximum-pool-size}")
    int maxPoolSize;

    @Bean
    public DataSource dataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.postgresql.Driver");
        dataSourceBuilder.url(url);
        dataSourceBuilder.username(userName);
        dataSourceBuilder.password(password);
        HikariDataSource dataSource = (HikariDataSource) dataSourceBuilder.build();
        dataSource.setMinimumIdle(idleConnections);
        dataSource.setIdleTimeout(idleTimeOut);
        //dataSource.setMaxLifetime(maxLife);
        dataSource.setMaximumPoolSize(maxPoolSize);
        return dataSource;
    }

    @Bean("springJdbcDataSource")
    public DataSource springJdbcDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.postgresql.Driver");
        dataSourceBuilder.url(url);
        dataSourceBuilder.username(userName);
        dataSourceBuilder.password(password);
        HikariDataSource dataSource = (HikariDataSource) dataSourceBuilder.build();
        dataSource.setMinimumIdle(idleConnections);
        dataSource.setIdleTimeout(idleTimeOut);
        //dataSource.setMaxLifetime(maxLife);
        dataSource.setMaximumPoolSize(maxPoolSize);
        return dataSourceBuilder.build();
    }

    @Bean(name = "txManager")
    public PlatformTransactionManager txManager() {
        PlatformTransactionManager platformTransactionManager = new DataSourceTransactionManager(dataSource());
        return platformTransactionManager;
    }

    @Bean
    public DefaultTransactionDefinition defaultTransactionDefinition() {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(Propagation.REQUIRED.ordinal());
        return def;
    }

    @Bean("springJdbcTemplate")
    JdbcTemplate springJdbcTemplate() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(springJdbcDataSource());
        return jdbcTemplate;
    }

    @Bean(name = "springJdbcTransactionManager")
    public PlatformTransactionManager springJdbcTransactionManager() {
        DataSourceTransactionManager platformTransactionManager = new DataSourceTransactionManager(springJdbcDataSource());
        return platformTransactionManager;
    }
}