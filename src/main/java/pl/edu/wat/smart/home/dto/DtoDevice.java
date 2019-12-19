package pl.edu.wat.smart.home.dto;


import lombok.*;
import pl.edu.wat.smart.home.entity.DeviceType;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DtoDevice {
    private Long id;
    private DtoDevice_Capability mainCapability;
    private DeviceType type;
    private String slug;
    private String name;
    private String owner;
    private List<DtoDevice_Capability> capabilities;
}