package pl.edu.wat.smart.home.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.edu.wat.smart.home.dto.*;
import pl.edu.wat.smart.home.exception.InvalidInputDtoException;
import pl.edu.wat.smart.home.service.DeviceService;

import java.util.List;

@RestController
@RequestMapping("/device")
public class DeviceController {

    @Autowired
    DeviceService service;

    @PostMapping("/add")
    public DtoSlugWrapper create(@RequestBody DtoCreateDevice dto) {
        if(dto.getDevName() == null
                || dto.getDevType() == null
                || dto.getCapabilities() == null
                || dto.getCapabilities().size() < 1)
            throw new InvalidInputDtoException();

        return new DtoSlugWrapper(service.create(dto));
    }

    @PostMapping("/share")
    public Long share(@RequestBody DtoDeviceShareRequest dto) {
        return service.share(dto);
    }

    @GetMapping("/shared")
    public List<DtoDevice> getShared() {
        return service.getSharedDevices();
    }

    @GetMapping("/shared_to/{deviceId}")
    public List<DtoUser> getDeviceSharedTo(@PathVariable Long deviceId) {
        return service.getDeviceSharedTo(deviceId);
    }

    @DeleteMapping("/share/{deviceId}/{username}")
    public void deleteDeviceShare(@PathVariable Long deviceId, @PathVariable String username) {
        service.deleteDeviceShare(deviceId, username);
    }

    @DeleteMapping("/share/{deviceId}")
    public void deleteDeviceShare(@PathVariable Long deviceId) {
        service.deleteDeviceShare(deviceId);
    }

    @GetMapping("")
    public List<DtoDevice> getDevices() {
        return service.getAll();
    }

    @GetMapping("/{slug}")
    public DtoDevice getDevice(@PathVariable String slug) {
        return service.getDeviceBySlug(slug);
    }

    @GetMapping("/{username}/{slug}")
    public DtoDevice getSharedDevice(@PathVariable String username, @PathVariable String slug) {
        return service.getSharedDeviceBySlug(username, slug);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
