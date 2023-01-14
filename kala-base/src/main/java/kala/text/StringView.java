package kala.text;

import kala.Conditions;
import kala.collection.base.primitive.*;
import kala.control.primitive.*;
import kala.function.CharConsumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Formatter;
import java.util.Locale;
import java.util.Objects;
import java.util.function.IntConsumer;

public final class StringView implements Comparable<StringView>, CharSequence, CharTraversable, Serializable {
    private static final long serialVersionUID = 0L;

    private static final StringView EMPTY = new StringView("");

    private final String value;

    private StringView(String value) {
        this.value = value;
    }

    public static StringView of(@NotNull String value) {
        Objects.requireNonNull(value);
        return !value.isEmpty() ? new StringView(value) : EMPTY;
    }

    public static StringView of(@NotNull CharSequence value) {
        return of(value.toString());
    }

    private StringView updated(String value) {
        //noinspection StringEquality
        return this.value != value ? StringView.of(value) : this;
    }

    @SuppressWarnings("Since15")
    @Override
    public boolean isEmpty() {
        return value.isEmpty();
    }

    @Override
    public int length() {
        return value.length();
    }

    @Override
    public char charAt(int index) {
        return value.charAt(index);
    }

    public char[] getChars() {
        return value.toCharArray();
    }

    public char[] getChars(int beginIndex, int endIndex) {
        Conditions.checkPositionIndices(beginIndex, endIndex, value.length());
        int resLength = endIndex - beginIndex;
        if (resLength == 0)
            return CharArrays.EMPTY;

        char[] res = new char[resLength];
        value.getChars(beginIndex, endIndex, res, 0);
        return res;
    }

    public byte[] getBytes() {
        return value.getBytes(StandardCharsets.UTF_8);
    }

    public byte[] getBytes(int beginIndex, int endIndex) {
        return getBytes(StandardCharsets.UTF_8, beginIndex, endIndex);
    }

    public byte[] getBytes(Charset charset) {
        return value.getBytes(charset);
    }

    public byte[] getBytes(Charset charset, int beginIndex, int endIndex) {
        Conditions.checkPositionIndices(beginIndex, endIndex, value.length());
        if (beginIndex == endIndex)
            return ByteArrays.EMPTY;
        if (this.value.length() == endIndex - beginIndex)
            return this.value.getBytes(charset);

        ByteBuffer result;
        try {
            result = charset.newEncoder()
                    .onMalformedInput(CodingErrorAction.REPLACE)
                    .onUnmappableCharacter(CodingErrorAction.REPLACE)
                    .encode(CharBuffer.wrap(value, beginIndex, endIndex));
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

    public StringView formatted(Object... args) {
        return updated(String.format(value, args));
    }

    public StringView repeat(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("count is negative: " + count);
        }
        if (count == 1) {
            return this;
        }
        if (value.isEmpty() || count == 0) {
            return EMPTY;
        }
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < count; i++) {
            res.append(value);
        }
        return StringView.of(res.toString());
    }

    @Override
    public @NotNull StringView subSequence(int start, int end) {
        return updated(value.substring(start, end));
    }

    public @NotNull StringView substring(int beginIndex) {
        return updated(value.substring(beginIndex));
    }

    public @NotNull StringView substring(int beginIndex, int endIndex) {
        return updated(value.substring(beginIndex, endIndex));
    }

    public @NotNull StringView removePrefix(String prefix) {
        return startsWith(prefix) ? substring(prefix.length()) : this;
    }

    public @NotNull StringView removePrefix(StringSlice prefix) {
        return startsWith(prefix) ? substring(prefix.length()) : this;
    }

    public @NotNull StringView removePrefix(CharSequence prefix) {
        return startsWith(prefix) ? substring(prefix.length()) : this;
    }

    public @NotNull StringView removeSuffix(String suffix) {
        return endsWith(suffix) ? substring(0, value.length() - suffix.length()) : this;
    }

    public @NotNull StringView removeSuffix(StringSlice suffix) {
        return endsWith(suffix) ? substring(0, value.length() - suffix.length()) : this;
    }

    public @NotNull StringView removeSuffix(CharSequence suffix) {
        return endsWith(suffix) ? substring(0, value.length() - suffix.length()) : this;
    }

    public @NotNull StringView concat(@NotNull CharSequence other) {
        return updated(this.value.concat(other.toString()));
    }

    public @NotNull StringView concat(@NotNull String other) {
        if (this.isEmpty())
            return StringView.of(other);

        return updated(this.value.concat(other));
    }

    public @NotNull StringView concat(@NotNull StringSlice other) {
        if (other.isEmpty())
            return this;
        if (this.isEmpty())
            return StringView.of(other.toString());

        StringBuilder builder = new StringBuilder(this.length() + other.length());
        builder.append(this.value);
        other.appendTo(builder);
        return StringView.of(builder.toString());
    }

