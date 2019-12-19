package pl.edu.wat.smart.home.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.wat.smart.home.entity.DeviceCapability;
import pl.edu.wat.smart.home.entity.User;

import java.util.List;

@Repository
public interface DeviceCapabilityRepo extends JpaRepository<DeviceCapability, Long> {
    @Query("SELECT dc FROM DeviceCapability dc WHERE dc.name = :name AND dc.device.id = :deviceId")
    DeviceCapability findByNameAndDeviceId(@Param("name") String name, @Param("deviceId") Long deviceId);

    @Query("SELECT dc FROM DeviceCapability dc WHERE dc.device.id = :deviceId")
    List<DeviceCapability> findByDeviceId(@Param("deviceId") Long deviceId);

    @Query("SELECT dc FROM DeviceCapability dc WHERE dc.name = :capName AND dc.device.slug = :deviceSlug AND dc.device.owner = :user")
    DeviceCapability findByDeviceAndNameAndUser(String deviceSlug, String capName, User user);

//    DeviceCapability findByDeviceSlugAndName(String slug, String name);

    @Modifying
    @Transactional
    void deleteByDeviceId(Long id);
}
