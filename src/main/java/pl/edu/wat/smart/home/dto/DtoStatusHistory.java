package pl.edu.wat.smart.home.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Builder
@Getter
public class DtoStatusHistory {
    private Date date;
    private Integer value;
}
