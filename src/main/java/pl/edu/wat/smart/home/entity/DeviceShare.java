package pl.edu.wat.smart.home.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "devices_share")
@Data
@ToString
public class DeviceShare {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    Long id;

    @OneToOne
    Device device;
    @OneToOne
    User sharedTo;
}
