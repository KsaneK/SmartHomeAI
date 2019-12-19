package pl.edu.wat.smart.home.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtoDeviceShareRequest {
    Long deviceId;
    String username;
}
