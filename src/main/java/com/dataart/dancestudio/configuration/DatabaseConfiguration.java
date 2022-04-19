package com.dataart.dancestudio.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = {
        "com.dataart.dancestudio.model.entity",
        "com.dataart.dancestudio.converter"
})
@EnableJpaRepositories(basePackages = "com.dataart.dancestudio.repository")
public class DatabaseConfiguration {
}
