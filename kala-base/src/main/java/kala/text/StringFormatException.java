package kala.text;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public class StringFormatException extends RuntimeException {
    public StringFormatException() {
    }

    public StringFormatException(String message) {
        super(message);
    }

    public StringFormatException(Throwable cause) {
        super(cause);
    }

    public StringFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
