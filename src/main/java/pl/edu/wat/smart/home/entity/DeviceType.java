package pl.edu.wat.smart.home.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "devices_devicetype")
@Data
public class DeviceType {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    Long id;

    String name;
    String icon;

}
