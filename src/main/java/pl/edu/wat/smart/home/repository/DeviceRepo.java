package pl.edu.wat.smart.home.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.wat.smart.home.entity.Device;
import pl.edu.wat.smart.home.entity.User;

import java.util.List;

@Repository
public interface DeviceRepo extends JpaRepository<Device, Long> {

    long countByNameAndOwner(String name, User user);
    Device findByIdAndOwner(Long id, User owner);

    Device findBySlugAndOwnerId(String slug, Long ownerId);

    List<Device> findAllByOwnerId(Long ownerId);
}
