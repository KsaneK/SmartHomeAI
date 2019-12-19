package pl.edu.wat.smart.home.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.wat.smart.home.dto.DtoRegisterUser;
import pl.edu.wat.smart.home.dto.DtoUserNotifications;
import pl.edu.wat.smart.home.dto.telegram.DtoUpdateEntry;
import pl.edu.wat.smart.home.dto.telegram.DtoUpdates;
import pl.edu.wat.smart.home.entity.User;
import pl.edu.wat.smart.home.exception.InvalidInputDtoException;
import pl.edu.wat.smart.home.repository.UserRepo;

import java.io.IOException;
import java.net.URL;
import java.security.Principal;
import java.util.Date;
import java.util.List;

@Service
public class UserService extends BaseService {

    @Autowired
    UserRepo repo;

    @Autowired
    PasswordEncoder passwordEncoder;

    public void registerUser(DtoRegisterUser dto) {
        if(usernameAlreadyExists(dto.getUsername()))
            throw new InvalidInputDtoException(String.format("Username '%s' already exists", dto.getUsername()));
        if(emailAlreadyExists(dto.getEmail()))
            throw new InvalidInputDtoException(String.format("Email '%s' already exists in database", dto.getEmail()));

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setDateJoined(new Date());

        repo.save(user);
    }

    public User getUser(Principal principal) {
        return repo.findByUsername(principal.getName());
    }

    public void updateTelegramToken(DtoUserNotifications dto) {
        User user = getCurrentlyLoggedUser();
        user.setTelegramToken(dto.getTelegramToken());
        repo.save(user);
    }

    public void updateTelegramNotification(DtoUserNotifications dto) {
        User user = getCurrentlyLoggedUser();
        user.setTelegramNotification(dto.isTelegramNotification());
        repo.save(user);
    }

    public void updateEmail(DtoUserNotifications dto) {
        User user = getCurrentlyLoggedUser();
        user.setEmail(dto.getEmail());
        repo.save(user);
    }

    public void updateEmailNotification(DtoUserNotifications dto) {
        User user = getCurrentlyLoggedUser();
        user.setEmailNotification(dto.isEmailNotification());
        repo.save(user);
    }

    private boolean emailAlreadyExists(String email) {
        return repo.findByEmail(email) != null;
    }

    private boolean usernameAlreadyExists(String username) {
        return repo.findByUsername(username) != null;
    }

    public boolean telegramConnect() throws IOException {
        User user = getCurrentlyLoggedUser();
        String token = user.getTelegramToken();
        URL url = new URL(String.format("https://api.telegram.org/bot%s/getUpdates", token));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        DtoUpdates updates = objectMapper.readValue(url, DtoUpdates.class);
        List<DtoUpdateEntry> entries = updates.getResult();
        for (DtoUpdateEntry entry : entries) {
            if (entry.getMessage().getText().equals("/smarthome")) {
                user.setTelegramId(entry.getMessage().getFrom().getId());
                repo.save(user);
                return true;
            }
        }
        return false;
    }
}
