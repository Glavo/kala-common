/*
 * Copyright 2024 Glavo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kala.text;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Formatter;
import java.util.Locale;
import java.util.Objects;

@SuppressWarnings("resource")
public class StringAppender extends Writer implements Externalizable {
    @Serial
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
            switch (obj) {
                case Object[] objects -> appendArray(objects);
                case char[] chars -> appendArray(chars);
                case boolean[] booleans -> appendArray(booleans);
                case byte[] bytes -> appendArray(bytes);
                case short[] shorts -> appendArray(shorts);
                case int[] ints -> appendArray(ints);
                case long[] longs -> appendArray(longs);
                case float[] floats -> appendArray(floats);
                case double[] doubles -> appendArray(doubles);
                default -> throw new AssertionError("Unknown array type: " + obj.getClass());
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

    public StringAppender append(StringSlice view) {
        if (view == null)
            appendNull();
        else
            view.appendTo(builder);

        return this;
    }

    public StringAppender append(StringSlice view, int beginIndex, int endIndex) {
        if (view == null)
            appendNull();
        else
            view.slice(beginIndex, endIndex).appendTo(builder);

        return this;
    }

    @Override
    public StringAppender append(CharSequence s) {
        if (s instanceof StringSlice)
            ((StringSlice) s).appendTo(builder);
        else
            builder.append(s);
        return this;
    }

    @Override
    public StringAppender append(CharSequence s, int beginIndex, int endIndex) {
        if (s instanceof StringSlice)
            ((StringSlice) s).appendTo(builder, beginIndex, endIndex);
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
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(builder.toString());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.builder.setLength(0);
        this.builder.append((String) in.readObject());
    }

    @Override
    public String toString() {
        return builder.toString();
    }
}
