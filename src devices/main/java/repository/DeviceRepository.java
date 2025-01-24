package repository;

import model.Device;
import model.UserDeviceAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    //List<Device> findAllByUser(User user);
}
