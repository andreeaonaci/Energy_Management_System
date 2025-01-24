package com.example.usermanagement;

import config.JwtAuthenticationEntryPoint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan({"controller","model", "repository", "service", "config"})
@EntityScan("model")
@EnableJpaRepositories("repository")
@SpringBootApplication(scanBasePackageClasses = JwtAuthenticationEntryPoint.class)
public class UserManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserManagementApplication.class, args);
    }

}
