package pl.edu.wat.smart.home.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DtoAction {
    private Long id;
    private String condition_device;
    private String action_device;
    private DtoDevice_Capability condition_capability;
    private Integer condition_value;
    private String condition;
    private DtoDevice_Capability action_capability;
    private Integer action_capability_value;
    private boolean notify;
}
