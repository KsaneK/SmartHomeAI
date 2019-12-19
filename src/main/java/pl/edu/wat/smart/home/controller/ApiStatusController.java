package pl.edu.wat.smart.home.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.wat.smart.home.dto.DtoAirQuality;
import pl.edu.wat.smart.home.dto.DtoWeather;
import pl.edu.wat.smart.home.service.ApiStatusService;

import java.util.List;

@RestController
@RequestMapping("/status")
public class ApiStatusController {

    @Autowired
    ApiStatusService service;

    @GetMapping("/weather")
    public DtoWeather getWeather() {
        return service.getWeather();
    }

    @GetMapping("/weather/{amount}")
    public List<DtoWeather> getWeather(@PathVariable Integer amount) {
        return service.getWeather(amount);
    }

    @GetMapping("/air")
    public DtoAirQuality getAirQuality() {
        return service.getAirQuality();
    }

    @GetMapping("/air/{amount}")
    public List<DtoAirQuality> getAirQuality(@PathVariable Integer amount) {
        return service.getAirQuality(amount);
    }
}
