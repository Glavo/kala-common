package org.glavo.kala.collection.base;

import org.glavo.kala.Conditions;
import org.glavo.kala.control.Option;
import org.glavo.kala.annotations.Covariant;
import org.glavo.kala.annotations.StaticClass;
import org.glavo.kala.collection.factory.CollectionFactory;
import org.glavo.kala.function.IndexedFunction;
import org.glavo.kala.tuple.Tuple;
import org.glavo.kala.tuple.Tuple2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;

@StaticClass
@SuppressWarnings("unchecked")
public final class GenericArrays {
    private GenericArrays() {
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
        return new GenericArrays.Factory<>(generator);
    }

    @Contract(value = "_ -> param1", pure = true)
    public static <E> E @NotNull [] of(E... values) {
        return values;
    }

    public static <E> E @NotNull [] of(@NotNull IntFunction<E[]> generator, E... values) {
        return from(generator, values);
    }

    public static <E> E @NotNull [] from(E @NotNull [] values) {
        return values.clone();
    }

    public static <E> E @NotNull [] from(@NotNull IntFunction<E[]> generator, E[] values) {
        final int length = values.length; // implicit null check of values
        E[] res = generator.apply(length);
        System.arraycopy(values, 0, res, 0, length);
        return res;
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

    public static @NotNull String className(Object @NotNull [] array) {
        return "Array<" + array.getClass().getComponentType().getSimpleName() + ">";
    }

    public static <E> @NotNull Iterator<E> iterator(E @NotNull [] array) {
        final int arrayLength = array.length; // implicit null check of array

        switch (arrayLength) {
            case 0:
                return Iterators.empty();
            case 1:
                return Iterators.of(array[0]);
        }
        return new Itr<>(array, 0, arrayLength);
    }

    public static <E> @NotNull Iterator<E> iterator(E @NotNull [] array, int beginIndex) {
        final int arrayLength = array.length; // implicit null check of array
        Conditions.checkPositionIndex(beginIndex, arrayLength);

        switch (arrayLength - beginIndex) {
            case 0:
                return Iterators.empty();
            case 1:
                return Iterators.of(array[0]);
        }
        return new Itr<>(array, beginIndex, arrayLength);
    }

    public static <E> @NotNull Iterator<E> iterator(E @NotNull [] array, int beginIndex, int endIndex) {
        final int arrayLength = array.length; // implicit null check of array
        Conditions.checkPositionIndices(beginIndex, endIndex, arrayLength);

        switch (endIndex - beginIndex) {
            case 0:
                return Iterators.empty();
            case 1:
                return Iterators.of(array[0]);
        }
        return new Itr<>(array, beginIndex, endIndex);
    }

    public static <E> @NotNull Spliterator<E> spliterator(E[] array) {
        return Arrays.spliterator(array);
    }

    public static <E> @NotNull Stream<E> stream(E @NotNull [] array) {
        return Arrays.stream(array);
    }

    public static <E> @NotNull Stream<E> parallelStream(E @NotNull [] array) {
        return Arrays.stream(array).parallel();
    }

    //endregion

    //region Size Info

    public static boolean isEmpty(Object @NotNull [] array) {
        return array.length == 0;
    }

    public static int size(Object @NotNull [] array) {
        return array.length;
    }

    public static int knownSize(Object @NotNull [] array) {
        return array.length;
    }

    //endregion

    //region Positional Access Operations

    public static boolean isDefineAt(Object @NotNull [] array, int index) {
        return index >= 0 && index <= array.length;
    }

    public static <E> E get(E @NotNull [] array, int index) {
        try {
            return array[index];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException(e.getMessage());
        }
    }

    public static <E> @Nullable E getOrNull(E @NotNull [] array, int index) {
        return index >= 0 && index <= array.length
                ? array[index]
                : null;
    }

    public static <E> @NotNull Option<E> getOption(E @NotNull [] array, int index) {
        return index >= 0 && index <= array.length
                ? Option.some(array[index])
                : Option.none();
    }

    public static <E> void set(E @NotNull [] array, int index, E value) {
        try {
            array[index] = value;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException(e.getMessage());
        }
    }

    //endregion

    //region Reversal Operations

    public static <E> E @NotNull [] reversed(E @NotNull [] array) {
        final int length = array.length;
        switch (length) {
            case 0:
                return array;
            case 1:
                return array.clone();
        }

        E[] res = array.getClass() == Object[].class
                ? (E[]) new Object[length]
                : (E[]) Array.newInstance(array.getClass().getComponentType(), length);

        for (int i = 0; i < length; i++) {
            res[i] = array[length - i - 1];
        }
        return res;
    }

    public static <E> @NotNull Iterator<E> reverseIterator(E @NotNull [] array) {
        final int length = array.length;
        switch (length) {
            case 0:
                return Iterators.empty();
            case 1:
                return Iterators.of(array[0]);
        }
        return new ReverseItr<>(array, length - 1);
    }

    //endregion

    //region Element Retrieval Operations

    public static <E> @NotNull Option<E> find(E @NotNull [] array, @NotNull Predicate<? super E> predicate) {
        for (E e : array) {
            if (predicate.test(e)) {
                return Option.some(e);
            }
        }
        return Option.none();
    }

    public static <E> E first(E @NotNull [] array) {
        try {
            return array[0];
        } catch (ArrayIndexOutOfBoundsException ignored) {
            throw new NoSuchElementException();
        }
    }

    public static <E> E last(E @NotNull [] array) {
        final int length = array.length;
        if (length == 0) {
            throw new NoSuchElementException();
        }
        return array[length - 1];
    }


    //endregion

    //region Element Conditions

    public static boolean contains(Object @NotNull [] array, Object value) {
        return ObjectArrays.contains(array, value);
    }

    public static boolean containsAll(Object @NotNull [] array, Object @NotNull [] values) {
        return ObjectArrays.containsAll(array, values);
    }

    public static boolean containsAll(Object @NotNull [] array, @NotNull Iterable<?> values) {
        return ObjectArrays.containsAll(array, values);
    }

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

    //region Search Operations

    @Contract(pure = true)
    public static int indexOf(Object @NotNull [] array, Object value) {
        return ObjectArrays.indexOf(array, value);
    }

    @Contract(pure = true)
    public static int indexOf(Object @NotNull [] array, Object value, int beginIndex) {
        return ObjectArrays.indexOf(array, value, beginIndex);
    }

    @Contract(pure = true)
    public static <E> int indexWhere(E @NotNull [] array, @NotNull Predicate<? super E> predicate) {
        return ObjectArrays.indexWhere(array, predicate);
    }

    @Contract(pure = true)
    public static <E> int indexWhere(E @NotNull [] array, @NotNull Predicate<? super E> predicate, int beginIndex) {
        return ObjectArrays.indexWhere(array, predicate, beginIndex);
    }

    @Contract(pure = true)
    public static int lastIndexOf(Object @NotNull [] array, Object value) {
        return ObjectArrays.lastIndexOf(array, value);
    }

    @Contract(pure = true)
    public static int lastIndexOf(Object @NotNull [] array, Object value, int endIndex) {
        return ObjectArrays.lastIndexOf(array, value, endIndex);
    }

    @Contract(pure = true)
    public static <E> int lastIndexWhere(E @NotNull [] array, @NotNull Predicate<? super E> predicate) {
        return ObjectArrays.lastIndexWhere(array, predicate);
    }

    @Contract(pure = true)
    public static <E> int lastIndexWhere(E @NotNull [] array, @NotNull Predicate<? super E> predicate, int endIndex) {
        return ObjectArrays.lastIndexWhere(array, predicate, endIndex);
    }

    //endregion

    //region Misc Operations

    public static <E> E @NotNull [] slice(E @NotNull [] array, int beginIndex, int endIndex) {
        return Arrays.copyOfRange(array, beginIndex, endIndex);
    }

    public static <E> E @NotNull [] drop(E @NotNull [] array, int n) {
        if (n <= 0) {
            return array.clone();
        }
        final int length = array.length;
        if (n >= length) {
            return newArrayByOldType(array, 0);
        }
        return Arrays.copyOfRange(array, n, array.length);
    }

    public static <E> E @NotNull [] dropLast(E @NotNull [] array, int n) {
        if (n <= 0) {
            return array.clone();
        }
        return take(array, array.length - n);
    }

    public static <E> E @NotNull [] dropWhile(E @NotNull [] array, @NotNull Predicate<? super E> predicate) {
        final int length = array.length;

        int idx = 0;
        while (idx < length && predicate.test(array[idx])) {
            ++idx;
        }

        return idx >= length
                ? newArrayByOldType(array, 0)
                : Arrays.copyOfRange(array, idx, length);
    }

    public static <E> E @NotNull [] take(E @NotNull [] array, int n) {
        if (n <= 0) {
            return newArrayByOldType(array, 0);
        }
        final int length = array.length;
        if (n >= length) {
            return array.clone();
        }
        return Arrays.copyOfRange(array, 0, n);
    }

    public static <E> E @NotNull [] takeLast(E @NotNull [] array, int n) {
        if (n <= 0) {
            return newArrayByOldType(array, 0);
        }
        return drop(array, array.length - n);
    }

    public static <E> E @NotNull [] takeWhile(E @NotNull [] array, @NotNull Predicate<? super E> predicate) {
        final int size = array.length;

        if (size == 0) {
            return array.clone();
        }

        int count = 0;
        while (count < size && predicate.test(array[count])) { // implicit null check of predicate
            ++count;
        }

        if (count == 0) {
            return newArrayByOldType(array, 0);
        }

        return Arrays.copyOf(array, count);
    }

    public static <E> E @NotNull [] updated(E @NotNull [] array, int index, E newValue) {
        final int size = array.length;

        Conditions.checkElementIndex(index, size);

        E[] newValues = array.clone();
        newValues[index] = newValue;
        return newValues;
    }

    public static <E> E @NotNull [] filter(E @NotNull [] array, @NotNull Predicate<? super E> predicate) {
        final int length = array.length;
        if (length == 0) {
            return array.clone();
        }

        E[] tmp = newArrayByOldType(array, array.length);
        int count = 0;
        for (E e : array) {
            if (predicate.test(e)) {
                tmp[count++] = e;
            }
        }

        if (count == 0) {
            return newArrayByOldType(array, 0);
        }
        if (count == length) {
            return tmp;
        }
        return Arrays.copyOf(array, count);
    }

    public static <E> E @NotNull [] filterNot(E @NotNull [] array, @NotNull Predicate<? super E> predicate) {
        final int length = array.length;
        if (length == 0) {
            return array.clone();
        }

        E[] tmp = newArrayByOldType(array, array.length);
        int count = 0;
        for (E e : array) {
            if (!predicate.test(e)) {
                tmp[count++] = e;
            }
        }

        if (count == 0) {
            return newArrayByOldType(array, 0);
        }
        if (count == length) {
            return tmp;
        }
        return Arrays.copyOf(array, count);
    }

    public static <E> @NotNull E @NotNull [] filterNotNull(E @NotNull [] array) {
        final int length = array.length;
        if (length == 0) {
            return array.clone();
        }

        E[] tmp = newArrayByOldType(array, array.length);
        int count = 0;
        for (E e : array) {
            if (e != null) {
                tmp[count++] = e;
            }
        }

        if (count == 0) {
            return newArrayByOldType(array, 0);
        }
        if (count == length) {
            return tmp;
        }
        return Arrays.copyOf(array, count);
    }

    public static <E, U> U @NotNull [] map(E @NotNull [] array, @NotNull IntFunction<U[]> generator, @NotNull Function<? super E, ? extends U> mapper) {
        final int length = array.length;
        final U[] res = generator.apply(length);
        for (int i = 0; i < length; i++) {
            res[i] = mapper.apply(array[i]);
        }
        return res;
    }

    public static <E, U> U @NotNull [] mapIndexed(
            E @NotNull [] array,
            @NotNull IntFunction<U[]> generator,
            @NotNull IndexedFunction<? super E, ? extends U> mapper) {
        final int length = array.length;
        final U[] res = generator.apply(length);
        for (int i = 0; i < length; i++) {
            res[i] = mapper.apply(i, array[i]);
        }
        return res;
    }

    public static <E, U> @NotNull U @NotNull [] mapNotNull(
            E @NotNull [] array,
            @NotNull IntFunction<U[]> generator, @NotNull Function<? super E, ? extends @Nullable U> mapper) {
        final int length = array.length;
        final U[] tmp = generator.apply(length);
        int c = 0;
        for (E e : array) {
            U u = mapper.apply(e);
            if (u != null) {
                tmp[c++] = u;
            }
        }
        if (c == length) {
            return tmp;
        }
        return Arrays.copyOf(tmp, c);
    }

    public static <E, U> @NotNull U @NotNull [] mapIndexedNotNull(
            E @NotNull [] array,
            @NotNull IntFunction<U[]> generator, @NotNull IndexedFunction<? super E, ? extends @Nullable U> mapper) {
        final int length = array.length;
        final U[] tmp = generator.apply(length);
        int c = 0;
        for (int i = 0, arrayLength = array.length; i < arrayLength; i++) {
            U u = mapper.apply(i, array[i]);
            if (u != null) {
                tmp[c++] = u;
            }
        }
        if (c == length) {
            return tmp;
        }
        return Arrays.copyOf(tmp, c);
    }

    public static <E, U> U @NotNull [] flatMap(
            E @NotNull [] array,
            @NotNull IntFunction<U[]> generator,
            @NotNull Function<? super E, ? extends Iterable<? extends U>> mapper) {
        if (array.length == 0) {
            return generator.apply(0);
        }
        ArrayList<U> tmp = new ArrayList<>();
        for (E e : array) {
            for (U u : mapper.apply(e)) {
                tmp.add(u);
            }
        }

        U[] res = generator.apply(tmp.size());
        tmp.toArray(res);
        return res;
    }

    public static <E, U> Tuple2<E, U> @NotNull [] zip(E @NotNull [] array, U @NotNull [] other) {
        final int length = Integer.min(array.length, other.length);
        Tuple2<E, U>[] res = (Tuple2<E, U>[]) new Tuple2<?, ?>[length];
        for (int i = 0; i < length; i++) {
            res[i] = Tuple.of(array[i], other[i]);
        }
        return res;
    }

    public static <E, U> Tuple2<E, U> @NotNull [] zip(E @NotNull [] array, Iterable<? extends U> other) {
        final int length = array.length;
        Tuple2<E, U>[] tmp = (Tuple2<E, U>[]) new Tuple2<?, ?>[length];
        if (length == 0) {
            return tmp;
        }

        int count = 0;
        Iterator<? extends U> it = other.iterator();
        while (count < length && it.hasNext()) {
            tmp[count] = Tuple.of(array[count], it.next());
            count += 1;
        }

        return count == length ? tmp : Arrays.copyOf(tmp, count);
    }

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
        return (E) ObjectArrays.max(array);
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
        return (E) ObjectArrays.maxOrNull(array);
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
        return Option.some((E) ObjectArrays.max(array));
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
        return (E) ObjectArrays.min(array);
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
        return (E) ObjectArrays.minOrNull(array);
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
        return Option.some((E) ObjectArrays.min(array));
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

    //region String Representation

    public static <A extends Appendable> @NotNull A joinTo(
            @NotNull Object[] array,
            @NotNull A buffer
    ) {
        return ObjectArrays.joinTo(array, buffer, ", ", "", "");
    }

    public static <A extends Appendable> @NotNull A joinTo(
            @NotNull Object[] array,
            @NotNull A buffer,
            CharSequence separator
    ) {
        return ObjectArrays.joinTo(array, buffer, separator, "", "");
    }

    public static <A extends Appendable> @NotNull A joinTo(
            @NotNull Object[] array,
            @NotNull A buffer,
            CharSequence separator, CharSequence prefix, CharSequence postfix
    ) {
        return ObjectArrays.joinTo(array, buffer, separator, prefix, postfix);
    }

    public static @NotNull String joinToString(
            @NotNull Object[] array
    ) {
        return ObjectArrays.joinTo(array, new StringBuilder()).toString();
    }

    public static @NotNull String joinToString(
            @NotNull Object[] array,
            CharSequence separator
    ) {
        return ObjectArrays.joinTo(array, new StringBuilder(), separator).toString();
    }

    public static @NotNull String joinToString(
            @NotNull Object[] array,
            CharSequence separator, CharSequence prefix, CharSequence postfix
    ) {
        return ObjectArrays.joinTo(array, new StringBuilder(), separator, prefix, postfix).toString();
    }

    //endregion

    private static <E> E[] newArrayByOldType(E @NotNull [] old, int newLength) {
        return old.getClass() == Object[].class
                ? (E[]) new Object[newLength]
                : (E[]) Array.newInstance(old.getClass().getComponentType(), newLength);
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
        private final E @NotNull [] array;

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
