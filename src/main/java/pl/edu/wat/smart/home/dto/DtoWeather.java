package pl.edu.wat.smart.home.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class DtoWeather {
    String description;
    String city;
    Double windSpeed;
    Double temp;
    String time;
}
