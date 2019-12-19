package pl.edu.wat.smart.home.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DtoUserNotifications {
    String email;
    boolean emailNotification;
    String telegramToken;
    boolean telegramNotification;
}
