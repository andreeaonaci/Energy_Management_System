package service;

import dto.DeviceDto;
import model.Device;
import model.UserDeviceAssignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import repository.DeviceRepository;
import repository.UserDeviceAssignmentRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DeviceService {
    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserDeviceAssignmentRepository userDeviceAssignmentRepository;

    private static final String USER_SERVICE_URL = "http://localhost:8080/api/users";

    public Device saveDevice(Device device) {
        System.out.println("am primit " + device.getName() + " " + device.getDescription() + " " + device.getAddress() + " " + device.getMaxhourlyenergyconsumption());
        return deviceRepository.save(device);
    }

    public Device updateDevice(Long id, Device device) {
        if (deviceRepository.existsById(id)) {
            device.setId(id);
            device.setAddress(device.getAddress());
            device.setDescription(device.getDescription());
            device.setMaxhourlyenergyconsumption(device.getMaxhourlyenergyconsumption());
            return deviceRepository.save(device);
        } else {
            throw new RuntimeException("Device not found");
        }
    }

    public void deleteDevice(Long id) {
        deviceRepository.deleteById(id);
    }

    public List<Device> findAllDevices() {
        for(Device device : deviceRepository.findAll()) {
            System.out.println(device.getName());
        }
        return deviceRepository.findAll();
    }

    public Device createDeviceDTO(UserDeviceAssignment deviceDto) {
        String userCheckUrl = USER_SERVICE_URL + "/" + deviceDto.getUserId();
        Boolean userExists = restTemplate.getForObject(userCheckUrl, Boolean.class);

        if (userExists == null || !userExists) {
            throw new RuntimeException("User not found");
        }

        Device device = new Device();
        device.setDescription(deviceRepository.findById(deviceDto.getDeviceId()).orElseThrow(() -> new RuntimeException("Device not found")).getDescription());
        device.setAddress(deviceRepository.findById(deviceDto.getDeviceId()).orElseThrow(() -> new RuntimeException("Device not found")).getAddress());
        device.setMaxhourlyenergyconsumption(deviceRepository.findById(deviceDto.getDeviceId()).orElseThrow(() -> new RuntimeException("Device not found")).getMaxhourlyenergyconsumption());
        Device savedDevice = device;

        UserDeviceAssignment assignment = new UserDeviceAssignment();
        assignment.setDeviceId(savedDevice.getId());
        assignment.setUserId(deviceDto.getUserId());
        userDeviceAssignmentRepository.save(assignment);

        return savedDevice;
    }

    public Device updateDeviceDTO(Long id, DeviceDto deviceDto) {
        Device device = deviceRepository.findById(id).orElseThrow(() -> new RuntimeException("Device not found"));
        device.setDescription(deviceDto.getDescription());
        device.setAddress(deviceDto.getAddress());
        device.setMaxhourlyenergyconsumption(deviceDto.getMaxHourlyEnergyConsumption());
        return deviceRepository.save(device);
    }

    public void deleteDeviceDTO(Long id) {
        userDeviceAssignmentRepository.deleteById(id);
        deviceRepository.deleteById(id);
    }
    public List<Device> getDevicesByUserId(Integer userId) {
        List<UserDeviceAssignment> assignments = userDeviceAssignmentRepository.findAll();
        return assignments.stream()
                .filter(assignment -> assignment.getUserId().equals(userId))
                .map(assignment -> deviceRepository.findById(assignment.getDeviceId()).orElse(null))
                .collect(Collectors.toList());
    }

    public List<UserDeviceAssignment> findAllUserDeviceAssignments() {
        return userDeviceAssignmentRepository.findAll();
    }

    public UserDeviceAssignment saveUserDeviceAssignment(UserDeviceAssignment userDeviceAssignment) {
        return userDeviceAssignmentRepository.save(userDeviceAssignment);
    }

    public UserDeviceAssignment updateUserDeviceAssignment(Long id, UserDeviceAssignment userDeviceAssignment) {
        if (userDeviceAssignmentRepository.existsById(id)) {
            userDeviceAssignment.setId(id);
            userDeviceAssignment.setDeviceId(userDeviceAssignment.getDeviceId());
            userDeviceAssignment.setUserId(userDeviceAssignment.getUserId());
            return userDeviceAssignmentRepository.save(userDeviceAssignment);
        } else {
            throw new RuntimeException("UserDeviceAssignment not found");
        }
    }

    public UserDeviceAssignment findEntityToDelete(Long userId, Long deviceId) {
        try {
            return userDeviceAssignmentRepository.findByUserIdAndDeviceId(userId, deviceId).get(0);
        } catch (Exception e) {
            throw new RuntimeException("UserDeviceAssignment not found");
        }
    }

    public void deleteUserDeviceAssignment(Long id) {
        userDeviceAssignmentRepository.deleteById(id);
    }

    //find all devices by user id
    public List<UserDeviceAssignment> findUserDeviceAssignmentsByUserId(Long userId) {
        System.out.println("User ID received: " + userId);
        return userDeviceAssignmentRepository.findByUserId(userId);
    }

    public Device getDeviceById(Long deviceId) {
        return deviceRepository.findById(deviceId)
                .orElseThrow(() -> new RuntimeException("Device not found with ID: " + deviceId));
    }

    public Optional<Device> getDeviceMeasurements(String deviceId) {
        return deviceRepository.findById(Long.valueOf(deviceId));
    }

}
