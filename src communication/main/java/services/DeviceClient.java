package services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class DeviceClient {

    @Value("${device.service.url}")
    private String deviceServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> getDeviceById(Long deviceId) {
        String url = deviceServiceUrl + "/devices/" + deviceId;
        return restTemplate.getForObject(url, Map.class);
    }
}
