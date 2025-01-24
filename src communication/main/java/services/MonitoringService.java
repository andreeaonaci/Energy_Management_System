package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.DeviceMeasurementDTO;
import dto.DeviceMeasurementWithThresholdDTO;
import jakarta.annotation.PostConstruct;
import model.DeviceMeasurement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import repository.DeviceMeasurementRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MonitoringService {

    @Autowired
    private DeviceMeasurementRepository measurementRepository;

    private RestTemplate restTemplate;

    private NotificationService notificationHandler;

//    public void processMeasurement(DeviceMeasurement measurement) {
//        // Save the measurement in the database
//        measurementRepository.save(measurement);
//    }

    @PostConstruct
    public void init() {
        if (this.restTemplate == null) {
            this.restTemplate = new RestTemplate();
        }
    }
    public void saveMeasurement(DeviceMeasurement measurement) {
        System.out.println("sunt aici");
        String deviceId = measurement.getDeviceId().split(":\"")[1].replace("}", "").replace("\"", "");
        System.out.println(deviceId + " " + measurement.getMeasurementValue() + " " + measurement.getTimestamp());
        measurement.setDeviceId(deviceId);
        measurementRepository.save(measurement);
    }

    public List<DeviceMeasurement> findMeasurement(String deviceId, LocalDateTime startTime, LocalDateTime endTime) {
        return measurementRepository.findMeasurementsForDeviceInTimeRange(deviceId, startTime.toString());
    }

    public List<DeviceMeasurement> getEnergyDataForDay(Long deviceId, String start) {
        return measurementRepository.findMeasurementsForDeviceInTimeRange(deviceId.toString(), start);
    }

//    public List<DeviceMeasurementDTO> getEnergyDataByDeviceId(Long deviceId, String date) {
//        List<DeviceMeasurement> measurements = measurementRepository.findMeasurementsForDeviceInTimeRange(deviceId.toString(), date);
//        return measurements.stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }

    public List<DeviceMeasurementDTO> getEnergyDataByDeviceId(Long deviceId, String date) {
        notificationHandler = new NotificationService();
        double threshold = fetchThresholdForDevice(String.valueOf(deviceId));

        List<DeviceMeasurement> measurements = measurementRepository.findMeasurementsForDeviceInTimeRange(deviceId.toString(), date);

        boolean exceededThreshold = false;

        for (DeviceMeasurement measurement : measurements) {
            if (measurement.getMeasurementValue() > threshold) {
                String notification = String.format("Device %d has exceeded the threshold. Measurement value: %.2f",
                        deviceId, measurement.getMeasurementValue());
                exceededThreshold = true;
                notificationHandler.sendNotification(notification);
                break;
            }
        }

        boolean finalExceededThreshold = exceededThreshold;
        return measurements.stream()
                .map(measurement -> convertToDTO(measurement, finalExceededThreshold))
                .collect(Collectors.toList());
    }

//    public List<DeviceMeasurementWithThresholdDTO> getEnergyDataByDeviceId(Long deviceId, String date) {
//        notificationHandler = new NotificationService();
//        double threshold = fetchThresholdForDevice(String.valueOf(deviceId));
//
//        List<DeviceMeasurement> measurements = measurementRepository.findMeasurementsForDeviceInTimeRange(deviceId.toString(), date);
//
//        boolean exceededThreshold = false;
//
//        for (DeviceMeasurement measurement : measurements) {
//            if (measurement.getMeasurementValue() > threshold) {
//                String notification = String.format("Device %d has exceeded the threshold. Measurement value: %.2f",
//                        deviceId, measurement.getMeasurementValue());
//                exceededThreshold = true;
//                break;
//                //notificationHandler.sendNotification(notification);
//            }
//        }
//
//        // Wrap each DeviceMeasurementDTO with exceededThreshold status
//        boolean finalExceededThreshold = exceededThreshold;
//        return measurements.stream()
//                .map(measurement -> new DeviceMeasurementWithThresholdDTO(
//                        convertToDTO(measurement, finalExceededThreshold),
//                        finalExceededThreshold))
//                .collect(Collectors.toList());
//    }

    private DeviceMeasurementDTO convertToDTO(DeviceMeasurement measurement, boolean exceededThreshold) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode deviceIdNode = null;

        if (measurement.getDeviceId() != null) {
            try {
                deviceIdNode = objectMapper.readTree(measurement.getDeviceId());
            } catch (Exception e) {
                throw new RuntimeException("Failed to convert deviceId to JsonNode", e);
            }
        }

        return DeviceMeasurementDTO.builder()
                .deviceId(deviceIdNode)
                .measurementValue(measurement.getMeasurementValue())
                .timestamp(measurement.getTimestamp())
                .build();
    }
    @Value("${device.service.url}")
    private String deviceServiceUrl;
    public void processMeasurement(DeviceMeasurement measurement) {
        double threshold = fetchThresholdForDevice(measurement.getDeviceId());

        if (measurement.getMeasurementValue() > threshold) {
            try {
                String notification = "Energy limit exceeded for device: " + measurement.getDeviceId() +
                        " with value: " + measurement.getMeasurementValue();
                notificationHandler.sendNotification(notification);
            } catch (Exception e) {
                System.err.println("Failed to send WebSocket notification: " + e.getMessage());
            }
        }
    }

    public double fetchThresholdForDevice(String deviceId) {
        String url = deviceServiceUrl + "/devices/" + deviceId + "/threshold";
        System.out.println(url);
        try {
            // Change to directly fetch a double, assuming the response is just the value
            ResponseEntity<Double> response = restTemplate.getForEntity(url, Double.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                // Return the double value directly from the response body
                return response.getBody();
            } else {
                throw new RuntimeException("Failed to fetch device details: " + response.getStatusCode());
            }
        } catch (HttpServerErrorException ex) {
            // Log the server-side error details
            System.err.println("Server-side error occurred: " + ex.getMessage());
            System.err.println("Response body: " + ex.getResponseBodyAsString());
            System.err.println("Response status code: " + ex.getStatusCode());
            return 0;  // Fallback value
        } catch (Exception e) {
            // Log the client-side error and rethrow it
            System.err.println("Error fetching device details for deviceId: " + deviceId);
            e.printStackTrace();
            return 0;  // Fallback value
        }
    }


}
