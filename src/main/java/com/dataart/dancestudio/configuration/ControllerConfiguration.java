package com.dataart.dancestudio.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
@ComponentScan(basePackages = {
        "com.dataart.dancestudio.controller",
        "com.dataart.dancestudio.rest"
})
public class ControllerConfiguration {

    @Value("${spring.security.oauth2.client.registration.google.timeout-in-seconds}")
    private int timeoutInSeconds;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(timeoutInSeconds))
                .setReadTimeout(Duration.ofSeconds(timeoutInSeconds))
                .build();
    }

}
