package pl.edu.wat.smart.home.dto;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DtoCapability {
    private Long id;
    private String name;
    private String component;
}