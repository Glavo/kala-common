package kala.text;

import kala.Conditions;
import kala.collection.base.primitive.ByteArrays;
import kala.collection.base.primitive.CharArrays;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.*;
import java.util.Objects;
import java.util.stream.IntStream;

public final class StringView implements Comparable<StringView>, CharSequence, Serializable {
    private static final long serialVersionUID = 0L;
    private static final int ZERO_HASH_REPLACE = 914090028;

    private static final StringView EMPTY = new StringView("", 0, 0);

    private final String value;
    private final int offset;
    private final int length;
    private int hash;

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
        return getBytes(StandardCharsets.UTF_8, 0, length);
    }

    public byte[] getBytes(int beginIndex, int endIndex) {
        return getBytes(StandardCharsets.UTF_8, beginIndex, endIndex);
    }

    public byte[] getBytes(Charset charset) {
        return getBytes(charset, 0, length);
    }

    public byte[] getBytes(Charset charset, int beginIndex, int endIndex) {
        Conditions.checkPositionIndices(beginIndex, endIndex, length);
        if (beginIndex == endIndex)
            return ByteArrays.EMPTY;
        if (this.value.length() == endIndex - beginIndex)
            return this.value.getBytes(charset);

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

    public boolean contentEqualsIgnoreCase(StringView other) {
        return this == other || this.length == other.length && this.value.regionMatches(true, this.offset, other.value, other.offset, this.length);
    }

    public boolean contentEqualsIgnoreCase(String other) {
        return this.length == other.length() && this.value.regionMatches(true, this.offset, other, 0, this.length);
    }

    public boolean contentEqualsIgnoreCase(CharSequence other) {
        if (this.length != other.length())
            return false;

        if (other instanceof StringView)
            return contentEqualsIgnoreCase(((StringView) other));

        return contentEqualsIgnoreCase(other.toString());
    }

    public void appendTo(StringBuilder builder)  {
        appendTo(builder, 0, this.length);
    }

    public void appendTo(StringBuilder builder, int beginIndex, int endIndex)  {
        Conditions.checkPositionIndices(beginIndex, endIndex, this.length);
        if (beginIndex != endIndex)
            builder.append(this.value, this.offset + beginIndex, this.offset + endIndex);
    }

    @Override
    public int hashCode() {
        int h = hash;
        if (h != 0)
            return h;

        for (int i = offset, end = offset + length; i < end; i++) {
            h = 31 * h + value.charAt(i);
        }

        return hash = (h == 0 ? ZERO_HASH_REPLACE : h);
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

    @Override
    public int compareTo(@NotNull StringView other) {
        int lim = Math.min(this.length, other.length);
        for (int i = 0; i < lim; i++) {
            char c1 = this.charAt(i);
            char c2 = other.charAt(i);
            if (c1 != c2)
                return c1 - c2;
        }
        return this.length - other.length;
    }
}
