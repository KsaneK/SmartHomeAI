package pl.edu.wat.smart.home.entity;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "status_weather")
@Data
public class WeatherStatus {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    Long id;

    String description;
    String city;
    Double windSpeed;
    Double temp;
    Date time;
}