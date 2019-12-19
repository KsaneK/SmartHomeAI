package pl.edu.wat.smart.home.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.wat.smart.home.entity.Capability;
import pl.edu.wat.smart.home.entity.DeviceCapability;

@Repository
public interface CapabilityRepo extends JpaRepository<Capability, Long> {

}
