package com.example.chatmicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan({"controller","entity", "repository", "services", "config", "component"})
@EntityScan("entity")
@EnableJpaRepositories("repository")
@SpringBootApplication
public class ChatMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatMicroserviceApplication.class, args);
    }

}
