package com.csaba79coder.databasereplication;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.EnabledIf;

@EnabledIf(expression = "#{false}", reason = "Disabling test execution")
@SpringBootTest
class DatabaseReplicationApplicationTests {

    @Test
    void contextLoads() {
    }

}
