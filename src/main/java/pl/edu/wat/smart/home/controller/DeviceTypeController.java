package pl.edu.wat.smart.home.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.wat.smart.home.entity.DeviceType;
import pl.edu.wat.smart.home.repository.DeviceTypeRepo;
import pl.edu.wat.smart.home.service.DeviceTypeService;

import java.util.List;

@RestController
public class DeviceTypeController {

    @Autowired
    DeviceTypeService deviceTypeService;

    @GetMapping("/device/types")
    public List<DeviceType> types() {
        return deviceTypeService.getAllTypes();
    }

}
