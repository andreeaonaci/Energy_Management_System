package services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import dto.DeviceMeasurementDTO;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import model.DeviceMeasurement;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

@Getter
@Service
@EnableRabbit
public class DeviceDataConsumer {
    public DeviceDataConsumer(MonitoringService monitoringService) {
        this.monitoringService = monitoringService;
    }

    @PostConstruct
    public void init() {
        System.out.println("DeviceDataConsumer initialized and listening to the queue...");
    }


    @Autowired
    private MonitoringService monitoringService;

    @RabbitListener(queues = "${queue.name}")
    public void handleMessage() throws URISyntaxException, NoSuchAlgorithmException, KeyManagementException, IOException, TimeoutException {
        AtomicReference<String> message = new AtomicReference<>();
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqps://jzweefzq:1SJKibuD1vQYYICex9SI9fsda8RaebSE@kangaroo.rmq.cloudamqp.com/jzweefzq");

        // Keep the connection and channel open
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // Declare the queue (if not already declared)
//        channel.queueDeclare("device_measurements", true, false, false, null);

        System.out.println("Waiting for messages...");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            System.out.println("Callback invoked!");
            // Convert the byte array message to a string
            String rawMessage = new String(delivery.getBody(), StandardCharsets.UTF_8);
            message.set(rawMessage);
            System.out.println("Received message: " + rawMessage);

            // Deserialize the message to a DeviceMeasurementDTO
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                // Deserialize the message to DeviceMeasurementDTO
                DeviceMeasurementDTO deviceMeasurementDTO = objectMapper.readValue(rawMessage, DeviceMeasurementDTO.class);

                // The timestamp will be automatically converted to LocalDateTime
                System.out.println("Received Device ID: " + deviceMeasurementDTO.getDeviceId());
                System.out.println("Measurement Value: " + deviceMeasurementDTO.getMeasurementValue());
                System.out.println("Timestamp: " + deviceMeasurementDTO.getTimestamp());  // LocalDateTime

                DeviceMeasurement deviceMeasurement = DeviceMeasurement.builder()
                        .deviceId(deviceMeasurementDTO.getDeviceId().toString())
                        .measurementValue(deviceMeasurementDTO.getMeasurementValue())
                        .timestamp(deviceMeasurementDTO.getTimestamp())
                        .build();

                monitoringService.saveMeasurement(deviceMeasurement);

            } catch (IOException e) {
                System.err.println("Failed to deserialize message: " + e.getMessage());
            }
        };

        channel.basicConsume("device_measurements", true, deliverCallback, consumerTag -> {
            System.out.println("Consumer canceled: " + consumerTag);
        });

        // add the data to db


        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                channel.close();
                connection.close();
            } catch (Exception e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }));
    }

    private void processMessage(String message) {
        // Custom processing logic here
        System.out.println("Processing message: " + message);

        // Example: Save to database or trigger another service
        //monitoringService.saveDeviceData(message);
    }
}
