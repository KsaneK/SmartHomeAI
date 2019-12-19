package pl.edu.wat.smart.home.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DtoCreateDevice {
    private String devName;
    private Long devType;
    private Long mainCapability;
    private List<DtoCreateDevice_Capability> capabilities;
}
