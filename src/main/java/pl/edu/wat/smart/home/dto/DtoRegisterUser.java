package pl.edu.wat.smart.home.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DtoRegisterUser {
    private String username;
    private String password;
    private String matchingPassword;
    private String email;
}
