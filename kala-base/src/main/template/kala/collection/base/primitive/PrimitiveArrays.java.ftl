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
package kala.collection.base.primitive;

import kala.Conditions;
import kala.annotations.StaticClass;
import kala.control.primitive.${Type}Option;
import kala.function.*;
import kala.index.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.*;
import java.util.function.IntFunction;
<#if IsSpecialized>
import java.util.stream.${Type}Stream;
</#if>

@StaticClass
public final class ${Type}Arrays {
    private ${Type}Arrays() {
    }

    public static final ${PrimitiveType}[] EMPTY = new ${PrimitiveType}[0];

    private static final IntFunction<${PrimitiveType}[]> GENERATOR = (IntFunction<${PrimitiveType}[]> & Serializable) ${PrimitiveType}[]::new;

    //region Static Factories

    @Contract(pure = true)
    public static @NotNull IntFunction<${PrimitiveType}[]> generator() {
        return GENERATOR;
    }

    public static ${PrimitiveType} @NotNull [] create(int length) {
        return new ${PrimitiveType}[length];
    }

    @Contract(value = "_ -> param1", pure = true)
    public static ${PrimitiveType} @NotNull [] of(${PrimitiveType}... values) {
        return values;
    }

    public static ${PrimitiveType} @NotNull [] from(${PrimitiveType} @NotNull [] values) {
        return values.clone();
    }

    public static ${PrimitiveType} @NotNull [] from(@NotNull ${Type}Traversable values) {
        return values.toArray();
    }

    public static ${PrimitiveType} @NotNull [] from(@NotNull ${Type}Iterator it) {
        return it.toArray();
    }

<#if IsSpecialized>
    public static ${PrimitiveType} @NotNull [] from(@NotNull ${Type}Stream stream) {
        return stream.toArray();
    }

</#if>
    //endregion

    public static @NotNull String className(${PrimitiveType} @NotNull [] array) {
        return "${PrimitiveType}[]";
    }

    public static @NotNull ${Type}Iterator iterator(${PrimitiveType} @NotNull [] array) {
        final int arrayLength = array.length; // implicit null check of array

        switch (arrayLength) {
            case 0:
                return ${Type}Iterator.empty();
            case 1:
                return ${Type}Iterator.of(array[0]);
        }
        return new Itr(array, 0, arrayLength);
    }

    public static @NotNull ${Type}Iterator iterator(${PrimitiveType} @NotNull [] array, int beginIndex) {
        final int arrayLength = array.length; // implicit null check of array
        Conditions.checkPositionIndex(beginIndex, arrayLength);

        switch (arrayLength - beginIndex) {
            case 0:
                return ${Type}Iterator.empty();
            case 1:
                return ${Type}Iterator.of(array[beginIndex]);
        }
        return new Itr(array, beginIndex, arrayLength);
    }

    public static @NotNull ${Type}Iterator iterator(${PrimitiveType} @NotNull [] array, int beginIndex, int endIndex) {
        final int arrayLength = array.length; // implicit null check of array
        Conditions.checkPositionIndices(beginIndex, endIndex, arrayLength);

        switch (endIndex - beginIndex) {
            case 0:
                return ${Type}Iterator.empty();
            case 1:
                return ${Type}Iterator.of(array[beginIndex]);
        }
        return new Itr(array, beginIndex, endIndex);
    }

<#if IsSpecialized>
    public static @NotNull Spliterator.Of${Type} spliterator(${PrimitiveType} @NotNull [] array) {
        return Arrays.spliterator(array);
    }

    public static @NotNull ${Type}Stream stream(${PrimitiveType} @NotNull [] array) {
        return Arrays.stream(array);
    }

    public static @NotNull ${Type}Stream parallelStream(${PrimitiveType} @NotNull [] array) {
        return Arrays.stream(array).parallel();
    }

</#if>
    //region Size Info

    public static boolean isEmpty(${PrimitiveType} @NotNull [] array) {
        return array.length == 0;
    }

