package kala.text;

import kala.Conditions;
import kala.collection.base.primitive.ByteArrays;
import kala.collection.base.primitive.CharArrays;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.*;

public final class StringView implements CharSequence, Serializable {
    private static final StringView EMPTY = new StringView("", 0, 0);

    private final String value;
    private final int offset;
    private final int length;

    private StringView(String value, int offset, int length) {
        this.value = value;
        this.offset = offset;
        this.length = length;
    }

    public static StringView of(@NotNull String value) {
        return value.length() != 0 ? new StringView(value, 0, value.length()) : EMPTY;
    }

    public static StringView of(@NotNull String value, int beginIndex, int endIndex) {
        Conditions.checkPositionIndices(beginIndex, endIndex, value.length());
        return beginIndex != endIndex ? new StringView(value, beginIndex, endIndex - beginIndex) : EMPTY;
    }

    @SuppressWarnings("Since15")
    public boolean isEmpty() {
        return this.length == 0;
    }

    @Override
    public int length() {
        return length;
    }

    @Override
    public char charAt(int index) {
        Conditions.checkElementIndex(index, length);
        return value.charAt(index + offset);
    }

    public char[] getChars() {
        return getChars(0, length);
    }

    public char[] getChars(int beginIndex, int endIndex) {
        Conditions.checkPositionIndices(beginIndex, endIndex, length);
        int resLength = endIndex - beginIndex;
        if (resLength == 0)
            return CharArrays.EMPTY;

        char[] res = new char[resLength];
        value.getChars(beginIndex, endIndex, res, 0);
        return res;
    }

    public byte[] getBytes() {
        return getBytes(StandardCharsets.UTF_8);
    }

    public byte[] getBytes(Charset charset) {
        return getBytes(charset, 0, length);
    }

    public byte[] getBytes(Charset charset, int beginIndex, int endIndex) {
        Conditions.checkPositionIndices(beginIndex, endIndex, length);
        if (beginIndex == endIndex)
            return ByteArrays.EMPTY;

        ByteBuffer result;
        try {
             result = charset.newEncoder()
                    .onMalformedInput(CodingErrorAction.REPLACE)
                    .onUnmappableCharacter(CodingErrorAction.REPLACE)
                    .encode(CharBuffer.wrap(value, beginIndex + offset, endIndex + offset));
        } catch (CharacterCodingException e) {
            throw new AssertionError(e);
        }

        if (result.capacity() == result.limit()) {
            return result.array();
        } else {
            byte[] res = new byte[result.limit()];
            result.get(res);
            return res;
        }
    }

    @Override
    public @NotNull StringView subSequence(int start, int end) {
        return this.substring(start, end);
    }

    public @NotNull StringView substring(int beginIndex, int endIndex) {
        Conditions.checkPositionIndices(beginIndex, endIndex, length);
        return beginIndex != endIndex ? new StringView(value, offset + beginIndex, endIndex - beginIndex) : EMPTY;
    }

    public @NotNull StringView concat(@NotNull String other) {
        if (other.isEmpty())
            return this;
        if (this.isEmpty())
            return StringView.of(other);

        StringBuilder builder = new StringBuilder(this.length + other.length());
        this.appendTo(builder);
        builder.append(other);
        return StringView.of(builder.toString());
    }

    public @NotNull StringView concat(@NotNull StringView other) {
        if (other.isEmpty())
            return this;
        if (this.isEmpty())
            return other;

        StringBuilder builder = new StringBuilder(this.length + other.length());
        this.appendTo(builder);
        other.appendTo(builder);
        return StringView.of(builder.toString());
    }

    public boolean contentEquals(StringView other) {
        return this == other || this.length == other.length && this.value.regionMatches(this.offset, other.value, other.offset, this.length);
    }

    public boolean contentEquals(String other) {
        return this.length == other.length() && this.value.regionMatches(this.offset, other, 0, this.length);
    }

    public boolean contentEquals(CharSequence other) {
        if (this.length != other.length())
            return false;

        if (other instanceof StringView)
            return contentEquals(((StringView) other));
        if (other instanceof String)
            return contentEquals(((String) other));

        for (int i = 0; i < this.length; i++) {
            if (this.value.charAt(offset + i) != other.charAt(i))
                return false;
        }
        return true;
    }

    public StringBuilder appendTo(StringBuilder builder)  {
        builder.append(this.value, this.offset, this.offset + this.length);
        return builder;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof StringView))
            return false;
        return contentEquals(((StringView) obj));
    }

    @Override
    public String toString() {
        return value.substring(offset, offset + length);
    }
}
