package com.bohdan.abramovych.axon_software;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AxonSoftwareApplication {

    public static void main(String[] args) {
        SpringApplication.run(AxonSoftwareApplication.class, args);
    }

}