    public static int size(${PrimitiveType} @NotNull [] array) {
        return array.length;
    }

    public static int knownSize(${PrimitiveType} @NotNull [] array) {
        return array.length;
    }

    //endregion

    //region Positional Access Operations

    public static boolean isDefineAt(${PrimitiveType} @NotNull [] array, int index) {
        return index >= 0 && index <= array.length;
    }

    public static ${PrimitiveType} get(${PrimitiveType} @NotNull [] array, int index) {
        try {
            return array[index];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException(e.getMessage());
        }
    }

    public static @Nullable ${WrapperType} getOrNull(${PrimitiveType} @NotNull [] array, int index) {
        return index >= 0 && index <= array.length
                ? array[index]
                : null;
    }

    public static @NotNull ${Type}Option getOption(${PrimitiveType} @NotNull [] array, int index) {
        return index >= 0 && index <= array.length
                ? ${Type}Option.some(array[index])
                : ${Type}Option.none();
    }

    public static void set(${PrimitiveType} @NotNull [] array, int index, ${PrimitiveType} value) {
        try {
            array[index] = value;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException(e.getMessage());
        }
    }

    //endregion

    //region Reversal Operations

    public static ${PrimitiveType} @NotNull [] reversed(${PrimitiveType} @NotNull [] array) {
        final int length = array.length;
        if (length == 0) {
            return array;
        }

        ${PrimitiveType}[] res = new ${PrimitiveType}[length];

        for (int i = 0; i < length; i++) {
            res[i] = array[length - i - 1];
        }
        return res;
    }

    public static @NotNull ${Type}Iterator reverseIterator(${PrimitiveType} @NotNull [] array) {
        final int length = array.length;
        switch (length) {
            case 0:
                return ${Type}Iterator.empty();
            case 1:
                return ${Type}Iterator.of(array[0]);
        }
        return new ReverseItr(array, length - 1);
    }

    //endregion

    public static void shuffle(${PrimitiveType} @NotNull [] array) {
        shuffle(array, ThreadLocalRandom.current());
    }

    public static void shuffle(${PrimitiveType} @NotNull [] array, @NotNull Random random) {
        final int size = array.length;
        if (size <= 1) {
            return;
        }

        for (int i = size; i > 1; i--) {
            final int k = random.nextInt(i);
            final ${PrimitiveType} tmp = array[i - 1];
            array[i - 1] = array[k];
            array[k] = tmp;
        }
    }

    public static void sort(${PrimitiveType} @NotNull [] array) {
<#if Type == "Boolean">
        if (array.length != 0)
            sort(array, 0, array.length);
<#else>
        Arrays.sort(array);
</#if>
    }

    public static void sort(${PrimitiveType} @NotNull [] array, int beginIndex, int endIndex) {
<#if Type == "Boolean">
        Conditions.checkPositionIndices(beginIndex, endIndex, array.length);
        int trueCount = 0;
        int falseCount;
        for (int i = beginIndex; i < endIndex; i++) {
            if (array[i]) trueCount++;
        }
        falseCount = endIndex - beginIndex - trueCount;

        if (trueCount == 0 || falseCount == 0) return;

        for (int i = beginIndex; i < falseCount; i++) {
            array[i] = false;
        }

        for (int i = beginIndex + falseCount; i < endIndex; i++) {
            array[i] = true;
        }
<#else>
        Arrays.sort(array, beginIndex, endIndex);
</#if>
    }

    //region Element Retrieval Operations

    public static @NotNull ${Type}Option find(${PrimitiveType} @NotNull [] array, @NotNull ${Type}Predicate predicate) {
        for (${PrimitiveType} e : array) {
            if (predicate.test(e)) {
                return ${Type}Option.some(e);
            }
        }
        return ${Type}Option.none();
    }

    public static ${PrimitiveType} first(${PrimitiveType} @NotNull [] array) {
        try {
            return array[0];
        } catch (ArrayIndexOutOfBoundsException ignored) {
            throw new NoSuchElementException();
        }
    }

