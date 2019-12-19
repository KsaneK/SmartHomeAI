package pl.edu.wat.smart.home.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DtoCreateAction {
    private String condition_device;
    private String condition_capability;
    private String condition;
    private Integer condition_value;
    private String action_device;
    private String action_capability;
    private Integer action_value;
}
