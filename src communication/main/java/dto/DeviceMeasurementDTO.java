package dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
//@AllArgsConstructor
public class DeviceMeasurementDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("device_id")
    private JsonNode deviceId;

    public DeviceMeasurementDTO(Long id, JsonNode deviceId, Double measurementValue, String timestamp) {
        this.id = id;
        this.deviceId = deviceId;
        this.measurementValue = measurementValue;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public JsonNode getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(JsonNode deviceId) {
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

    @JsonProperty("measurement_value")
    private Double measurementValue;

    @JsonProperty("timestamp")
    private String timestamp;

    // Constructor
//    @JsonCreator
    public DeviceMeasurementDTO() {
    }
}