package org.glavo.kala.collection.base;

import org.glavo.kala.control.Option;
import org.glavo.kala.annotations.Covariant;
import org.glavo.kala.annotations.StaticClass;
import org.glavo.kala.factory.CollectionFactory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.function.*;

@StaticClass
@SuppressWarnings("unchecked")
public final class JavaArray {
    private JavaArray() {
    }

    public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

    //region Static Factories

    @Contract(pure = true)
    public static @NotNull <E> IntFunction<E[]> generator(@NotNull Class<E> type) {
        Objects.requireNonNull(type);
        return length -> (E[]) Array.newInstance(type, length);
    }

    @Contract(pure = true)
    public static <E> @NotNull CollectionFactory<E, ?, E[]> factory(@NotNull Class<E> type) {
        return factory(generator(type));
    }

    @Contract(pure = true)
    public static <E> @NotNull CollectionFactory<E, ?, E[]> factory(@NotNull IntFunction<E[]> generator) {
        Objects.requireNonNull(generator);
        return new JavaArray.Factory<>(generator);
    }

    @Contract(value = "_ -> param1", pure = true)
    public static <E> E @NotNull [] of(E... values) {
        return values;
    }

    public static <E> E @NotNull [] from(E @NotNull [] values) {
        return values.clone();
    }

    public static <E> E @NotNull [] from(@NotNull IntFunction<E[]> generator, @NotNull Iterable<? extends E> values) {
        Objects.requireNonNull(values);
        Objects.requireNonNull(generator);

        if (values instanceof Collection<?>) {
            Collection<E> collection = (Collection<E>) values;
            int size = collection.size();
            E[] arr = generator.apply(size);
            int idx = 0;
            for (E e : collection) {
                arr[idx++] = e;
            }
            return arr;
        } else if (values instanceof Traversable<?>) {
            return ((Traversable<E>) values).toArray(generator);
        } else {
            ArrayList<E> tmp = new ArrayList<>();
            for (E e : values) {
                tmp.add(e);
            }
            return tmp.toArray(generator.apply(tmp.size()));
        }
    }

    public static <E> E @NotNull [] fill(@NotNull IntFunction<E[]> generator, int n, E value) {
        if (n <= 0) {
            return generator.apply(0);
        }
        E[] ans = generator.apply(n);
        if (value != null) {
            Arrays.fill(ans, value);
        }
        return ans;
    }

    public static <E> E @NotNull [] fill(@NotNull IntFunction<E[]> generator, int n, @NotNull Supplier<? extends E> supplier) {
        if (n <= 0) {
            return generator.apply(0);
        }
        E[] ans = generator.apply(n);
        for (int i = 0; i < n; i++) {
            ans[i] = supplier.get();
        }
        return ans;
    }

    public static <E> E @NotNull [] fill(@NotNull IntFunction<E[]> generator, int n, @NotNull IntFunction<? extends E> supplier) {
        if (n <= 0) {
            return generator.apply(0);
        }
        E[] ans = generator.apply(n);
        for (int i = 0; i < n; i++) {
            ans[i] = supplier.apply(i);
        }
        return ans;
    }

    public static <E> E @NotNull [] wrapInArray(E element) {
        Class<?> cls = element == null ? Object.class : element.getClass();
        Object arr = Array.newInstance(cls, 1);
        Array.set(arr, 0, element);
        return (E[]) arr;
    }

    //endregion

    //region Collection Operations

    public static <E> @NotNull Iterator<E> iterator(E @NotNull [] array) {
        final int arrayLength = array.length; // implicit null check of array
        if (arrayLength == 0) {
            return Iterators.empty();
        }
        if (arrayLength == 1) {
            return Iterators.of(array[0]);
        }
        return new Itr<>(array, 0, arrayLength);
    }

    public static <E> @NotNull Iterator<E> iterator(E @NotNull [] array, int from) {
        final int arrayLength = array.length; // implicit null check of array
        if (from < 0 || from > arrayLength) {
            throw new IndexOutOfBoundsException();
        }
        final int len = arrayLength - from;

        if (len == 0) {
            return Iterators.empty();
        }
        if (len == 1) {
            return Iterators.of(array[0]);
        }
        return new Itr<>(array, from, arrayLength);
    }

