package kala.text;

import kala.annotations.UnstableName;
import org.jetbrains.annotations.ApiStatus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@FunctionalInterface
@ApiStatus.Experimental
public interface StringFormatProcessor {
    StringFormatProcessor DEFAULT = (factory, out, value, style) -> {
        if (!style.isEmpty()) {
            throw new StringFormatException("Unknown style: " + style);
        }
        out.append(value);
    };

    StringFormatProcessor ARRAY = (factory, out, value, style) -> {
        if (!style.isEmpty()) {
            throw new StringFormatException("Unknown style: " + style);
        }
        new StringAppender(out).append(value);
    };

    StringFormatProcessor DATE = (factory, out, value, style) -> {
        if (!(value instanceof Date)) {
            throw new StringFormatException(value + " is not date");
        }

        DateFormat format;

        switch (style) {
            case "":
            case "medium":
                format = DateFormat.getDateInstance(DateFormat.DEFAULT, factory.getLocale());
                break;
            case "short":
                format = DateFormat.getDateInstance(DateFormat.SHORT, factory.getLocale());
                break;
            case "long":
                format = DateFormat.getDateInstance(DateFormat.LONG, factory.getLocale());
                break;
            case "full":
                format = DateFormat.getDateInstance(DateFormat.FULL, factory.getLocale());
                break;
            default:
                format = new SimpleDateFormat(style, factory.getLocale());
                break;
        }

        out.append(format.format(value));
    };

    @UnstableName
    default boolean acceptNull() {
        return true;
    }

    void accept(StringFormatFactory factory, StringBuilder out, Object value, String style) throws StringFormatException;
}
