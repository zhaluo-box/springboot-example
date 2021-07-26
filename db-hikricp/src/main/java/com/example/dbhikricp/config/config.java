package com.example.dbhikricp.config;

import com.example.dbhikricp.config.condition.JdbcTemplateCondition;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import java.sql.SQLException;


@Slf4j
@Configuration
public class config {

    @Value("${spring.datasource.driverClassName}")
    private String driverClass;

    @Value("${spring.datasource.url}")
    private String uri;

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).driverClassName(driverClass).url(uri).username("root").password("root").build();
    }


    @Bean
    @DependsOn("dataSource")
    @Conditional(JdbcTemplateCondition.class)
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        try {
            dataSource.getConnection().createStatement().execute("select 1");
            log.info("jdbcTemplate init success!");
        } catch (SQLException e) {
            log.error("jdbcTemplate init  fail!");
            throw new RuntimeException(e);
        }
        return new JdbcTemplate(dataSource);
    }

    public HikariDataSource createHikariDataSource(String driverName, String password, String url, String username, boolean autoCommit, String validationQuery,
                                                   int connectionTimeout, int maxPoolSize, int minimumIdle, int idleTimeout, int maxLifetime) {
        HikariDataSource hikariDataSource = DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .driverClassName(driverName)
                .password(password)
                .url(url)
                .username(username)
                .build();
        hikariDataSource.setAutoCommit(autoCommit);
        hikariDataSource.setConnectionInitSql(validationQuery);
        hikariDataSource.setConnectionTimeout(connectionTimeout);
        hikariDataSource.setMaximumPoolSize(maxPoolSize);
        hikariDataSource.setMinimumIdle(minimumIdle);
        hikariDataSource.setIdleTimeout(idleTimeout);
        hikariDataSource.setMaxLifetime(maxLifetime);
        return hikariDataSource;
    }
}
