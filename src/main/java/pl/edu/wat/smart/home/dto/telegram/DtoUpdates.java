package pl.edu.wat.smart.home.dto.telegram;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtoUpdates {
    boolean ok;
    List<DtoUpdateEntry> result;
}
