package com.example.energy_system_management;

import config.JwtAuthenticationEntryPoint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;

@ComponentScan({"controller","model", "repository", "service", "dto", "config"})
@EntityScan("model")
@EnableJpaRepositories("repository")
@SpringBootApplication(scanBasePackageClasses = JwtAuthenticationEntryPoint.class)
public class EnergySystemManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnergySystemManagementApplication.class, args);
    }
}
