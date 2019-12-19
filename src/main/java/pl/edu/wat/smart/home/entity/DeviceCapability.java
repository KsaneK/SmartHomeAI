package pl.edu.wat.smart.home.entity;

import lombok.Data;
import javax.persistence.*;

@Entity
@Table(name = "devices_device_capability")
@Data
public class DeviceCapability {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    Long id;

    String name;
    String label;
    String icon;

    @OneToOne
    Capability capability;

    @ManyToOne
    Device device;

}
