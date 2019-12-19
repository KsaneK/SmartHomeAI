package pl.edu.wat.smart.home.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.wat.smart.home.entity.DeviceType;
import pl.edu.wat.smart.home.repository.DeviceTypeRepo;

import java.util.List;

@Service
public class DeviceTypeService {

    @Autowired
    DeviceTypeRepo deviceTypeRepo;

    public List<DeviceType> getAllTypes(){
        return deviceTypeRepo.findAll();
    }


}
