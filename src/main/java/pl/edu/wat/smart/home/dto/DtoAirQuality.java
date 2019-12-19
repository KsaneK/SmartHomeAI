package pl.edu.wat.smart.home.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class DtoAirQuality {
    String city;
    Double aqi;
    String time;
}
