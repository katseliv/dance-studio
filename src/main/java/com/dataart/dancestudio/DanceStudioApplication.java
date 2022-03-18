package com.dataart.dancestudio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DanceStudioApplication {

    public static void main(String[] args) {
        System.out.println(java.time.ZoneId.systemDefault().toString());
        SpringApplication.run(DanceStudioApplication.class, args);
    }

}
