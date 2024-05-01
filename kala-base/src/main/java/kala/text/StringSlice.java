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

import kala.Conditions;
import kala.collection.base.primitive.ByteArrays;
import kala.collection.base.primitive.CharArrays;
import kala.control.primitive.*;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.*;

public final class StringSlice implements Comparable<StringSlice>, CharSequence, Serializable {
    @Serial
    private static final long serialVersionUID = 0L;
    private static final int ZERO_HASH_REPLACE = 914090028;

    private static final StringSlice EMPTY = new StringSlice("", 0, 0);

    private final String value;
    private final int offset;
    private final int length;
    private int hash;

    private StringSlice(String value, int offset, int length) {
        this.value = value;
        this.offset = offset;
        this.length = length;
    }

    public static StringSlice of(@NotNull String value) {
        return !value.isEmpty() ? new StringSlice(value, 0, value.length()) : EMPTY;
    }

    public static StringSlice of(@NotNull String value, int beginIndex, int endIndex) {
        Conditions.checkPositionIndices(beginIndex, endIndex, value.length());
        return beginIndex != endIndex ? new StringSlice(value, beginIndex, endIndex - beginIndex) : EMPTY;
    }

    public boolean isEmpty() {
        return this.length == 0;
    }

    @ApiStatus.Internal
    public String source() {
        return value;
    }

