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
package kala.range;

import kala.annotations.StaticClass;
import kala.range.primitive.*;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

@StaticClass
public final class Ranges {
    private Ranges() {
    }

    public static @NotNull CharRange range(char lowerBound, char upperBound) {
        return CharRange.closedOpen(lowerBound, upperBound);
    }

    public static @NotNull ByteRange range(byte lowerBound, byte upperBound) {
        return closedOpen(lowerBound, upperBound);
    }

    public static @NotNull ShortRange range(short lowerBound, short upperBound) {
        return closedOpen(lowerBound, upperBound);
    }

    public static @NotNull IntRange range(int lowerBound, int upperBound) {
        return closedOpen(lowerBound, upperBound);
    }

    public static @NotNull LongRange range(long lowerBound, long upperBound) {
        return closedOpen(lowerBound, upperBound);
    }

    public static @NotNull FloatRange range(float lowerBound, float upperBound) {
        return closedOpen(lowerBound, upperBound);
    }

    public static @NotNull DoubleRange range(double lowerBound, double upperBound) {
        return closedOpen(lowerBound, upperBound);
    }

    public static <T extends Comparable<? super T>> @NotNull GenericRange<T> range(T lowerBound, T upperBound) {
        return closedOpen(lowerBound, upperBound);
    }

    public static <T> @NotNull GenericRange<T> range(T lowerBound, T upperBound, Comparator<? super T> comparator) {
        return closedOpen(lowerBound, upperBound, comparator);
    }


    public static @NotNull CharRange is(char value) {
        return CharRange.is(value);
    }

    public static @NotNull CharRange open(char lowerBound, char upperBound) {
        return CharRange.open(lowerBound, upperBound);
    }

    public static @NotNull CharRange closed(char lowerBound, char upperBound) {
        return CharRange.closed(lowerBound, upperBound);
    }

    public static @NotNull CharRange openClosed(char lowerBound, char upperBound) {
        return CharRange.openClosed(lowerBound, upperBound);
    }

    public static @NotNull CharRange closedOpen(char lowerBound, char upperBound) {
        return CharRange.closedOpen(lowerBound, upperBound);
    }

    public static @NotNull CharRange greaterThan(char lowerBound) {
        return CharRange.greaterThan(lowerBound);
    }

    public static @NotNull CharRange atLeast(char lowerBound) {
        return CharRange.atLeast(lowerBound);
    }

    public static @NotNull CharRange lessThan(char upperBound) {
        return CharRange.lessThan(upperBound);
    }

    public static @NotNull CharRange atMost(char upperBound) {
        return CharRange.atMost(upperBound);
    }


    public static @NotNull ByteRange is(byte value) {
        return ByteRange.is(value);
    }

    public static @NotNull ByteRange open(byte lowerBound, byte upperBound) {
        return ByteRange.open(lowerBound, upperBound);
    }

    public static @NotNull ByteRange closed(byte lowerBound, byte upperBound) {
        return ByteRange.closed(lowerBound, upperBound);
    }

    public static @NotNull ByteRange openClosed(byte lowerBound, byte upperBound) {
        return ByteRange.openClosed(lowerBound, upperBound);
    }

    public static @NotNull ByteRange closedOpen(byte lowerBound, byte upperBound) {
        return ByteRange.closedOpen(lowerBound, upperBound);
    }

    public static @NotNull ByteRange greaterThan(byte lowerBound) {
        return ByteRange.greaterThan(lowerBound);
    }

    public static @NotNull ByteRange atLeast(byte lowerBound) {
        return ByteRange.atLeast(lowerBound);
    }

    public static @NotNull ByteRange lessThan(byte upperBound) {
        return ByteRange.lessThan(upperBound);
    }

    public static @NotNull ByteRange atMost(byte upperBound) {
        return ByteRange.atMost(upperBound);
    }


    public static @NotNull ShortRange is(short value) {
        return ShortRange.is(value);
    }

    public static @NotNull ShortRange open(short lowerBound, short upperBound) {
        return ShortRange.open(lowerBound, upperBound);
    }

    public static @NotNull ShortRange closed(short lowerBound, short upperBound) {
        return ShortRange.closed(lowerBound, upperBound);
    }

    public static @NotNull ShortRange openClosed(short lowerBound, short upperBound) {
        return ShortRange.openClosed(lowerBound, upperBound);
    }

