package kala.text;

import java.io.Serializable;

@FunctionalInterface
public interface StringFormatProcessor<T> {

    StringFormatProcessor<Object> ARRAY = (StringFormatProcessor<Object> & Serializable) (factory, out, value, style) -> new StringAppender(out).append(value);

    default boolean acceptNull() {
        return true;
    }

    void accept(StringFormatFactory factory, StringBuilder out, T value, String style) throws StringFormatException;
}