    @ApiStatus.Internal
    public int sourceOffset() {
        return offset;
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
        value.getChars(offset + beginIndex, offset + endIndex, res, 0);
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
    public @NotNull StringSlice subSequence(int start, int end) {
        return this.substring(start, end);
    }

    public @NotNull StringSlice substring(int beginIndex) {
        return substring(beginIndex, length);
    }

    public @NotNull StringSlice substring(int beginIndex, int endIndex) {
        Conditions.checkPositionIndices(beginIndex, endIndex, length);
        return beginIndex != endIndex ? new StringSlice(value, offset + beginIndex, endIndex - beginIndex) : EMPTY;
    }

    public @NotNull StringSlice concat(@NotNull String other) {
        if (other.isEmpty())
            return this;
        if (this.isEmpty())
            return StringSlice.of(other);

        StringBuilder builder = new StringBuilder(this.length + other.length());
        this.appendTo(builder);
        builder.append(other);
        return StringSlice.of(builder.toString());
    }

    public @NotNull StringSlice concat(@NotNull StringSlice other) {
        if (other.isEmpty())
            return this;
        if (this.isEmpty())
            return other;

        StringBuilder builder = new StringBuilder(this.length + other.length());
        this.appendTo(builder);
        other.appendTo(builder);
        return StringSlice.of(builder.toString());
    }

    public boolean startsWith(@NotNull String prefix) {
        return startsWith(prefix, 0);
    }

    public boolean startsWith(@NotNull String prefix, int toIndex) {
        if (toIndex < 0 || toIndex > this.length - prefix.length())
            return false;
        return value.regionMatches(offset + toIndex, prefix, 0, prefix.length());
    }

    public boolean startsWith(@NotNull StringSlice prefix) {
        return startsWith(prefix, 0);
    }

    public boolean startsWith(@NotNull StringSlice prefix, int toIndex) {
        if (toIndex < 0 || toIndex > this.length - prefix.length())
            return false;
        return value.regionMatches(offset + toIndex, prefix.value, prefix.offset, prefix.length);
    }

    public boolean endsWith(@NotNull String suffix) {
        return startsWith(suffix, this.length - suffix.length());
    }

    public boolean endsWith(@NotNull StringSlice suffix) {
        return startsWith(suffix, this.length - suffix.length());
    }

    public int indexOf(char ch) {
        return indexOf(ch, 0, length);
    }

    public int indexOf(char ch, int beginIndex) {
        return indexOf(ch, beginIndex, length);
    }

    public int indexOf(char ch, int beginIndex, int endIndex) {
        Conditions.checkPositionIndices(beginIndex, endIndex, length);

        for (int i = beginIndex + offset; i < endIndex + offset; i++) {
            if (value.charAt(i) == ch)
                return i;
        }
        return -1;
    }

    public int indexOf(int ch) {
        return indexOf(ch, 0);
    }

    public int indexOf(int ch, int fromIndex) {
        if (ch <= 0xFFFF)
            return indexOf((char) ch, 0, length);

        if (fromIndex >= length)
            return -1;
        if (fromIndex < 0)
            fromIndex = 0;

        int idx = value.indexOf(ch, fromIndex + offset);
        return idx >= offset && idx < offset + length ? idx - offset : -1;
    }

    public boolean contains(char ch) {
        return indexOf(ch) >= 0;
    }

    public boolean contains(String other) {
        int idx = value.indexOf(other, offset);
        return idx >= 0 && idx <= offset + length - other.length();
    }

    public boolean contains(CharSequence other) {
        return contains(other.toString());
    }

    public StringSlice replace(char oldChar, char newChar) {
        if (oldChar == newChar)
            return this;

        int idx = indexOf(oldChar);
        if (idx < 0)
            return this;

        StringBuilder res = new StringBuilder(this.length);

        idx += offset;
        res.append(this.value, offset, idx);
        res.append(newChar);

        while (++idx < this.offset + this.length) {
            char ch = this.value.charAt(idx);
            res.append(ch == oldChar ? newChar : ch);
        }

        return StringSlice.of(res.toString());
    }

    public boolean contentEquals(StringSlice other) {
        return this == other || this.length == other.length && this.value.regionMatches(this.offset, other.value, other.offset, this.length);
    }

    public boolean contentEquals(String other) {
        return this.length == other.length() && this.value.regionMatches(this.offset, other, 0, this.length);
    }

    public boolean contentEquals(CharSequence other) {
        if (this.length != other.length())
            return false;

        if (other instanceof StringSlice)
            return contentEquals(((StringSlice) other));
        if (other instanceof String)
            return contentEquals(((String) other));

        for (int i = 0; i < this.length; i++) {
            if (this.value.charAt(offset + i) != other.charAt(i))
                return false;
        }
        return true;
    }

    public boolean contentEqualsIgnoreCase(StringSlice other) {
        return this == other || this.length == other.length && this.value.regionMatches(true, this.offset, other.value, other.offset, this.length);
    }

    public boolean contentEqualsIgnoreCase(String other) {
        return this.length == other.length() && this.value.regionMatches(true, this.offset, other, 0, this.length);
    }

    public boolean contentEqualsIgnoreCase(CharSequence other) {
        if (this.length != other.length())
            return false;

        if (other instanceof StringSlice)
            return contentEqualsIgnoreCase(((StringSlice) other));

        return contentEqualsIgnoreCase(other.toString());
    }

    public void appendTo(StringBuilder builder) {
        appendTo(builder, 0, this.length);
    }

    public void appendTo(StringBuilder builder, int beginIndex, int endIndex) {
        Conditions.checkPositionIndices(beginIndex, endIndex, this.length);
        if (beginIndex != endIndex)
            builder.append(this.value, this.offset + beginIndex, this.offset + endIndex);
    }

    public boolean toBoolean() {
        return contentEqualsIgnoreCase("true");
    }

    public byte toByte() throws NumberFormatException {
        return Byte.parseByte(toString(), 10);
    }

    public byte toByte(int radix) throws NumberFormatException {
        return Byte.parseByte(toString(), radix);
    }

    public @Nullable Byte toByteOrNull() {
        try {
            return Byte.parseByte(toString(), 10);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public @Nullable Byte toByteOrNull(int radix) {
        try {
            return Byte.parseByte(toString(), radix);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public @NotNull ByteOption toByteOption() {
        try {
            return ByteOption.some(Byte.parseByte(toString(), 10));
        } catch (NumberFormatException e) {
            return ByteOption.none();
        }
    }

    public @NotNull ByteOption toByteOption(int radix) {
        try {
            return ByteOption.some(Byte.parseByte(toString(), radix));
        } catch (NumberFormatException e) {
            return ByteOption.none();
        }
    }

    public short toShort() throws NumberFormatException {
        return Short.parseShort(toString(), 10);
    }

    public short toShort(int radix) throws NumberFormatException {
        return Short.parseShort(toString(), radix);
    }

    public @Nullable Short toShortOrNull() {
        try {
            return Short.parseShort(toString(), 10);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public @Nullable Short toShortOrNull(int radix) {
        try {
            return Short.parseShort(toString(), radix);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public @NotNull ShortOption toShortOption() {
        try {
            return ShortOption.some(Short.parseShort(toString(), 10));
        } catch (NumberFormatException e) {
            return ShortOption.none();
        }
    }

    public @NotNull ShortOption toShortOption(int radix) {
        try {
            return ShortOption.some(Short.parseShort(toString(), radix));
        } catch (NumberFormatException e) {
            return ShortOption.none();
        }
    }

    public int toInt() throws NumberFormatException {
        return toInt(10);
    }

    public int toInt(int radix) throws NumberFormatException {
        return Integer.parseInt(toString(), radix);
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
        return Long.parseLong(toString(), radix);
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
        return Float.parseFloat(toString());
    }

    public @Nullable Float toFloatOrNull() {
        try {
            return Float.parseFloat(toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public @NotNull FloatOption toFloatOption() {
        try {
            return FloatOption.some(Float.parseFloat(toString()));
        } catch (NumberFormatException e) {
            return FloatOption.none();
        }
    }

    public double toDouble() throws NumberFormatException {
        return Double.parseDouble(toString());
    }

    public @Nullable Double toDoubleOrNull() {
        try {
            return Double.parseDouble(toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public @NotNull DoubleOption toDoubleOption() {
        try {
            return DoubleOption.some(Double.parseDouble(toString()));
        } catch (NumberFormatException e) {
            return DoubleOption.none();
        }
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
        if (!(obj instanceof StringSlice))
            return false;
        return contentEquals(((StringSlice) obj));
    }

    @Override
    public String toString() {
        return value.substring(offset, offset + length);
    }

    @Override
    public int compareTo(@NotNull StringSlice other) {
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