    public boolean startsWith(@NotNull String prefix) {
        return value.startsWith(prefix);
    }

    public boolean startsWith(@NotNull String prefix, int toIndex) {
        return value.startsWith(prefix, toIndex);
    }

    public boolean startsWith(@NotNull StringSlice prefix) {
        return startsWith(prefix, 0);
    }

    public boolean startsWith(@NotNull StringSlice prefix, int toIndex) {
        if (toIndex < 0 || toIndex > value.length() - prefix.length())
            return false;
        return value.regionMatches(toIndex, prefix.source(), prefix.sourceOffset(), prefix.length());
    }

    public boolean startsWith(@NotNull CharSequence prefix) {
        return prefix instanceof StringSlice ? startsWith((StringSlice) prefix) : value.startsWith(prefix.toString());
    }

    public boolean startsWith(@NotNull CharSequence prefix, int toIndex) {
        return prefix instanceof StringSlice ? startsWith((StringSlice) prefix, toIndex) : value.startsWith(prefix.toString(), toIndex);
    }

    public boolean endsWith(@NotNull String suffix) {
        return value.endsWith(suffix);
    }

    public boolean endsWith(@NotNull StringSlice suffix) {
        return startsWith(suffix, value.length() - suffix.length());
    }

    public boolean endsWith(@NotNull CharSequence suffix) {
        return suffix instanceof StringSlice ? endsWith((StringSlice) suffix) : value.endsWith(suffix.toString());
    }

    public int indexOf(char ch) {
        return value.indexOf(ch);
    }

    public int indexOf(char ch, int beginIndex) {
        return value.indexOf(ch, beginIndex);
    }

    public int indexOf(char ch, int beginIndex, int endIndex) {
        Conditions.checkPositionIndices(beginIndex, endIndex, value.length());

        for (int i = beginIndex; i < endIndex; i++) {
            if (value.charAt(i) == ch)
                return i;
        }
        return -1;
    }

    public int lastIndexOf(char ch) {
        return value.lastIndexOf(ch);
    }

    public int lastIndexOf(char ch, int beginIndex) {
        return value.lastIndexOf(ch, beginIndex);
    }

    public int lastIndexOf(char ch, int beginIndex, int endIndex) {
        Conditions.checkPositionIndices(beginIndex, endIndex, value.length());

        for (int i = endIndex - 1; i >= beginIndex; i--) {
            if (value.charAt(i) == ch)
                return i;
        }
        return -1;
    }

    public int indexOf(int ch) {
        return indexOf(ch, 0);
    }

    public int indexOf(int ch, int fromIndex) {
        return value.indexOf(ch, fromIndex);
    }

    public StringView toLowerCase() {
        return updated(value.toLowerCase(Locale.ROOT));
    }

    public StringView toLowerCase(Locale locale) {
        return updated(value.toLowerCase(locale));
    }

    public StringView toUpperCase() {
        return updated(value.toUpperCase(Locale.ROOT));
    }

    public StringView toUpperCase(Locale locale) {
        return updated(value.toUpperCase(locale));
    }

    public StringView trim() {
        return updated(value.trim());
    }

    public StringView replace(char oldChar, char newChar) {
        return updated(value.replace(oldChar, newChar));
    }

    public boolean contentEquals(StringSlice other) {
        return value.length() == other.length() && this.value.regionMatches(0, other.source(), other.sourceOffset(), value.length());
    }

    public boolean contentEquals(String other) {
        return this.value.equals(other);
    }

    public boolean contentEquals(CharSequence other) {
        if (value.length() != other.length())
            return false;

        if (other instanceof StringSlice)
            return contentEquals(((StringSlice) other));
        if (other instanceof String)
            return contentEquals(((String) other));

        for (int i = 0; i < value.length(); i++) {
            if (this.value.charAt(i) != other.charAt(i))
                return false;
        }
        return true;
    }

    public boolean contentEqualsIgnoreCase(StringSlice other) {
        return value.length() == other.length() && this.value.regionMatches(true, 0, other.source(), other.sourceOffset(), value.length());
    }

    public boolean contentEqualsIgnoreCase(String other) {
        return value.equalsIgnoreCase(other);
    }

    public boolean contentEqualsIgnoreCase(CharSequence other) {
        if (value.length() != other.length())
            return false;

        if (other instanceof StringSlice)
            return contentEqualsIgnoreCase(((StringSlice) other));

        return contentEqualsIgnoreCase(other.toString());
    }

    public boolean toBoolean() {
        return contentEqualsIgnoreCase("true");
    }

    public byte toByte() throws NumberFormatException {
        return Byte.parseByte(value, 10);
    }

