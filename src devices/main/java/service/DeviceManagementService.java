package service;

import model.Device;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Service
public class DeviceManagementService {

    private static final String DEVICE_SERVICE_URL = "http://localhost/devices";  // Replace with the actual URL of the Device Management Microservice
    private final RestTemplate restTemplate;

    public DeviceManagementService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Method to fetch all devices from the Device Management microservice
    public List<Device> getAllDevices() {
        String url = DEVICE_SERVICE_URL + "/all";  // Call the /all endpoint
        ResponseEntity<List<Device>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Device>>() {});
        return response.getBody();
    }

    // Method to create a device in the Device Management microservice
    public Device createDevice(Device device) {
        String url = DEVICE_SERVICE_URL + "/create";  // Call the /create endpoint
        ResponseEntity<Device> response = restTemplate.postForEntity(url, device, Device.class);
        return response.getBody();
    }

    // Method to fetch devices for a user
    public List<Device> getDevicesByUser(Long userId) {
        String url = DEVICE_SERVICE_URL + "/user/" + userId;  // Call the /user/{userId} endpoint
        ResponseEntity<List<Device>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Device>>() {});
        return response.getBody();
    }

    // Other methods to interact with device-related APIs from the Device Management microservice
}

