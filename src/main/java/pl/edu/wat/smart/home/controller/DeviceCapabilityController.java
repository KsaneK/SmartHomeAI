package pl.edu.wat.smart.home.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.wat.smart.home.dto.DtoCapability;
import pl.edu.wat.smart.home.entity.Capability;
import pl.edu.wat.smart.home.service.DeviceCapabilityService;

import java.util.List;

@RestController
public class DeviceCapabilityController {

    @Autowired
    DeviceCapabilityService deviceCapabilityService;

    @GetMapping("/device/capabilities")
    public List<DtoCapability> capabilities() {
        return deviceCapabilityService.getAllCapabilities();
    }
}
