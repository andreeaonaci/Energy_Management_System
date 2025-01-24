package repository;

import model.DeviceMeasurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DeviceMeasurementRepository extends JpaRepository<DeviceMeasurement, Long> {

    @Query("SELECT dm FROM DeviceMeasurement dm WHERE dm.deviceId = :deviceId AND dm.timestamp LIKE CONCAT(:start, '%')")
    List<DeviceMeasurement> findMeasurementsForDeviceInTimeRange(String deviceId, String start);

    List<DeviceMeasurement> findByDeviceId(String deviceId);
}
