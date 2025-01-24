package controllers;

import dto.DeviceMeasurementDTO;
import dto.DeviceMeasurementWithThresholdDTO;
import model.DeviceMeasurement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.MonitoringService;
import services.NotificationService;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/monitoring")
//@CrossOrigin(origins = "http://localhost:4200")
public class MonitoringController {

    @Autowired
    private MonitoringService monitoringService;

    private NotificationService notificationHandler;

    @GetMapping("/measurements")
    public void receiveMeasurement(@RequestBody DeviceMeasurement measurement) {
        monitoringService.processMeasurement(measurement);
    }

    @GetMapping("/{deviceId}/{date}")
    public ResponseEntity<List<DeviceMeasurement>> getDailyEnergyConsumptionDate(
            @PathVariable(name = "deviceId") Long deviceId,
            @PathVariable(name = "date") String date) {
        try {
            LocalDate parsedDate = LocalDate.parse(date);
            System.out.println("Parsed day: " + parsedDate);

            List<DeviceMeasurement> measurements = monitoringService.getEnergyDataForDay(
                    deviceId, parsedDate.toString());

            System.out.println(measurements);

            return ResponseEntity.ok(measurements);
        } catch (DateTimeParseException e) {
            // Handle invalid date format
            return ResponseEntity.badRequest().body(Collections.emptyList());
        } catch (Exception e) {
            // Handle any other errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    @GetMapping("/measurements/history")
    public List<DeviceMeasurement> getHistory(@RequestParam String deviceId, @RequestParam String date) {
        LocalDateTime startOfDay = LocalDate.parse(date).atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        return monitoringService.findMeasurement(deviceId, startOfDay, endOfDay);
    }

    @GetMapping("/energy/{deviceId}/{date}")
    public ResponseEntity<List<DeviceMeasurementDTO>> getEnergyDataByDeviceId(@PathVariable Long deviceId, @PathVariable(name = "date") String date) {
        LocalDate parsedDate = LocalDate.parse(date);
        System.out.println("Parsed day: " + parsedDate);
        List<DeviceMeasurementDTO> deviceMeasurementDTOs = monitoringService.getEnergyDataByDeviceId(deviceId, date);
        return ResponseEntity.ok(deviceMeasurementDTOs);
    }

//    @GetMapping("/daily/{deviceId}")
//    public List<DeviceMeasurement> getDailyEnergyConsumption(@PathVariable Long deviceId, @RequestParam String date) {
//        Timestamp startOfDay = Timestamp.valueOf(date + " 00:00:00");
//        Timestamp endOfDay = Timestamp.valueOf(date + " 23:59:59");
//
//        return monitoringService.getEnergyDataForDay(deviceId, startOfDay.toString());
//    }

    @GetMapping("/daily/{deviceId}")
    public ResponseEntity<List<DeviceMeasurement>> getDailyEnergyConsumption(@PathVariable Long deviceId, @RequestParam String date) {
        try {
            // Parse the provided date
            LocalDate parsedDate = LocalDate.parse(date);
            Timestamp startOfDay = Timestamp.valueOf(date + " 00:00:00");
            Timestamp endOfDay = Timestamp.valueOf(date + " 23:59:59");

            // Fetch the energy consumption data for the day
            List<DeviceMeasurement> measurements = monitoringService.getEnergyDataForDay(deviceId, startOfDay.toString());

            // Check the device threshold and send a notification if necessary
            double threshold = monitoringService.fetchThresholdForDevice(deviceId.toString()); // Fetch threshold for the device
            String message = "Daily energy consumption for device " + deviceId + " on " + date + " fetched. Threshold: " + threshold;
            notificationHandler.sendNotification(message); // Send WebSocket notification

            return ResponseEntity.ok(measurements);
        } catch (DateTimeParseException e) {
            // Handle invalid date format
            return ResponseEntity.badRequest().body(Collections.emptyList());
        } catch (Exception e) {
            // Handle any other errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

}
