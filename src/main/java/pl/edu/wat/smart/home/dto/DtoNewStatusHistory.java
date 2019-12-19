package pl.edu.wat.smart.home.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtoNewStatusHistory {
    private String username;
    private String device;
    private String capability;
    private Integer message;
}
