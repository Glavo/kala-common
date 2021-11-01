package kala.collection.base.primitive;

import kala.Conditions;
import kala.annotations.StaticClass;
import kala.control.primitive.${Type}Option;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntFunction;
<#if IsSpecialized>
import java.util.function.${Type}Predicate;
import java.util.stream.${Type}Stream;
<#else>
import kala.function.${Type}Predicate;
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

    //region Collection Operations

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
    //endregion

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
            if (value == o) {
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
        for (${PrimitiveType} value : values) {
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
