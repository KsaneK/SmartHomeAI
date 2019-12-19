package pl.edu.wat.smart.home.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DtoCreateDevice_Capability {
    private String capabilityName;
    private Long capabilityType;
    private String capabilityIcon;
}
