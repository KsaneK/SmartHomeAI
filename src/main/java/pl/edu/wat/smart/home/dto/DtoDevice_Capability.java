package pl.edu.wat.smart.home.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DtoDevice_Capability {
    private String name;
    private String component;
    private String icon;
    private String label;
    private Integer last_value;
}
