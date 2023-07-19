package com.service.ttucktak;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;

import static org.junit.Assert.assertTrue;

@SpringBootTest(properties = "spring.config.location= classpath:/application.yml,classpath:/secret.yml")
class TtuckTakApplicationTests {

    @Test
    void contextLoads() {
        Assertions.assertTrue(true);
    }

}
