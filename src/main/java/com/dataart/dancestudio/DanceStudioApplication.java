package com.dataart.dancestudio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:constants.properties")
@SpringBootApplication
public class DanceStudioApplication {

    public static void main(final String[] args) {
        SpringApplication.run(DanceStudioApplication.class, args);
    }

}
