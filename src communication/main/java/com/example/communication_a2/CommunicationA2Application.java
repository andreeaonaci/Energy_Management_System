package com.example.communication_a2;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import config.JwtAuthenticationEntryPoint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import services.DeviceDataConsumer;
import services.MonitoringService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

@ComponentScan({"controllers","model", "repository", "services", "config"})
@EntityScan("model")
@EnableJpaRepositories("repository")
@SpringBootApplication(scanBasePackageClasses = JwtAuthenticationEntryPoint.class)
public class CommunicationA2Application {

    public static void main(String[] args) throws URISyntaxException, NoSuchAlgorithmException, KeyManagementException, IOException, TimeoutException {
//        AtomicReference<String> message = new AtomicReference<>();
//        ConnectionFactory factory = new ConnectionFactory();
//        factory.setUri("amqps://jzweefzq:1SJKibuD1vQYYICex9SI9fsda8RaebSE@kangaroo.rmq.cloudamqp.com/jzweefzq");
//
//        // Keep the connection and channel open
//        Connection connection = factory.newConnection();
//        Channel channel = connection.createChannel();
//
//        // Declare the queue (if not already declared)
//        channel.queueDeclare("device_measurements", true, false, false, null);
//
//        System.out.println("Waiting for messages...");
//
//        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
//            System.out.println("Callback invoked!");
//            message.set(new String(delivery.getBody(), StandardCharsets.UTF_8));
//            System.out.println("Received message: " + message);
//        };
//
//        channel.basicConsume("device_measurements", true, deliverCallback, consumerTag -> {
//            System.out.println("Consumer canceled: " + consumerTag);
//        });
//
//        // add the data to db
//
//
//        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//            try {
//                channel.close();
//                connection.close();
//            } catch (Exception e) {
//                System.err.println("Error closing connection: " + e.getMessage());
//            }
//        }));
        SpringApplication.run(CommunicationA2Application.class, args);
    }

}
