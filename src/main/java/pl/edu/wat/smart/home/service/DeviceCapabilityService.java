package pl.edu.wat.smart.home.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.edu.wat.smart.home.dto.DtoCapability;
import pl.edu.wat.smart.home.entity.Capability;
import pl.edu.wat.smart.home.entity.StatusHistory;
import pl.edu.wat.smart.home.repository.CapabilityRepo;
import pl.edu.wat.smart.home.repository.StatusHistoryRepo;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeviceCapabilityService {

    @Autowired
    CapabilityRepo capabilityRepo;

    public List<DtoCapability> getAllCapabilities(){
        List<Capability> capabilities = capabilityRepo.findAll();
        List<DtoCapability> dtoCapabilities = new ArrayList<>();
        capabilities.forEach(c -> dtoCapabilities.add(convertToDto(c)));
        return dtoCapabilities;
    }

    private DtoCapability convertToDto(Capability c) {
        return new DtoCapability(c.getId(), c.getName(), c.getComponent());
    }
}