    public static ${PrimitiveType} last(${PrimitiveType} @NotNull [] array) {
        final int length = array.length;
        if (length == 0) {
            throw new NoSuchElementException();
        }
        return array[length - 1];
    }

    //endregion

    //region Element Conditions

    public static boolean contains(${PrimitiveType} @NotNull [] array, ${PrimitiveType} value) {
        for (${PrimitiveType} o : array) {
            if (${PrimitiveEquals("value", "o")}) {
                return true;
            }
        }

        return false;
    }

    public static boolean containsAll(${PrimitiveType} @NotNull [] array, ${PrimitiveType} @NotNull [] values) {
        for (${PrimitiveType} value : values) {
            if (!contains(array, value)) {
                return false;
            }
        }
        return true;
    }

    public static boolean containsAll(${PrimitiveType} @NotNull [] array, @NotNull ${Type}Traversable values) {
        ${Type}Iterator it = values.iterator();
        while (it.hasNext()) {
            ${PrimitiveType} value = it.next${Type}();
            if (!contains(array, value)) {
                return false;
            }
        }
        return true;
    }

    public static boolean anyMatch(${PrimitiveType} @NotNull [] array, @NotNull ${Type}Predicate predicate) {
        for (${PrimitiveType} e : array) {
            if (predicate.test(e)) {
                return true;
            }
        }
        return false;
    }

    public static boolean allMatch(${PrimitiveType} @NotNull [] array, @NotNull ${Type}Predicate predicate) {
        for (${PrimitiveType} e : array) {
            if (!predicate.test(e)) {
                return false;
            }
        }
        return true;
    }

    public static boolean noneMatch(${PrimitiveType} @NotNull [] array, @NotNull ${Type}Predicate predicate) {
        for (${PrimitiveType} e : array) {
            if (predicate.test(e)) {
                return false;
            }
        }
        return true;
    }

    //endregion

    //region Search Operations

    @Contract(pure = true)
    public static int indexOf(${PrimitiveType} @NotNull [] array, ${PrimitiveType} value) {
        final int length = array.length;

        for (int i = 0; i < length; i++) {
            if (${PrimitiveEquals("value", "array[i]")}) {
                return i;
            }
        }
        return -1;
    }

    @Contract(pure = true)
    public static int indexOf(${PrimitiveType} @NotNull [] array, ${PrimitiveType} value, int beginIndex) {
        final int length = array.length;

        if (beginIndex >= length) {
            return -1;
        }

        for (int i = Math.max(beginIndex, 0); i < length; i++) {
            if (${PrimitiveEquals("value", "array[i]")}) {
                return i;
            }
        }

        return -1;
    }

    @Contract(pure = true)
    public static int indexWhere(${PrimitiveType} @NotNull [] array, @NotNull ${Type}Predicate predicate) {
        final int length = array.length;
        for (int i = 0; i < length; i++) {
            if (predicate.test(array[i])) {
                return i;
            }
        }
        return -1;
    }

    @Contract(pure = true)
    public static int indexWhere(${PrimitiveType} @NotNull [] array, @NotNull ${Type}Predicate predicate, int beginIndex) {
        final int length = array.length;

        if (beginIndex >= length) {
            return -1;
        }

        for (int i = Math.max(beginIndex, 0); i < length; i++) {
            if (predicate.test(array[i])) {
                return i;
            }
        }
        return -1;
    }

    @Contract(pure = true)
    public static int lastIndexOf(${PrimitiveType} @NotNull [] array, ${PrimitiveType} value) {
        for (int i = array.length - 1; i >= 0; i--) {
            if (${PrimitiveEquals("value", "array[i]")}) {
                return i;
            }
        }

        return -1;
    }

    @Contract(pure = true)
    public static int lastIndexOf(${PrimitiveType} @NotNull [] array, ${PrimitiveType} value, int endIndex) {
        if (endIndex < 0) {
            return -1;
        }

        for (int i = endIndex; i >= 0; i--) {
            if (${PrimitiveEquals("value", "array[i]")}) {
                return i;
            }
        }

        return -1;
    }

