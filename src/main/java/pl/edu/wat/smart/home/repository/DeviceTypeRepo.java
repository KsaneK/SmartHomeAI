package pl.edu.wat.smart.home.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.wat.smart.home.entity.DeviceType;

import java.util.List;

@Repository
public interface DeviceTypeRepo extends JpaRepository<DeviceType, Long> {

}
