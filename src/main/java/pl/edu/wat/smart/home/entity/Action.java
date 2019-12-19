package pl.edu.wat.smart.home.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "actions_action")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Action {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Column
    Long id;

    @OneToOne
    DeviceCapability conditionCapability;

    String cond;
    Integer conditionValue;

    @OneToOne
    DeviceCapability actionCapability;

    Integer actionValue;

    @Setter
    boolean notify;
}