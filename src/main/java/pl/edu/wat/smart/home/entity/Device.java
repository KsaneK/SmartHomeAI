package pl.edu.wat.smart.home.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "devices_device")
@Data
@ToString(exclude = "mainCapability")
public class Device {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    Long id;

    String name;
    String slug;

    @OneToOne
    User owner;

    @OneToOne
    DeviceType deviceType;

    @OneToOne
    DeviceCapability mainCapability;

}