    public static @NotNull ShortRange closedOpen(short lowerBound, short upperBound) {
        return ShortRange.closedOpen(lowerBound, upperBound);
    }

    public static @NotNull ShortRange greaterThan(short lowerBound) {
        return ShortRange.greaterThan(lowerBound);
    }

    public static @NotNull ShortRange atLeast(short lowerBound) {
        return ShortRange.atLeast(lowerBound);
    }

    public static @NotNull ShortRange lessThan(short upperBound) {
        return ShortRange.lessThan(upperBound);
    }

    public static @NotNull ShortRange atMost(short upperBound) {
        return ShortRange.atMost(upperBound);
    }


    public static @NotNull IntRange is(int value) {
        return IntRange.is(value);
    }

    public static @NotNull IntRange open(int lowerBound, int upperBound) {
        return IntRange.open(lowerBound, upperBound);
    }

    public static @NotNull IntRange closed(int lowerBound, int upperBound) {
        return IntRange.closed(lowerBound, upperBound);
    }

    public static @NotNull IntRange openClosed(int lowerBound, int upperBound) {
        return IntRange.openClosed(lowerBound, upperBound);
    }

    public static @NotNull IntRange closedOpen(int lowerBound, int upperBound) {
        return IntRange.closedOpen(lowerBound, upperBound);
    }

    public static @NotNull IntRange greaterThan(int lowerBound) {
        return IntRange.greaterThan(lowerBound);
    }

    public static @NotNull IntRange atLeast(int lowerBound) {
        return IntRange.atLeast(lowerBound);
    }

    public static @NotNull IntRange lessThan(int upperBound) {
        return IntRange.lessThan(upperBound);
    }

    public static @NotNull IntRange atMost(int upperBound) {
        return IntRange.atMost(upperBound);
    }


    public static @NotNull LongRange is(long value) {
        return LongRange.is(value);
    }

    public static @NotNull LongRange open(long lowerBound, long upperBound) {
        return LongRange.open(lowerBound, upperBound);
    }

    public static @NotNull LongRange closed(long lowerBound, long upperBound) {
        return LongRange.closed(lowerBound, upperBound);
    }

    public static @NotNull LongRange openClosed(long lowerBound, long upperBound) {
        return LongRange.openClosed(lowerBound, upperBound);
    }

    public static @NotNull LongRange closedOpen(long lowerBound, long upperBound) {
        return LongRange.closedOpen(lowerBound, upperBound);
    }

    public static @NotNull LongRange greaterThan(long lowerBound) {
        return LongRange.greaterThan(lowerBound);
    }

    public static @NotNull LongRange atLeast(long lowerBound) {
        return LongRange.atLeast(lowerBound);
    }

    public static @NotNull LongRange lessThan(long upperBound) {
        return LongRange.lessThan(upperBound);
    }

    public static @NotNull LongRange atMost(long upperBound) {
        return LongRange.atMost(upperBound);
    }


    public static @NotNull FloatRange is(float value) {
        return FloatRange.is(value);
    }

    public static @NotNull FloatRange open(float lowerBound, float upperBound) {
        return FloatRange.open(lowerBound, upperBound);
    }

    public static @NotNull FloatRange closed(float lowerBound, float upperBound) {
        return FloatRange.closed(lowerBound, upperBound);
    }

    public static @NotNull FloatRange openClosed(float lowerBound, float upperBound) {
        return FloatRange.openClosed(lowerBound, upperBound);
    }

    public static @NotNull FloatRange closedOpen(float lowerBound, float upperBound) {
        return FloatRange.closedOpen(lowerBound, upperBound);
    }

    public static @NotNull FloatRange greaterThan(float lowerBound) {
        return FloatRange.greaterThan(lowerBound);
    }

    public static @NotNull FloatRange atLeast(float lowerBound) {
        return FloatRange.atLeast(lowerBound);
    }

    public static @NotNull FloatRange lessThan(float upperBound) {
        return FloatRange.lessThan(upperBound);
    }

    public static @NotNull FloatRange atMost(float upperBound) {
        return FloatRange.atMost(upperBound);
    }


    public static @NotNull DoubleRange is(double value) {
        return DoubleRange.is(value);
    }

    public static @NotNull DoubleRange open(double lowerBound, double upperBound) {
        return DoubleRange.open(lowerBound, upperBound);
    }

