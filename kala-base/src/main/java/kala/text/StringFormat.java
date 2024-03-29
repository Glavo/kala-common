package kala.text;

import org.jetbrains.annotations.ApiStatus;

/**
 * Flexible and high-performance string interpolator.
 *
 * @see <a href="https://github.com/Glavo/kala-common/blob/main/docs/zh/StringFormat.md">Document (Chinese)</a>
 */
@ApiStatus.Experimental
public final class StringFormat {

    private final StringFormatFactory factory;
    private final String format;

    StringFormat(StringFormatFactory factory, String format) {
        this.factory = factory;
        this.format = format;
    }

    public static String format(String format, Object... arguments) {
        return StringFormatFactory.getDefault().format(format, arguments);
    }

    public StringFormatFactory getFactory() {
        return factory;
    }

    public String format(Object... arguments) {
        return format(new StringBuilder(), arguments).toString();
    }

    public StringBuilder format(StringBuilder out, Object... arguments) {
        return factory.format(out, format, arguments); // TODO: opt
    }
}
