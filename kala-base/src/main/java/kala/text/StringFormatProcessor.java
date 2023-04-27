package kala.text;

import kala.annotations.UnstableName;
import org.jetbrains.annotations.ApiStatus;

@FunctionalInterface
@ApiStatus.Experimental
public interface StringFormatProcessor<T> {
    StringFormatProcessor<Object> DEFAULT =  (factory, out, value, style) -> {
        if (!style.isEmpty()) {
            throw new StringFormatException("Unknown style: " + style);
        }
        out.append(value);
    };
    StringFormatProcessor<Object> ARRAY = (factory, out, value, style) -> {
        if (!style.isEmpty()) {
            throw new StringFormatException("Unknown style: " + style);
        }
        new StringAppender(out).append(value);
    };

    @UnstableName
    default boolean acceptNull() {
        return true;
    }

    void accept(StringFormatFactory factory, StringBuilder out, T value, String style) throws StringFormatException;
}
