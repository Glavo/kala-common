package kala.text;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.Formatter;
import java.util.Locale;
import java.util.Objects;

public class StringAppender extends Writer implements Serializable {
    private static final long serialVersionUID = 0L;

    private final StringBuilder builder;

    public StringAppender() {
        this.builder = new StringBuilder();
    }

    public StringAppender(int capacity) {
        this.builder = new StringBuilder(capacity);
    }

    public StringAppender(@NotNull String value) {
        this.builder = new StringBuilder(value);
    }

    public StringAppender(@NotNull CharSequence value) {
        this.builder = new StringBuilder(value);
    }

    public StringAppender(@NotNull StringBuilder builder) {
        this.builder = Objects.requireNonNull(builder);
    }

    public @NotNull StringBuilder getBuilder() {
        return builder;
    }

    //region Writer

    @Override
    public void write(int c) {
        builder.append((char) c);
    }

    @Override
    public void write(@NotNull String str) {
        builder.append(str);
    }

    @Override
    public void write(@NotNull String str, int off, int len) {
        builder.append(str, off, off + len);
    }

    @Override
    public void write(char @NotNull [] str, int offset, int len) {
        builder.append(str, offset, len);
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() throws IOException {
    }

    //endregion

    public void appendTo(Appendable appendable) throws IOException {
        appendable.append(builder);
    }

    public StringAppender append(char[] str) {
        builder.append(str);
        return this;
    }

    public StringAppender append(char[] str, int offset, int len) {
        builder.append(str, offset, len);
        return this;
    }

    public StringAppender append(boolean b) {
        builder.append(b);
        return this;
    }

    @Override
    public StringAppender append(char c) {
        builder.append(c);
        return this;
    }

    public StringAppender append(int i) {
        builder.append(i);
        return this;
    }

    public StringAppender append(long lng) {
        builder.append(lng);
        return this;
    }

    public StringAppender append(float f) {
        builder.append(f);
        return this;
    }

    public StringAppender append(double d) {
        builder.append(d);
        return this;
    }

    public StringAppender append(Object obj) {
        if (obj == null) {
            builder.append((String) null);
        } else if (obj.getClass().isArray()) {
            if (obj instanceof Object[]) {
                appendArray((Object[]) obj);
            } else if (obj instanceof char[]) {
                appendArray((char[]) obj);
            } else if (obj instanceof boolean[]) {
                appendArray((boolean[]) obj);
            } else if (obj instanceof byte[]) {
                appendArray((byte[]) obj);
            } else if (obj instanceof short[]) {
                appendArray((short[]) obj);
            } else if (obj instanceof int[]) {
                appendArray((int[]) obj);
            } else if (obj instanceof long[]) {
                appendArray((long[]) obj);
            } else if (obj instanceof float[]) {
                appendArray((float[]) obj);
            } else if (obj instanceof double[]) {
                appendArray((double[]) obj);
            } else {
                throw new AssertionError("Unknown array type: " + obj.getClass());
            }
        } else {
            builder.append(obj);
        }

        return this;
    }

    public StringAppender appendNull() {
        builder.append((String) null);
        return this;
    }

    public StringAppender appendArray(Object[] array) {
        if (array == null) {
            appendNull();
            return this;
        }

        if (array.length == 0) {
            builder.append("[]");
            return this;
        }

        builder.append('[');
        append(array[0]);

        for (int i = 1; i < array.length; i++) {
            builder.append(", ");
            append(array[i]);
        }

        builder.append(']');
        return this;
    }

    public StringAppender appendArray(char[] array) {
        if (array == null) {
            appendNull();
            return this;
        }

        if (array.length == 0) {
            builder.append("[]");
            return this;
        }

        builder.append('[');
        append(array[0]);

        for (int i = 1; i < array.length; i++) {
            builder.append(", ");
            append(array[i]);
        }

        builder.append(']');
        return this;
    }

    public StringAppender appendArray(boolean[] array) {
        if (array == null) {
            appendNull();
            return this;
        }

        if (array.length == 0) {
            builder.append("[]");
            return this;
        }

        builder.append('[');
        append(array[0]);

        for (int i = 1; i < array.length; i++) {
            builder.append(", ");
            append(array[i]);
        }

        builder.append(']');
        return this;
    }

    public StringAppender appendArray(byte[] array) {
        if (array == null) {
            appendNull();
            return this;
        }

        if (array.length == 0) {
            builder.append("[]");
            return this;
        }

        builder.append('[');
        append(array[0]);

        for (int i = 1; i < array.length; i++) {
            builder.append(", ");
            append(array[i]);
        }

        builder.append(']');
        return this;
    }

    public StringAppender appendArray(short[] array) {
        if (array == null) {
            appendNull();
            return this;
        }

        if (array.length == 0) {
            builder.append("[]");
            return this;
        }

        builder.append('[');
        append(array[0]);

        for (int i = 1; i < array.length; i++) {
            builder.append(", ");
            append(array[i]);
        }

        builder.append(']');
        return this;
    }

    public StringAppender appendArray(int[] array) {
        if (array == null) {
            appendNull();
            return this;
        }

        if (array.length == 0) {
            builder.append("[]");
            return this;
        }

        builder.append('[');
        append(array[0]);

        for (int i = 1; i < array.length; i++) {
            builder.append(", ");
            append(array[i]);
        }

        builder.append(']');
        return this;
    }

    public StringAppender appendArray(long[] array) {
        if (array == null) {
            appendNull();
            return this;
        }

        if (array.length == 0) {
            builder.append("[]");
            return this;
        }

        builder.append('[');
        append(array[0]);

        for (int i = 1; i < array.length; i++) {
            builder.append(", ");
            append(array[i]);
        }

        builder.append(']');
        return this;
    }

    public StringAppender appendArray(float[] array) {
        if (array == null) {
            appendNull();
            return this;
        }

        if (array.length == 0) {
            builder.append("[]");
            return this;
        }

        builder.append('[');
        append(array[0]);

        for (int i = 1; i < array.length; i++) {
            builder.append(", ");
            append(array[i]);
        }

        builder.append(']');
        return this;
    }

    public StringAppender appendArray(double[] array) {
        if (array == null) {
            appendNull();
            return this;
        }

        if (array.length == 0) {
            builder.append("[]");
            return this;
        }

        builder.append('[');
        append(array[0]);

        for (int i = 1; i < array.length; i++) {
            builder.append(", ");
            append(array[i]);
        }

        builder.append(']');
        return this;
    }

    public StringAppender appendCodePoint(int codePoint) {
        builder.appendCodePoint(codePoint);
        return this;
    }

    public StringAppender append(String str) {
        builder.append(str);
        return this;
    }

    public StringAppender append(StringBuffer buffer) {
        builder.append(buffer);
        return this;
    }

    public StringAppender append(StringView view) {
        if (view == null)
            appendNull();
        else
            view.appendTo(builder);

        return this;
    }

    public StringAppender append(StringView view, int beginIndex, int endIndex) {
        if (view == null)
            appendNull();
        else
            view.appendTo(builder, beginIndex, endIndex);

        return this;
    }

    @Override
    public StringAppender append(CharSequence s) {
        if (s instanceof StringView)
            ((StringView) s).appendTo(builder);
        else
            builder.append(s);
        return this;
    }

    @Override
    public StringAppender append(CharSequence s, int beginIndex, int endIndex) {
        if (s instanceof StringView)
            ((StringView) s).appendTo(builder, beginIndex, endIndex);
        else
            builder.append(s);
        return this;
    }

    public StringAppender appendFormatted(String format, Object... args) {
        new Formatter(this).format(format, args);
        return this;
    }

    public StringAppender appendLowerCase(CharSequence str) {
        if (str == null) {
            appendNull();
            return this;
        }

        builder.append(str.toString().toLowerCase(Locale.ROOT));
        return this;
    }

    public StringAppender appendUpperCase(CharSequence str) {
        if (str == null) {
            appendNull();
            return this;
        }

        builder.append(str.toString().toUpperCase(Locale.ROOT));
        return this;
    }

    private void beforeAppend(int n) {
        builder.ensureCapacity(builder.length() + n);
    }

    @Override
    public String toString() {
        return builder.toString();
    }
}