    @Contract(pure = true)
    public static int lastIndexWhere(${PrimitiveType} @NotNull [] array, @NotNull ${Type}Predicate predicate) {
        for (int i = array.length - 1; i >= 0; i--) {
            if (predicate.test(array[i])) {
                return i;
            }
        }

        return -1;
    }

    @Contract(pure = true)
    public static int lastIndexWhere(${PrimitiveType} @NotNull [] array, @NotNull ${Type}Predicate predicate, int endIndex) {
        if (endIndex < 0) {
            return -1;
        }

        for (int i = endIndex; i >= 0; i--) {
            if (predicate.test(array[i])) {
                return i;
            }
        }

        return -1;
    }

    //endregion

    //region Aggregate Operations

    @Contract(pure = true)
    public static ${PrimitiveType} max(${PrimitiveType} @NotNull [] array) {
        final int length = array.length; // implicit null check of array
        if (length == 0) {
            throw new NoSuchElementException();
        }

<#if Type == "Boolean">
        for (boolean e : array) {
            if (e) return true;
        }

        return false;
<#else>
        ${PrimitiveType} e = array[0];
        for (int i = 1; i < length; i++) {
            ${PrimitiveType} v = array[i];
            if (${WrapperType}.compare(e, v) < 0) {
                e = v;
            }
        }
        return e;
</#if>
    }

    @Contract(pure = true)
    public static @Nullable ${WrapperType} maxOrNull(${PrimitiveType} @NotNull [] array) {
        return array.length != 0 ? max(array) : null;
    }

    @Contract(pure = true)
    public static @NotNull ${Type}Option maxOption(${PrimitiveType} @NotNull [] array) {
        return array.length != 0 ? ${Type}Option.some(max(array)) : ${Type}Option.none();
    }

    @Contract(pure = true)
    public static ${PrimitiveType} min(${PrimitiveType} @NotNull [] array) {
        final int length = array.length; // implicit null check of array
        if (length == 0) {
            throw new NoSuchElementException();
        }

<#if Type == "Boolean">
        for (boolean e : array) {
            if (!e) return false;
        }

        return true;
<#else>
    ${PrimitiveType} e = array[0];
        for (int i = 1; i < length; i++) {
            ${PrimitiveType} v = array[i];
            if (${WrapperType}.compare(e, v) > 0) {
                e = v;
            }
        }
        return e;
</#if>
    }

    @Contract(pure = true)
    public static @Nullable ${WrapperType} minOrNull(${PrimitiveType} @NotNull [] array) {
        return array.length != 0 ? min(array) : null;
    }

    @Contract(pure = true)
    public static @NotNull ${Type}Option minOption(${PrimitiveType} @NotNull [] array) {
        return array.length != 0 ? ${Type}Option.some(min(array)) : ${Type}Option.none();
    }
    
    @Contract(pure = true)
    public static ${PrimitiveType} fold(${PrimitiveType} @NotNull [] array, ${PrimitiveType} zero, @NotNull ${Type}BinaryOperator op) {
        return foldLeft(array, zero, op);
    }

    @Contract(pure = true)
    public static ${PrimitiveType} foldLeft(${PrimitiveType} @NotNull [] array, ${PrimitiveType} zero, @NotNull ${Type}BinaryOperator op) {
        for (${PrimitiveType} e : array) {
            zero = op.applyAs${Type}(zero, e);
        }
        return zero;
    }

    @Contract(pure = true)
    public static <U> U foldLeftToObj(${PrimitiveType} @NotNull [] array, U zero, @NotNull Obj${Type}BiFunction<U, U> op) {
        for (${PrimitiveType} e : array) {
            zero = op.apply(zero, e);
        }
        return zero;
    }

    @Contract(pure = true)
    public static ${PrimitiveType} foldRight(${PrimitiveType} @NotNull [] array, ${PrimitiveType} zero, @NotNull ${Type}BinaryOperator op) {
        for (int i = array.length - 1; i >= 0; i--) {
            zero = op.applyAs${Type}(array[i], zero);
        }
        return zero;
    }

