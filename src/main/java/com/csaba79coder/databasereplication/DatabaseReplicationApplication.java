package com.csaba79coder.databasereplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.csaba79coder.databasereplication.entity")
public class DatabaseReplicationApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatabaseReplicationApplication.class, args);
    }

}
