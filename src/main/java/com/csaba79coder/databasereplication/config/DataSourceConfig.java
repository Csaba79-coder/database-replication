package com.csaba79coder.databasereplication.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties
public class DataSourceConfig {

    @Value("${spring.datasource.url}")
    private String masterUrl;

    @Value("${spring.datasource.username}")
    private String masterUsername;

    @Value("${spring.datasource.password}")
    private String masterPassword;

    @Value("${spring.datasource.replica.url}")
    private String replicaUrl;

    @Value("${spring.datasource.replica.username}")
    private String replicaUsername;

    @Value("${spring.datasource.replica.password}")
    private String replicaPassword;

    @Bean
    @Primary
    public DataSource masterDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(masterUrl);
        dataSource.setUsername(masterUsername);
        dataSource.setPassword(masterPassword);
        return dataSource;
    }

    @Bean
    public DataSource replicaDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(replicaUrl);
        dataSource.setUsername(replicaUsername);
        dataSource.setPassword(replicaPassword);
        return dataSource;
    }

    @Bean
    public DataSource routingDataSource() {
        AbstractRoutingDataSource routingDataSource = new AbstractRoutingDataSource() {
            @Override
            protected Object determineCurrentLookupKey() {
                return isWriteOperation() ? "master" : "replica";  // Váltás master és replika között
            }

            private boolean isWriteOperation() {
                // Ha INSERT, UPDATE, DELETE akkor master, ha SELECT akkor replika
                String currentOperation = getCurrentOperationType();
                return currentOperation != null && !currentOperation.equals("SELECT");
            }

            private String getCurrentOperationType() {
                // Itt implementálhatod a művelet meghatározásának logikáját
                // Például HTTP kérésből vagy más forrásból is nyerhető információ
                return "SELECT";  // A logikát itt módosíthatod, hogy a művelet típusát ellenőrizze
            }
        };

        // Adatforrások hozzárendelése
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("master", masterDataSource());  // Master adatforrás
        targetDataSources.put("replica", replicaDataSource());  // Replica adatforrás

        routingDataSource.setTargetDataSources(targetDataSources);  // Target adatforrások beállítása
        routingDataSource.setDefaultTargetDataSource(masterDataSource());  // Alapértelmezett adatforrás

        return routingDataSource;
    }
}
