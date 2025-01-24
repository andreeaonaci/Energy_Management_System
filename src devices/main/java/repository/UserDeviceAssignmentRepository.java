package repository;

import model.UserDeviceAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDeviceAssignmentRepository extends JpaRepository<UserDeviceAssignment, Long> {
    List<UserDeviceAssignment> findByUserIdAndDeviceId(Long userId, Long deviceId);
    List<UserDeviceAssignment> findByUserId(Long userId);
}
