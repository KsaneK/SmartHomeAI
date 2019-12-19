package pl.edu.wat.smart.home.entity;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "auth_user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    Long id;

    String username;
    String password;
    String email;
    String telegramToken;
    int telegramId;
    boolean emailNotification;
    boolean telegramNotification;
    Boolean isSuperUser;
    Date dateJoined;
    Date lastLogin;
}
