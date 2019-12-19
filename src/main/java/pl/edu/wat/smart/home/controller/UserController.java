package pl.edu.wat.smart.home.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.wat.smart.home.dto.DtoRegisterUser;
import pl.edu.wat.smart.home.dto.DtoUserNotifications;
import pl.edu.wat.smart.home.entity.User;
import pl.edu.wat.smart.home.service.UserService;

import java.io.IOException;
import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/account")
public class UserController {
    @Autowired
    UserService service;


    @PostMapping("/create")
    public void register(@RequestBody DtoRegisterUser dto) {
        service.registerUser(dto);
    }

    @GetMapping("/notification_config")
    public DtoUserNotifications getNotificationConfig(Principal principal) {
        User user = service.getUser(principal);
        return new DtoUserNotifications(user.getEmail(), user.isEmailNotification(),
                user.getTelegramToken(), user.isTelegramNotification());
    }

    @PutMapping("/email")
    public void updateEmail(@RequestBody DtoUserNotifications dto) {
        service.updateEmail(dto);
    }

    @PutMapping("/email/notification")
    public void updateEmailNotification(@RequestBody DtoUserNotifications dto) {
        service.updateEmailNotification(dto);
    }

    @PutMapping("/telegram_token")
    public void updateTelegramToken(@RequestBody DtoUserNotifications dto) {
        service.updateTelegramToken(dto);
    }

    @PutMapping("/telegram/notification")
    public void updateTelegramNotification(@RequestBody DtoUserNotifications dto) {
        service.updateTelegramNotification(dto);
    }

    @GetMapping("/telegram_connect")
    public ResponseEntity telegramConnect() {
        try {
            if (service.telegramConnect())
                return new ResponseEntity(HttpStatus.OK);
            else
                return new ResponseEntity(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/authenticated")
    public boolean isAuthenticated(Principal principal) {
        return principal != null;
    }
}