    public static <U> U foldRightToObj(${PrimitiveType} @NotNull [] array, U zero, @NotNull ${Type}ObjBiFunction<U, U> op) {
        for (int i = array.length - 1; i >= 0; i--) {
            zero = op.apply(array[i], zero);
        }
        return zero;
    }

    @Contract(pure = true)
    public static ${PrimitiveType} reduce(${PrimitiveType} @NotNull [] array, @NotNull ${Type}BinaryOperator op) {
        return reduceLeft(array, op);
    }

    @Contract(pure = true)
    public static @NotNull ${Type}Option reduceOption(${PrimitiveType} @NotNull [] array, @NotNull ${Type}BinaryOperator op) {
        return reduceLeftOption(array, op);
    }

    @Contract(pure = true)
    public static ${PrimitiveType} reduceLeft(${PrimitiveType} @NotNull [] array, @NotNull ${Type}BinaryOperator op) {
        final int length = array.length;

        if (length == 0) {
            throw new NoSuchElementException();
        }

        ${PrimitiveType} e = array[0];
        for (int i = 1; i < length; i++) {
            e = op.applyAs${Type}(e, array[i]);
        }
        return e;
    }

    @Contract(pure = true)
    public static @Nullable ${WrapperType} reduceLeftOrNull(${PrimitiveType} @NotNull [] array, @NotNull ${Type}BinaryOperator op) {
        return array.length != 0 ? reduceLeft(array, op) : null;
    }

    @Contract(pure = true)
    public static @NotNull ${Type}Option reduceLeftOption(${PrimitiveType} @NotNull [] array, @NotNull ${Type}BinaryOperator op) {
        return array.length != 0 ? ${Type}Option.some(reduceLeft(array, op)) : ${Type}Option.none();
    }

    @Contract(pure = true)
    public static ${PrimitiveType} reduceRight(${PrimitiveType} @NotNull [] array, @NotNull ${Type}BinaryOperator op) {
        final int length = array.length;

        if (length == 0) {
            throw new NoSuchElementException();
        }

        ${PrimitiveType} e = array[length - 1];
        for (int i = length - 2; i >= 0; i--) {
            e = op.applyAs${Type}(array[i], e);
        }
        return e;
    }

    @Contract(pure = true)
    public static @Nullable ${WrapperType} reduceRightOrNull(${PrimitiveType} @NotNull [] array, @NotNull ${Type}BinaryOperator op) {
        return array.length != 0 ? reduceRight(array, op) : null;
    }

    @Contract(pure = true)
    public static @NotNull ${Type}Option reduceRightOption(${PrimitiveType} @NotNull [] array, @NotNull ${Type}BinaryOperator op) {
        return array.length != 0 ? ${Type}Option.some(reduceRight(array, op)) : ${Type}Option.none();
    }

    //endregion

    //region String Representation

    public static <A extends Appendable> @NotNull A joinTo(
            ${PrimitiveType} @NotNull [] array,
            @NotNull A buffer
    ) {
        return joinTo(array, buffer, ", ", "", "" );
    }

    public static <A extends Appendable> @NotNull A joinTo(
            ${PrimitiveType} @NotNull [] array,
            @NotNull A buffer,
            CharSequence separator
    ) {
        return joinTo(array, buffer, separator, "", "" );
    }

