package repository;

import dto.DeviceMeasurementDTO;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.SqlResultSetMapping;
import model.DeviceMeasurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DeviceMeasurementDTORepository extends JpaRepository<DeviceMeasurement, Long> {
    List<DeviceMeasurementDTO> findDeviceMeasurementsByDeviceId(@Param("deviceId") String deviceId);
}
