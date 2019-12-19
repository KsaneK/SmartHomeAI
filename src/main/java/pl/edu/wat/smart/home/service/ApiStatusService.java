package pl.edu.wat.smart.home.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.edu.wat.smart.home.dto.DtoAirQuality;
import pl.edu.wat.smart.home.dto.DtoWeather;
import pl.edu.wat.smart.home.entity.AirQualityStatus;
import pl.edu.wat.smart.home.entity.WeatherStatus;
import pl.edu.wat.smart.home.repository.AirQualityStatusRepo;
import pl.edu.wat.smart.home.repository.WeatherStatusRepo;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApiStatusService {

    @Autowired
    AirQualityStatusRepo airQualityStatusRepo;

    @Autowired
    WeatherStatusRepo weatherStatusRepo;

    public DtoWeather getWeather() {
        List<WeatherStatus> statuses = weatherStatusRepo.getLast(timePage(1));

        if(statuses.size() == 0)
            return null;

        WeatherStatus status = statuses.get(0);
        return convertToDto(status);
    }

    public List<DtoWeather> getWeather(Integer amount) {
        List<WeatherStatus> statuses = weatherStatusRepo.getLast(timePage(amount));
        return statuses.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private DtoWeather convertToDto(WeatherStatus status) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");

        return DtoWeather.builder()
                .description(status.getDescription())
                .city(status.getCity())
                .temp(status.getTemp())
                .windSpeed(status.getWindSpeed())
                .time(sdf.format(status.getTime()))
                .build();
    }

    public DtoAirQuality getAirQuality() {
        List<AirQualityStatus> statuses = airQualityStatusRepo.getLast(timePage(1));

        if(statuses.size() == 0)
            return null;

        AirQualityStatus status = statuses.get(0);
        return convertToDto(status);
    }


    public List<DtoAirQuality> getAirQuality(Integer amount) {
        List<AirQualityStatus> statuses = airQualityStatusRepo.getLast(timePage(amount));
        return statuses.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private DtoAirQuality convertToDto(AirQualityStatus status) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");

        return DtoAirQuality.builder()
                .city(status.getCity())
                .aqi(status.getAqi())
                .time(sdf.format(status.getTime()))
                .build();
    }

    private Pageable timePage(int max) {
        return PageRequest.of(0, max, Sort.by(Sort.Order.desc("time")));
    }
}
