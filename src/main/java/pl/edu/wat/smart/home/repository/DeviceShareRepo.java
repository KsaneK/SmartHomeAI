package pl.edu.wat.smart.home.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.wat.smart.home.entity.Device;
import pl.edu.wat.smart.home.entity.DeviceShare;
import pl.edu.wat.smart.home.entity.User;

import java.util.List;

public interface DeviceShareRepo extends JpaRepository<DeviceShare, Long> {
    List<DeviceShare> findAllBySharedTo(User user);
    List<DeviceShare> findAllByDevice(Device device);
    DeviceShare findByDeviceAndSharedTo(Device device, User user);
    @Transactional
    void deleteByDevice_IdAndSharedTo(Long deviceId, User user);
    @Transactional
    void deleteByDevice_OwnerAndDevice_IdAndSharedTo_Username(User user, Long deviceId, String username);
}
