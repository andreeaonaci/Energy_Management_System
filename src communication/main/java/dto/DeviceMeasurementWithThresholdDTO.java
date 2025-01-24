package dto;

public class DeviceMeasurementWithThresholdDTO {
    private DeviceMeasurementDTO deviceMeasurementDTO;
    private boolean exceededThreshold;

    // Constructor
    public DeviceMeasurementWithThresholdDTO(DeviceMeasurementDTO deviceMeasurementDTO, boolean exceededThreshold) {
        this.deviceMeasurementDTO = deviceMeasurementDTO;
        this.exceededThreshold = exceededThreshold;
    }

    // Getters and Setters
    public DeviceMeasurementDTO getDeviceMeasurementDTO() {
        return deviceMeasurementDTO;
    }

    public void setDeviceMeasurementDTO(DeviceMeasurementDTO deviceMeasurementDTO) {
        this.deviceMeasurementDTO = deviceMeasurementDTO;
    }

    public boolean isExceededThreshold() {
        return exceededThreshold;
    }

    public void setExceededThreshold(boolean exceededThreshold) {
        this.exceededThreshold = exceededThreshold;
    }
}
