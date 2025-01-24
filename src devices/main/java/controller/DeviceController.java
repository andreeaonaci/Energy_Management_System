package controller;

import model.Device;
import model.UserDeviceAssignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import service.DeviceService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/devices")
//@CrossOrigin(origins = "http://localhost:4200")
public class DeviceController {
    @Autowired
    private DeviceService deviceService;

    private static final Logger logger = LoggerFactory.getLogger(DeviceController.class);


    @GetMapping("/all")
    public ResponseEntity<List<Device>> getAllDevices() {
        System.out.println("Getting all devices");
        List<Device> devices = deviceService.findAllDevices();
        devices.forEach(device ->
                logger.info("Device ID: {}, Address: {}, Description: {}, Name: {}",
                        device.getMaxhourlyenergyconsumption(), device.getAddress(), device.getDescription(), device.getName())

        );
        return new ResponseEntity<>(deviceService.findAllDevices(), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Device> createDevice(@RequestBody Device device) {
        System.out.println("Creating device");
        return new ResponseEntity<>(deviceService.saveDevice(device), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Device> updateDevice(@PathVariable Long id, @RequestBody Device device) {
        device.setId(id);
        return new ResponseEntity<>(deviceService.updateDevice(id, device), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long id) {
        deviceService.deleteDevice(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/devices")
    public ResponseEntity<List<UserDeviceAssignment>> getAllUserDeviceAssignments() {
        System.out.println("sunt aici");
        List<UserDeviceAssignment> userDeviceAssignments = deviceService.findAllUserDeviceAssignments();
        return ResponseEntity.ok(userDeviceAssignments);
    }

    @DeleteMapping("/api/devices/{userId}/{deviceId}")
    public ResponseEntity<Void> deleteUserDeviceAssignment(@PathVariable Long userId, @PathVariable Long deviceId) {
        UserDeviceAssignment assignment = deviceService.findEntityToDelete(userId, deviceId);
        deviceService.deleteUserDeviceAssignment(assignment.getId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/devices/create")
    public ResponseEntity<UserDeviceAssignment> createUserDeviceAssignment(@RequestBody UserDeviceAssignment userDeviceAssignment) {
        return new ResponseEntity<>(deviceService.saveUserDeviceAssignment(userDeviceAssignment), HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Device>> getAllDevicesByUser(@PathVariable Long userId) {
        System.out.println("Getting devices for user: " + userId);

        List<UserDeviceAssignment> userDeviceAssignments = deviceService.findUserDeviceAssignmentsByUserId(userId);
        List<Device> devicesList = new ArrayList<>();

        userDeviceAssignments.forEach(assignment ->
                devicesList.add(deviceService.getDeviceById(assignment.getDeviceId()))
        );

        devicesList.forEach(device ->
                System.out.println("Device ID: " + device.getId())
        );

        return ResponseEntity.ok(devicesList);
    }

    @GetMapping("/{deviceId}/threshold")
    public ResponseEntity<Double> getDeviceMeasurement(@PathVariable Long deviceId) {
        System.out.println(deviceId);
        Device device = deviceService.getDeviceById(deviceId);
        if (device == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(device.getMaxhourlyenergyconsumption());
    }
}
