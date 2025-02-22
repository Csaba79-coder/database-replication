package com.csaba79coder.databasereplication.config;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.PlatformTransactionManager;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class DataSourceConfig {

    @Value("${spring.datasource.url}")
    private String masterDbUrl;

    @Value("${spring.datasource.username}")
    private String masterDbUsername;

    @Value("${spring.datasource.password}")
    private String masterDbPassword;

    @Value("${spring.datasource.replica.url}")
    private String replicaDbUrl;

    @Value("${spring.datasource.replica.username}")
    private String replicaDbUsername;

    @Value("${spring.datasource.replica.password}")
    private String replicaDbPassword;

    // Fő adatforrás beállítása
    @Bean
    @Primary
    public DataSource masterDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(masterDbUrl);
        dataSource.setUsername(masterDbUsername);
        dataSource.setPassword(masterDbPassword);
        return dataSource;
    }

    // Replikált adatforrás beállítása
    @Bean
    public DataSource replicaDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(replicaDbUrl);
        dataSource.setUsername(replicaDbUsername);
        dataSource.setPassword(replicaDbPassword);
        return dataSource;
    }

    // EntityManagerFactory beállítása a fő adatforrással
    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource masterDataSource) {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(masterDataSource);
        factoryBean.setPackagesToScan("com.csaba79coder.databasereplication"); // Az entitásokat tartalmazó csomag neve
        factoryBean.setPersistenceProvider(new HibernatePersistenceProvider()); // Beállítjuk a PersistenceProvider-t
        return factoryBean;
    }

    // TransactionManager beállítása a fő EntityManagerFactory-hoz
    @Bean
    @Primary
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    // EntityManagerFactory beállítása a replikált adatforrással (ha szükséges)
    @Bean
    public LocalContainerEntityManagerFactoryBean replicaEntityManagerFactory(DataSource replicaDataSource) {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(replicaDataSource);
        factoryBean.setPackagesToScan("com.csaba79coder.databasereplication"); // A replikált entitásokat tartalmazó csomag neve
        factoryBean.setPersistenceProvider(new HibernatePersistenceProvider()); // Beállítjuk a PersistenceProvider-t
        return factoryBean;
    }

    // TransactionManager beállítása a replikált EntityManagerFactory-hoz (ha szükséges)
    @Bean
    public PlatformTransactionManager replicaTransactionManager(EntityManagerFactory replicaEntityManagerFactory) {
        return new JpaTransactionManager(replicaEntityManagerFactory);
    }
}
