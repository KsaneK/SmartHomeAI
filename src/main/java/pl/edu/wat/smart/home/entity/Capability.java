package pl.edu.wat.smart.home.entity;

import lombok.Data;
import javax.persistence.*;

@Entity
@Table(name = "devices_capability")
@Data
public class Capability {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    Long id;

    String component;
    String name;
}
