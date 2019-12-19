package pl.edu.wat.smart.home.controller;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.edu.wat.smart.home.dto.DtoMqttMessage;
import pl.edu.wat.smart.home.dto.DtoStatusHistory;
import pl.edu.wat.smart.home.service.ActionService;
import pl.edu.wat.smart.home.service.StatusHistoryService;

import java.util.List;

@RestController
@RequestMapping("/statushistory")
public class StatusHistoryController {

    @Autowired
    StatusHistoryService service;

    @Autowired
    ActionService actionService;

    @PostMapping("")
    public void create(@RequestBody DtoMqttMessage dto) {
//        service.create(dto);
        try {
            actionService.getMqttClient().publish(dto.getTopic(),
                    new MqttMessage(String.valueOf(dto.getValue()).getBytes()));
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/{devSlug}/{capName}")
    public List<DtoStatusHistory> getStatusHistoryForDeviceCapability(@PathVariable String devSlug, @PathVariable String capName) {
        return service.getStatusHistoryForDevice(devSlug, capName);
    }

    @GetMapping("/{owner}/{devSlug}/{capName}")
    public List<DtoStatusHistory> getStatusHistoryForDeviceCapabilityAndUser
            (@PathVariable String owner, @PathVariable String devSlug, @PathVariable String capName) {
        return service.getStatusHistoryForDevice(owner, devSlug, capName);
    }
}
