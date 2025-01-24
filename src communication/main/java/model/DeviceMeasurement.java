package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Entity
@Builder
@Table(name = "device_measurement")
public class DeviceMeasurement {
    public DeviceMeasurement() {
    }
    public DeviceMeasurement(Long id, String deviceId, Double measurementValue, String timestamp) {
        this.id = id;
        this.deviceId = deviceId;
        this.measurementValue = measurementValue;
        this.timestamp = timestamp;
    }
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Double getMeasurementValue() {
        return measurementValue;
    }

    public void setMeasurementValue(Double measurementValue) {
        this.measurementValue = measurementValue;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "deviceid")
    private String deviceId; // Match VARCHAR(255)
    @Column(name = "measurementvalue")
    private Double measurementValue; // Match DOUBLE
    @Column(name = "timestamp")
    private String timestamp; // Match DATETIME

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    // Getters and setters
}