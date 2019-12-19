package pl.edu.wat.smart.home.dto.telegram;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtoEntryMessage {
    DtoMessageFrom from;
    String text;
}
