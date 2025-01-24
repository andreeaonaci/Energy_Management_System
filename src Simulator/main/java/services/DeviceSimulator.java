package services;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

public class DeviceSimulator {

    private static final String CONFIG_FILE = "G:/poli/an4/SEM1/DS/Communication_Microservice/src/main/resources/config.properties";
    private static final String EXCHANGE_NAME = "device_data_exchange";
    private static final String ROUTING_KEY = "device.measurements";
    private static final String RABBITMQ_URI = "amqps://jzweefzq:1SJKibuD1vQYYICex9SI9fsda8RaebSE@kangaroo.rmq.cloudamqp.com/jzweefzq";

    private int deviceId;

    public static void main(String[] args) {
        try {
            DeviceSimulator simulator = new DeviceSimulator();
            simulator.loadConfiguration();
            simulator.simulateDeviceData();
        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadConfiguration() throws IOException {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream(CONFIG_FILE)) {
            properties.load(input);
            deviceId = Integer.parseInt(properties.getProperty("device.id"));
            System.out.println("Loaded configuration: device.id = " + deviceId);
        } catch (IOException | NumberFormatException e) {
            throw new IOException("Failed to load or parse configuration file.", e);
        }
    }

    private void simulateDeviceData() throws IOException, TimeoutException, URISyntaxException, NoSuchAlgorithmException, KeyManagementException, InterruptedException {
        // Set up RabbitMQ connection
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(RABBITMQ_URI);

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(EXCHANGE_NAME, "direct", true);

            // Read the sensor CSV file
            try (BufferedReader reader = new BufferedReader(new FileReader("sensor.csv"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    try {
                        String energyValue = extractEnergyValue(line);
                        String timestamp = LocalDateTime.now().toString();

                        String message = "{" +
                                "\"timestamp\":\"" + timestamp + "\"," +
                                "\"measurement_value\":" + energyValue + ", " +
                                "\"device_id\":{" +
                                "\"id\":\"" + deviceId + "\"" +
                                "}}";

                        // Publish message to RabbitMQ
                        channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, null, message.getBytes());
                        System.out.println("Sent: " + message);

                        // Delay between messages (e.g., 60 seconds)
                        Thread.sleep(60000);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Skipping invalid line: " + line);
                    }
                }
            }
        }
    }

    private String extractEnergyValue(String line) {
        try {
            String[] parts = line.split(","); // Assuming CSV is comma-separated
            return parts[0].trim(); // Assuming the first column is the energy value
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid line format or energy value: " + line);
        }
    }
}
