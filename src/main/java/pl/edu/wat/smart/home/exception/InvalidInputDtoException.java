package pl.edu.wat.smart.home.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class InvalidInputDtoException extends RuntimeException {
    public InvalidInputDtoException() {
        super("Invalid input object");
    }

    public InvalidInputDtoException(String message) {
        super(message);
    }
}
