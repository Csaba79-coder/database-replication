package com.csaba79coder.databasereplication.config;

import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.csaba79coder.databasereplication.persistence", entityManagerFactoryRef = "entityManagerFactory")
@EntityScan(basePackages = "com.csaba79coder.databasereplication.entity")
@Slf4j
public class DataSourceConfig {

    @Value("${spring.datasource.master.jdbc-url}")
    private String masterUrl;

    @Value("${spring.datasource.master.username}")
    private String masterUsername;

    @Value("${spring.datasource.master.password}")
    private String masterPassword;

    @Value("${spring.datasource.master.driver-class-name}")
    private String masterDriverClassName;

    @Value("${spring.datasource.replica.jdbc-url}")
    private String replicaUrl;

    @Value("${spring.datasource.replica.username}")
    private String replicaUsername;

    @Value("${spring.datasource.replica.password}")
    private String replicaPassword;

    @Value("${spring.datasource.replica.driver-class-name}")
    private String replicaDriverClassName;

    @Primary
    @Bean
    public DataSource routingDataSource() {
        RoutingDataSource routingDataSource = new RoutingDataSource();

        // Adatforrások hozzárendelése
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("master", masterDataSource());
        targetDataSources.put("replica", replicaDataSource());

        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(masterDataSource());

        return routingDataSource;
    }

    @Bean
    public DataSource masterDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(masterUrl);
        dataSource.setUsername(masterUsername);
        dataSource.setPassword(masterPassword);
        dataSource.setDriverClassName(masterDriverClassName);
        return dataSource;
    }

    @Bean
    public DataSource replicaDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(replicaUrl);
        dataSource.setUsername(replicaUsername);
        dataSource.setPassword(replicaPassword);
        dataSource.setDriverClassName(replicaDriverClassName);
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder, DataSource routingDataSource) {
        return builder
                .dataSource(routingDataSource)
                .packages("com.csaba79coder.databasereplication.entity")
                .persistenceUnit("default")
                .build();
    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}