    public static <A extends Appendable> @NotNull A joinTo(
            ${PrimitiveType} @NotNull [] array,
            @NotNull A buffer,
            CharSequence separator, CharSequence prefix, CharSequence postfix
    ) {
        final int length = array.length;
        try {
            if (length == 0) {
                buffer.append(prefix).append(postfix);
                return buffer;
            }
            buffer.append(prefix).append(String.valueOf(array[0]));
            for (int i = 1; i < length; i++) {
                buffer.append(separator);
                buffer.append(Objects.toString(array[i]));
            }
            buffer.append(postfix);
            return buffer;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static <A extends Appendable> @NotNull A joinTo(
            ${PrimitiveType} @NotNull [] array,
            @NotNull A buffer,
            @NotNull ${Type}Function<? extends CharSequence> transform
    ) {
        return joinTo(array, buffer, ", ", "", "", transform);
    }

    public static <A extends Appendable> @NotNull A joinTo(
            ${PrimitiveType} @NotNull [] array,
            @NotNull A buffer,
            CharSequence separator,
            @NotNull ${Type}Function<? extends CharSequence> transform
    ) {
        return joinTo(array, buffer, separator, "", "", transform);
    }

    public static <A extends Appendable> @NotNull A joinTo(
            ${PrimitiveType} @NotNull [] array,
            @NotNull A buffer,
            CharSequence separator, CharSequence prefix, CharSequence postfix,
            @NotNull ${Type}Function<? extends CharSequence> transform
    ) {
        final int length = array.length;
        try {
            if (length == 0) {
                buffer.append(prefix).append(postfix);
                return buffer;
            }
            buffer.append(prefix).append(transform.apply(array[0]));
            for (int i = 1; i < length; i++) {
                buffer.append(separator);
                buffer.append(transform.apply(array[i]));
            }
            buffer.append(postfix);
            return buffer;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static @NotNull String joinToString(
            ${PrimitiveType} @NotNull [] array
    ) {
        return joinTo(array, new StringBuilder()).toString();
    }

    public static @NotNull String joinToString(
            ${PrimitiveType} @NotNull [] array,
            CharSequence separator
    ) {
        return joinTo(array, new StringBuilder(), separator).toString();
    }

    public static @NotNull String joinToString(
            ${PrimitiveType} @NotNull [] array,
            CharSequence separator, CharSequence prefix, CharSequence postfix
    ) {
        return joinTo(array, new StringBuilder(), separator, prefix, postfix).toString();
    }

    public static @NotNull String joinToString(
            ${PrimitiveType} @NotNull [] array,
            @NotNull ${Type}Function<? extends CharSequence> transform
    ) {
        return joinTo(array, new StringBuilder(), transform).toString();
    }

    public static @NotNull String joinToString(
            ${PrimitiveType} @NotNull [] array,
            CharSequence separator,
            @NotNull ${Type}Function<? extends CharSequence> transform
    ) {
        return joinTo(array, new StringBuilder(), separator, transform).toString();
    }

    public static @NotNull String joinToString(
            ${PrimitiveType} @NotNull [] array,
            CharSequence separator, CharSequence prefix, CharSequence postfix,
            @NotNull ${Type}Function<? extends CharSequence> transform
    ) {
        return joinTo(array, new StringBuilder(), separator, prefix, postfix, transform).toString();
    }

    //endregion

    private static final class Itr extends Abstract${Type}Iterator {
        private final ${PrimitiveType} @NotNull [] array;
        private final int endIndex;

        private int index;

        Itr(${PrimitiveType} @NotNull [] array, int beginIndex, int endIndex) {
            this.array = array;
            this.index = beginIndex;
            this.endIndex = endIndex;
        }

        @Override
        public boolean hasNext() {
            return index < endIndex;
        }

        @Override
        public ${PrimitiveType} next${Type}() {
            if (index >= endIndex) {
                throw new NoSuchElementException(this + ".next()" );
            }
            return array[index++];
        }
    }

    private static final class ReverseItr extends Abstract${Type}Iterator {
        private final ${PrimitiveType} @NotNull [] array;

        private int index;

        ReverseItr(${PrimitiveType} @NotNull [] array, int index) {
            this.array = array;
            this.index = index;
        }

        ReverseItr(${PrimitiveType} @NotNull [] array) {
            this(array, array.length - 1);
        }

        @Override
        public boolean hasNext() {
            return index >= 0;
        }

        @Override
        public ${PrimitiveType} next${Type}() {
            try {
                return array[index--];
            } catch (ArrayIndexOutOfBoundsException ignored) {
                throw new NoSuchElementException();
            }
        }
    }
}
