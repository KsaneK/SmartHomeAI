package pl.edu.wat.smart.home.entity;

import lombok.Data;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "status_air_quality")
@Data
public class AirQualityStatus {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    Long id;

    String city;
    Double aqi;
    Date time;
}
