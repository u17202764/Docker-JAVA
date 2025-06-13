package com.example.demodockerfile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing// JPA AUDITING
@SpringBootApplication
public class DemoDockerFileApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoDockerFileApplication.class, args);
    }

}