    public static <E> @NotNull Iterator<E> iterator(E @NotNull [] array, int from, int to) {
        final int arrayLength = array.length; // implicit null check of array
        if (from < 0 || from > arrayLength) {
            throw new IndexOutOfBoundsException();
        }
        if (to < from || to > arrayLength) {
            throw new IndexOutOfBoundsException();
        }
        final int len = to - from;

        if (len == 0) {
            return Iterators.empty();
        }
        if (len == 1) {
            return Iterators.of(array[0]);
        }
        return new Itr<>(array, from, to);
    }

    //endregion

    //region Reversal Operations

    public static <E> @NotNull Iterator<E> reverseIterator(E @NotNull [] array) {
        final int length = array.length;
        if (length == 0) {
            return Iterators.empty();
        }
        if (length == 1) {
            return Iterators.of(array[0]);
        }
        return new ReverseItr<>(array, length - 1);
    }

    //endregion

    //region Search Operations

    @Contract(pure = true)
    public static int indexOf(Object @NotNull [] array, Object value) {
        final int length = array.length;

        if (value == null) {
            for (int i = 0; i < length; i++) {
                if (array[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < length; i++) {
                if (value.equals(array[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Contract(pure = true)
    public static int indexOf(Object @NotNull [] array, Object value, int from) {
        final int length = array.length;

        if (from >= length) {
            return -1;
        }

        if (value == null) {
            for (int i = Math.max(from, 0); i < length; i++) {
                if (array[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = Math.max(from, 0); i < length; i++) {
                if (value.equals(array[i])) {
                    return i;
                }
            }
        }

        return -1;
    }

    @Contract(pure = true)
    public static <E> int indexWhere(E @NotNull [] array, @NotNull Predicate<? super E> predicate) {
        final int length = array.length;
        for (int i = 0; i < length; i++) {
            if (predicate.test(array[i])) {
                return i;
            }
        }
        return -1;
    }

    @Contract(pure = true)
    public static <E> int indexWhere(E @NotNull [] array, @NotNull Predicate<? super E> predicate, int from) {
        final int length = array.length;

        if (from >= length) {
            return -1;
        }

        for (int i = Math.max(from, 0); i < length; i++) {
            if (predicate.test(array[i])) {
                return i;
            }
        }
        return -1;
    }

    @Contract(pure = true)
    public static int lastIndexOf(Object @NotNull [] array, Object value) {
        if (value == null) {
            for (int i = array.length - 1; i >= 0; i--) {
                if (array[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = array.length - 1; i >= 0; i--) {
                if (value.equals(array[i])) {
                    return i;
                }
            }
        }

        return -1;
    }

    @Contract(pure = true)
    public static int lastIndexOf(Object @NotNull [] array, Object value, int end) {
        if (end < 0) {
            return -1;
        }

        if (value == null) {
            for (int i = end; i >= 0; i--) {
                if (array[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = end; i >= 0; i--) {
                if (value.equals(array[i])) {
                    return i;
                }
            }
        }

        return -1;
    }

    @Contract(pure = true)
    public static <E> int lastIndexWhere(E @NotNull [] array, @NotNull Predicate<? super E> predicate) {
        for (int i = array.length - 1; i >= 0; i--) {
            if (predicate.test(array[i])) {
                return i;
            }
        }

        return -1;
    }

    @Contract(pure = true)
    public static <E> int lastIndexWhere(E @NotNull [] array, @NotNull Predicate<? super E> predicate, int end) {
        if (end < 0) {
            return -1;
        }

        for (int i = end; i >= 0; i--) {
            if (predicate.test(array[i])) {
                return i;
            }
        }

        return -1;
    }


    //endregion

    //region Misc Operations

    public static <E> E @NotNull [] @NotNull [] chunked(E @NotNull [] array, int size) {
        if (size <= 0) {
            throw new IllegalArgumentException();
        }

        int arrayLength = array.length;
        if (arrayLength == 0) {
            return (E[][]) Array.newInstance(array.getClass(), 0);
        }

        int x = arrayLength / size;
        int r = arrayLength % size;

        E[][] res = (E[][]) Array.newInstance(array.getClass(), r == 0 ? x : x + 1);

        for (int i = 0; i < x; i++) {
            res[i] = Arrays.copyOfRange(array, i * size, (i + 1) * size);
        }
        if (r != 0) {
            res[x] = Arrays.copyOfRange(array, x * size, x * size + r);
        }

        return res;
    }

    public static <E> E @NotNull [] @NotNull [] windowed(E @NotNull [] array, int size) {
        return windowed(array, size, 1, false);
    }

    public static <E> E @NotNull [] @NotNull [] windowed(E @NotNull [] array, int size, int step) {
        return windowed(array, size, step, false);
    }

    public static <E> E @NotNull [] @NotNull [] windowed(E @NotNull [] array, int size, int step, boolean partialWindows) {
        final int arrayLength = array.length;
        if (size <= 0 || step <= 0) {
            if (size == step) {
                throw new IllegalArgumentException("size " + size + " must be greater than zero.");
            } else {
                throw new IllegalArgumentException("Both size " + size + " and step " + step + " must be greater than zero.");
            }
        }

        final int resultCapacity = arrayLength / step + (arrayLength % step == 0 ? 0 : 1);
        E[][] ans = (E[][]) Array.newInstance(array.getClass(), resultCapacity);
        int ansi = 0;
        int index = 0;
        while (index < arrayLength) {
            int windowSize = Integer.min(size, arrayLength - index);
            if (windowSize < size && !partialWindows) {
                break;
            }
            ans[ansi++] = Arrays.copyOfRange(array, index, windowSize + index);
            index += step;
        }
        return Arrays.copyOf(ans, ansi);
    }

    //endregion

    //region Aggregate Operations

    @Contract(pure = true)
    public static <E extends Comparable<E>> E max(E @NotNull [] array) {
        return (E) Unsafe.max(array);
    }

    @Contract(pure = true)
    public static <E> E max(E @NotNull [] array, @NotNull Comparator<? super E> comparator) {
        final int length = array.length;
        E e = array[0];
        for (int i = 1; i < length; i++) {
            E v = array[i];
            if (comparator.compare(e, v) < 0) {
                e = v;
            }
        }
        return e;
    }

    @Contract(pure = true)
    public static <E extends Comparable<E>> @Nullable E maxOrNull(E @NotNull [] array) {
        return (E) Unsafe.maxOrNull(array);
    }

    @Contract(pure = true)
    public static <E> @Nullable E maxOrNull(E @NotNull [] array, @NotNull Comparator<? super E> comparator) {
        final int length = array.length;
        if (length == 0) {
            return null;
        }
        E e = array[0];
        for (int i = 1; i < length; i++) {
            E v = array[i];
            if (comparator.compare(e, v) < 0) {
                e = v;
            }
        }
        return e;
    }

    @Contract(pure = true)
    public static <E extends Comparable<E>> @NotNull Option<E> maxOption(E @NotNull [] array) {
        if (array.length == 0) {
            return Option.none();
        }
        return Option.some((E) Unsafe.max(array));
    }

    @Contract(pure = true)
    public static <E> @NotNull Option<E> maxOption(E @NotNull [] array, @NotNull Comparator<? super E> comparator) {
        if (array.length == 0) {
            return Option.none();
        }
        return Option.some(max(array, comparator));
    }

    @Contract(pure = true)
    public static <E extends Comparable<E>> E min(E @NotNull [] array) {
        return (E) Unsafe.min(array);
    }

    @Contract(pure = true)
    public static <E> E min(E @NotNull [] array, @NotNull Comparator<? super E> comparator) {
        final int length = array.length;
        E e = array[0];
        for (int i = 1; i < length; i++) {
            E v = array[i];
            if (comparator.compare(e, v) > 0) {
                e = v;
            }
        }
        return e;
    }

    @Contract(pure = true)
    public static <E extends Comparable<E>> @Nullable E minOrNull(E @NotNull [] array) {
        return (E) Unsafe.minOrNull(array);
    }

    @Contract(pure = true)
    public static <E> @Nullable E minOrNull(E @NotNull [] array, @NotNull Comparator<? super E> comparator) {
        final int length = array.length;
        if (length == 0) {
            return null;
        }

        E e = array[0];
        for (int i = 1; i < length; i++) {
            E v = array[i];
            if (comparator.compare(e, v) > 0) {
                e = v;
            }
        }
        return e;
    }

    @Contract(pure = true)
    public static <E extends Comparable<E>> @NotNull Option<E> minOption(E @NotNull [] array) {
        if (array.length == 0) {
            return Option.none();
        }
        return Option.some((E) Unsafe.min(array));
    }

    @Contract(pure = true)
    public static <E> @NotNull Option<E> minOption(E @NotNull [] array, @NotNull Comparator<? super E> comparator) {
        if (array.length == 0) {
            return Option.none();
        }
        return Option.some(min(array, comparator));
    }

    @Contract(pure = true)
    public static <E> E fold(
            E @NotNull [] array, E zero, @NotNull BiFunction<? super E, ? super E, ? extends E> op
    ) {
        return foldLeft(array, zero, op);
    }

    @Contract(pure = true)
    public static <E, U> U foldLeft(
            E @NotNull [] array, U zero, @NotNull BiFunction<? super U, ? super E, ? extends U> op
    ) {
        for (E e : array) {
            zero = op.apply(zero, e);
        }
        return zero;
    }

    @Contract(pure = true)
    public static <E, U> U foldRight(
            E @NotNull [] array, U zero, @NotNull BiFunction<? super E, ? super U, ? extends U> op
    ) {
        for (int i = array.length - 1; i >= 0; i--) {
            zero = op.apply(array[i], zero);
        }
        return zero;
    }

    @Contract(pure = true)
    public static <E> E reduce(E @NotNull [] array, @NotNull BiFunction<? super E, ? super E, ? extends E> op) {
        return reduceLeft(array, op);
    }

    @Contract(pure = true)
    public static <E> @NotNull Option<E> reduceOption(E @NotNull [] array, @NotNull BiFunction<? super E, ? super E, ? extends E> op) {
        return reduceLeftOption(array, op);
    }

    @Contract(pure = true)
    public static <E> E reduceLeft(E @NotNull [] array, @NotNull BiFunction<? super E, ? super E, ? extends E> op) {
        final int length = array.length;

        if (length == 0) {
            throw new NoSuchElementException();
        }

        E e = array[0];
        for (int i = 1; i < length; i++) {
            e = op.apply(e, array[i]);
        }
        return e;
    }

    @Contract(pure = true)
    public static <E> @NotNull Option<E> reduceLeftOption(E @NotNull [] array, @NotNull BiFunction<? super E, ? super E, ? extends E> op) {
        final int length = array.length;

        if (length == 0) {
            return Option.none();
        }

        E e = array[0];
        for (int i = 1; i < length; i++) {
            e = op.apply(e, array[i]);
        }
        return Option.some(e);
    }

    @Contract(pure = true)
    public static <E> E reduceRight(E @NotNull [] array, @NotNull BiFunction<? super E, ? super E, ? extends E> op) {
        final int length = array.length;

        if (length == 0) {
            throw new NoSuchElementException();
        }

        E e = array[length - 1];
        for (int i = length - 2; i >= 0; i--) {
            e = op.apply(array[i], e);
        }
        return e;
    }

    @Contract(pure = true)
    public static <E> @NotNull Option<E> reduceRightOption(E @NotNull [] array, @NotNull BiFunction<? super E, ? super E, ? extends E> op) {
        final int length = array.length;

        if (length == 0) {
            return Option.none();
        }

        E e = array[length - 1];
        for (int i = length - 2; i >= 0; i--) {
            e = op.apply(array[i], e);
        }
        return Option.some(e);
    }

    //endregion

    //region Element Conditions

    public static <E> boolean anyMatch(E @NotNull [] array, @NotNull Predicate<? super E> predicate) {
        for (Object e : array) {
            if (predicate.test((E) e)) {
                return true;
            }
        }
        return false;
    }

    public static <E> boolean allMatch(E @NotNull [] array, @NotNull Predicate<? super E> predicate) {
        for (Object e : array) {
            if (!predicate.test((E) e)) {
                return false;
            }
        }
        return true;
    }

    public static <E> boolean noneMatch(E @NotNull [] array, @NotNull Predicate<? super E> predicate) {
        for (Object e : array) {
            if (predicate.test((E) e)) {
                return false;
            }
        }
        return true;
    }

    //endregion

    //region String Representation

    public static <A extends Appendable> @NotNull A joinTo(
            @NotNull Object[] array,
            @NotNull A buffer
    ) {
        return joinTo(array, buffer, ", ", "", "");
    }

    public static <A extends Appendable> @NotNull A joinTo(
            @NotNull Object[] array,
            @NotNull A buffer,
            CharSequence separator
    ) {
        return joinTo(array, buffer, separator, "", "");
    }

    public static <A extends Appendable> @NotNull A joinTo(
            @NotNull Object[] array,
            @NotNull A buffer,
            CharSequence separator, CharSequence prefix, CharSequence postfix
    ) {
        final int length = array.length;
        try {
            if (length == 0) {
                buffer.append(prefix).append(postfix);
                return buffer;
            }
            buffer.append(prefix).append(Objects.toString(array[0]));
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

    public static @NotNull String joinToString(
            @NotNull Object[] array
    ) {
        return joinTo(array, new StringBuilder()).toString();
    }

    public static @NotNull String joinToString(
            @NotNull Object[] array,
            CharSequence separator
    ) {
        return joinTo(array, new StringBuilder(), separator).toString();
    }

    public static @NotNull String joinToString(
            @NotNull Object[] array,
            CharSequence separator, CharSequence prefix, CharSequence postfix
    ) {
        return joinTo(array, new StringBuilder(), separator, prefix, postfix).toString();
    }

    //endregion

    public static final class Unsafe {
        public static Object max(Object @NotNull [] array) {
            final int length = array.length;
            if (length == 0) {
                throw new NoSuchElementException();
            }

            Object e = array[0];
            for (int i = 1; i < length; i++) {
                Object v = array[i];
                if (((Comparable<Object>) e).compareTo(v) < 0) {
                    e = v;
                }
            }
            return e;
        }

        @Nullable
        public static Object maxOrNull(Object @NotNull [] array) {
            final int length = array.length;
            if (length == 0) {
                return null;
            }

            Object e = array[0];
            for (int i = 1; i < length; i++) {
                Object v = array[i];
                if (((Comparable<Object>) e).compareTo(v) < 0) {
                    e = v;
                }
            }
            return e;
        }

        public static Object min(Object @NotNull [] array) {
            final int length = array.length;
            if (length == 0) {
                throw new NoSuchElementException();
            }

            Object e = array[0];
            for (int i = 1; i < length; i++) {
                Object v = array[i];
                if (((Comparable<Object>) e).compareTo(v) > 0) {
                    e = v;
                }
            }
            return e;
        }

        @Nullable
        public static Object minOrNull(Object @NotNull [] array) {
            final int length = array.length;
            if (length == 0) {
                return null;
            }

            Object e = array[0];
            for (int i = 1; i < length; i++) {
                Object v = array[i];
                if (((Comparable<Object>) e).compareTo(v) > 0) {
                    e = v;
                }
            }
            return e;
        }
    }

    private static final class Factory<E> implements CollectionFactory<E, ArrayList<E>, E[]> {
        @NotNull
        private final IntFunction<E[]> generator;

        private final E[] empty;

        public Factory(@NotNull IntFunction<E[]> generator) {
            this.generator = generator;
            this.empty = generator.apply(0);
        }


        @Override
        public final E[] empty() {
            return empty;
        }

        @Override
        public final ArrayList<E> newBuilder() {
            return new ArrayList<>();
        }

        @Override
        public final void addToBuilder(@NotNull ArrayList<E> buffer, E value) {
            buffer.add(value);
        }

        @Override
        public final ArrayList<E> mergeBuilder(@NotNull ArrayList<E> builder1, @NotNull ArrayList<E> builder2) {
            builder1.addAll(builder2);
            return builder1;
        }

        @Override
        public final E[] build(@NotNull ArrayList<E> buffer) {
            final int size = buffer.size();

            if (size == 0) {
                return empty;
            }

            return buffer.toArray(generator.apply(size));
        }
    }

    static final class Itr<@Covariant E> implements Iterator<E> {
        @NotNull
        private final E[] array;
        private final int endIndex;

        private int index;

        Itr(E @NotNull [] array, int beginIndex, int endIndex) {
            this.array = array;
            this.index = beginIndex;
            this.endIndex = endIndex;
        }


        @Override
        public final boolean hasNext() {
            return index < endIndex;
        }

        @Override
        public final E next() {
            if (index >= endIndex) {
                throw new NoSuchElementException(this + ".next()");
            }
            return array[index++];
        }
    }

    static final class ReverseItr<@Covariant E> implements Iterator<E> {
        @NotNull
        private final E[] array;

        private int index;

        ReverseItr(E @NotNull [] array, int index) {
            this.array = array;
            this.index = index;
        }

        ReverseItr(E @NotNull [] array) {
            this(array, array.length - 1);
        }

        @Override
        public final boolean hasNext() {
            return index >= 0;
        }

        @Override
        public final E next() {
            try {

                return array[index--];
            } catch (ArrayIndexOutOfBoundsException ignored) {
                throw new NoSuchElementException();
            }
        }
    }
}