    public static @NotNull DoubleRange closed(double lowerBound, double upperBound) {
        return DoubleRange.closed(lowerBound, upperBound);
    }

    public static @NotNull DoubleRange openClosed(double lowerBound, double upperBound) {
        return DoubleRange.openClosed(lowerBound, upperBound);
    }

    public static @NotNull DoubleRange closedOpen(double lowerBound, double upperBound) {
        return DoubleRange.closedOpen(lowerBound, upperBound);
    }

    public static @NotNull DoubleRange greaterThan(double lowerBound) {
        return DoubleRange.greaterThan(lowerBound);
    }

    public static @NotNull DoubleRange atLeast(double lowerBound) {
        return DoubleRange.atLeast(lowerBound);
    }

    public static @NotNull DoubleRange lessThan(double upperBound) {
        return DoubleRange.lessThan(upperBound);
    }

    public static @NotNull DoubleRange atMost(double upperBound) {
        return DoubleRange.atMost(upperBound);
    }


    public static <T extends Comparable<? super T>> @NotNull GenericRange<T> is(T value) {
        return GenericRange.is(value);
    }

    public static <T extends Comparable<? super T>> @NotNull GenericRange<T> open(T lowerBound, T upperBound) {
        return GenericRange.open(lowerBound, upperBound);
    }

    public static <T extends Comparable<? super T>> @NotNull GenericRange<T> closed(T lowerBound, T upperBound) {
        return GenericRange.closed(lowerBound, upperBound);
    }

    public static <T extends Comparable<? super T>> @NotNull GenericRange<T> openClosed(T lowerBound, T upperBound) {
        return GenericRange.openClosed(lowerBound, upperBound);
    }

    public static <T extends Comparable<? super T>> @NotNull GenericRange<T> closedOpen(T lowerBound, T upperBound) {
        return GenericRange.closedOpen(lowerBound, upperBound);
    }

    public static <T extends Comparable<? super T>> @NotNull GenericRange<T> greaterThan(T lowerBound) {
        return GenericRange.greaterThan(lowerBound);
    }

    public static <T extends Comparable<? super T>> @NotNull GenericRange<T> atLeast(T lowerBound) {
        return GenericRange.atLeast(lowerBound);
    }

    public static <T extends Comparable<? super T>> @NotNull GenericRange<T> lessThan(T upperBound) {
        return GenericRange.lessThan(upperBound);
    }

    public static <T extends Comparable<? super T>> @NotNull GenericRange<T> atMost(T upperBound) {
        return GenericRange.atMost(upperBound);
    }

    public static <T> @NotNull GenericRange<T> is(T value, Comparator<? super T> comparator) {
        return GenericRange.is(value, comparator);
    }

    public static <T> @NotNull GenericRange<T> open(T lowerBound, T upperBound, Comparator<? super T> comparator) {
        return GenericRange.open(lowerBound, upperBound, comparator);
    }

    public static <T> @NotNull GenericRange<T> closed(T lowerBound, T upperBound, Comparator<? super T> comparator) {
        return GenericRange.closed(lowerBound, upperBound, comparator);
    }

    public static <T> @NotNull GenericRange<T> openClosed(T lowerBound, T upperBound, Comparator<? super T> comparator) {
        return GenericRange.openClosed(lowerBound, upperBound, comparator);
    }

    public static <T> @NotNull GenericRange<T> closedOpen(T lowerBound, T upperBound, Comparator<? super T> comparator) {
        return GenericRange.closedOpen(lowerBound, upperBound, comparator);
    }

    public static <T> @NotNull GenericRange<T> greaterThan(T lowerBound, Comparator<? super T> comparator) {
        return GenericRange.greaterThan(lowerBound, comparator);
    }

    public static <T> @NotNull GenericRange<T> atLeast(T lowerBound, Comparator<? super T> comparator) {
        return GenericRange.atLeast(lowerBound, comparator);
    }

    public static <T> @NotNull GenericRange<T> lessThan(T upperBound, Comparator<? super T> comparator) {
        return GenericRange.lessThan(upperBound, comparator);
    }

    public static <T> @NotNull GenericRange<T> atMost(T upperBound, Comparator<? super T> comparator) {
        return GenericRange.atMost(upperBound, comparator);
    }
}
