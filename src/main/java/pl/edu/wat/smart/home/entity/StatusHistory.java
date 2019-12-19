package pl.edu.wat.smart.home.entity;

import lombok.Data;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "devices_statushistory")
@Data
public class StatusHistory {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    Long id;

    Integer value;
    Date date;

    @OneToOne
    DeviceCapability deviceCapability;

}