    public byte toByte(int radix) throws NumberFormatException {
        return Byte.parseByte(value, radix);
    }

    public @Nullable Byte toByteOrNull() {
        try {
            return Byte.parseByte(value, 10);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public @Nullable Byte toByteOrNull(int radix) {
        try {
            return Byte.parseByte(value, radix);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public @NotNull ByteOption toByteOption() {
        try {
            return ByteOption.some(Byte.parseByte(value, 10));
        } catch (NumberFormatException e) {
            return ByteOption.none();
        }
    }

    public @NotNull ByteOption toByteOption(int radix) {
        try {
            return ByteOption.some(Byte.parseByte(value, radix));
        } catch (NumberFormatException e) {
            return ByteOption.none();
        }
    }

    public short toShort() throws NumberFormatException {
        return Short.parseShort(value, 10);
    }

    public short toShort(int radix) throws NumberFormatException {
        return Short.parseShort(value, radix);
    }

    public @Nullable Short toShortOrNull() {
        try {
            return Short.parseShort(value, 10);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public @Nullable Short toShortOrNull(int radix) {
        try {
            return Short.parseShort(value, radix);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public @NotNull ShortOption toShortOption() {
        try {
            return ShortOption.some(Short.parseShort(value, 10));
        } catch (NumberFormatException e) {
            return ShortOption.none();
        }
    }

    public @NotNull ShortOption toShortOption(int radix) {
        try {
            return ShortOption.some(Short.parseShort(value, radix));
        } catch (NumberFormatException e) {
            return ShortOption.none();
        }
    }

    public int toInt() throws NumberFormatException {
        return toInt(10);
    }

    public int toInt(int radix) throws NumberFormatException {
        return Integer.parseInt(value, radix);
    }

    public @Nullable Integer toIntOrNull() {
        try {
            return toInt(10);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public @Nullable Integer toIntOrNull(int radix) {
        try {
            return toInt(radix);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public @NotNull IntOption toIntOption() {
        try {
            return IntOption.some(toInt(10));
        } catch (NumberFormatException e) {
            return IntOption.none();
        }
    }

    public @NotNull IntOption toIntOption(int radix) {
        try {
            return IntOption.some(toInt(radix));
        } catch (NumberFormatException e) {
            return IntOption.none();
        }
    }

    public long toLong() throws NumberFormatException {
        return toLong(10);
    }

    public long toLong(int radix) throws NumberFormatException {
        return Long.parseLong(value, radix);
    }

    public @Nullable Long toLongOrNull() {
        try {
            return toLong(10);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public @Nullable Long toLongOrNull(int radix) {
        try {
            return toLong(radix);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public @NotNull LongOption toLongOption() {
        try {
            return LongOption.some(toLong(10));
        } catch (NumberFormatException e) {
            return LongOption.none();
        }
    }

    public @NotNull LongOption toLongOption(int radix) {
        try {
            return LongOption.some(toLong(radix));
        } catch (NumberFormatException e) {
            return LongOption.none();
        }
    }

    public float toFloat() throws NumberFormatException {
        return Float.parseFloat(value);
    }

    public @Nullable Float toFloatOrNull() {
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public @NotNull FloatOption toFloatOption() {
        try {
            return FloatOption.some(Float.parseFloat(value));
        } catch (NumberFormatException e) {
            return FloatOption.none();
        }
    }

    public double toDouble() throws NumberFormatException {
        return Double.parseDouble(value);
    }

    public @Nullable Double toDoubleOrNull() {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public @NotNull DoubleOption toDoubleOption() {
        try {
            return DoubleOption.some(Double.parseDouble(value));
        } catch (NumberFormatException e) {
            return DoubleOption.none();
        }
    }

    //region CharSequence

    @Override
    public @NotNull CharIterator iterator() {
        return CharIterator.of(value);
    }

    public void forEach(@NotNull CharConsumer action) {
        for (int i = 0; i < value.length(); i++) {
            action.accept(value.charAt(i));
        }
    }

    public void forEachCodePoint(@NotNull IntConsumer action) {
        for(int offset = 0; offset < value.length();) {
            int ch = value.codePointAt(offset);
            action.accept(ch);
            offset += Character.charCount(ch);
        }
    }

    @Override
    public char @NotNull [] toArray() {
        return value.toCharArray();
    }

    //endregion

    @Override
    public int compareTo(@NotNull StringView o) {
        return this.value.compareTo(o.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof StringView && this.value.equals(((StringView) obj).value);
    }

    public boolean equalsIgnoreCase(String other) {
        return this.value.equalsIgnoreCase(other);
    }

    public boolean equalsIgnoreCase(StringView other) {
        return this.value.equalsIgnoreCase(other.value);
    }

    @Override
    public String toString() {
        return value;
    }
}
