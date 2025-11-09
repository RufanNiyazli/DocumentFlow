package com.project.documentflow;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EntityScan("com.project")
@ComponentScan("com.project")
@EnableScheduling
@EnableIntegration
public class DocumentFlowApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();

        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
        SpringApplication.run(DocumentFlowApplication.class, args);
    }

}
