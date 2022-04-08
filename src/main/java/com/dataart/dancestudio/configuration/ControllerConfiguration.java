package com.dataart.dancestudio.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.dataart.dancestudio.controller")
public class ControllerConfiguration {
}
