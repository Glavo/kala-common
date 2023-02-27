package kala.collection.base;

import kala.Conditions;
import kala.annotations.StaticClass;
import kala.collection.factory.CollectionFactory;
import kala.control.Option;
import kala.function.IndexedBiConsumer;
import kala.function.IndexedFunction;
import kala.tuple.Tuple;
import kala.tuple.Tuple2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.*;
import java.util.stream.Stream;

/**
 * Array operations based on {@code Object[]}.
 * <p>
 * These operations do not require reflection or generators to construct arrays,
 * so they are faster than operations in {@link GenericArrays}.
 */
@StaticClass
@SuppressWarnings({"unchecked", "rawtypes"})
public final class ObjectArrays {
    private ObjectArrays() {
    }

    public static final Object[] EMPTY = new Object[0];

    private static final IntFunction<Object[]> GENERATOR = (IntFunction<Object[]> & Serializable) Object[]::new;
    private static final CollectionFactory<Object, ?, Object[]> FACTORY = GenericArrays.factory(Object.class);

    //region Static Factories

    @Contract(pure = true)
    public static @NotNull IntFunction<Object[]> generator() {
        return GENERATOR;
    }

    @Contract(pure = true)
    public static @NotNull CollectionFactory<Object, ?, Object[]> factory() {
        return FACTORY;
    }

    @Contract("_ -> new")
    public static Object @NotNull [] create(int length) {
        return new Object[length];
    }

    @Contract(value = "_ -> param1", pure = true)
    public static Object @NotNull [] of(Object... values) {
        return values;
    }

    public static Object @NotNull [] from(Object @NotNull [] values) {
        return values.length == 0 ? EMPTY : Arrays.copyOf(values, values.length, Object[].class);
    }

    public static Object @NotNull [] from(@NotNull Iterable<?> values) {
        Objects.requireNonNull(values);

        if (values instanceof Collection<?>) {
            return ((Collection<?>) values).toArray();
        } else if (values instanceof Traversable<?>) {
            return ((Traversable<?>) values).toArray();
        } else {
            ArrayList<Object> tmp = new ArrayList<>();
            for (Object e : values) {
                tmp.add(e);
            }
            return tmp.toArray();
        }
    }

    public static Object @NotNull [] from(@NotNull Iterator<?> it) {
        return Iterators.toArray(it);
    }

    public static Object @NotNull [] from(@NotNull Stream<?> stream) {
        return stream.toArray(GENERATOR);
    }

    public static Object @NotNull [] fill(int n, Object value) {
        if (n <= 0) {
            return EMPTY;
        }
        Object[] res = new Object[n];
        if (value != null) {
            Arrays.fill(res, value);
        }
        return res;
    }

    public static Object @NotNull [] fill(int n, @NotNull Supplier<?> supplier) {
        if (n <= 0) {
            return EMPTY;
        }
        Object[] res = new Object[n];
        for (int i = 0; i < n; i++) {
            res[i] = supplier.get();
        }
        return res;
    }

    public static Object @NotNull [] fill(int n, @NotNull IntFunction<?> supplier) {
        if (n <= 0) {
            return EMPTY;
        }
        Object[] res = new Object[n];
        for (int i = 0; i < n; i++) {
            res[i] = supplier.apply(i);
        }
        return res;
    }

    //endregion

    public static @NotNull String className(Object @NotNull [] array) {
        return "java.lang.Object[]";
    }

    public static @NotNull Iterator<Object> iterator(Object @NotNull [] array) {
        return GenericArrays.iterator(array);
    }

    public static @NotNull Iterator<Object> iterator(Object @NotNull [] array, int beginIndex) {
        return GenericArrays.iterator(array, beginIndex);
    }

    public static @NotNull Iterator<Object> iterator(Object @NotNull [] array, int beginIndex, int endIndex) {
        return GenericArrays.iterator(array, beginIndex, endIndex);
    }

    public static @NotNull Spliterator<Object> spliterator(Object @NotNull [] array) {
        return Arrays.spliterator(array);
    }

    public static @NotNull Stream<Object> stream(Object @NotNull [] array) {
        return Arrays.stream(array);
    }

    public static @NotNull Stream<Object> parallelStream(Object @NotNull [] array) {
        return Arrays.stream(array).parallel();
    }

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

    public static Object get(Object @NotNull [] array, int index) {
        try {
            return array[index];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException(e.getMessage());
        }
    }

    public static @Nullable Object getOrNull(Object @NotNull [] array, int index) {
        return index >= 0 && index <= array.length
                ? array[index]
                : null;
    }

    public static @NotNull Option<Object> getOption(Object @NotNull [] array, int index) {
        return index >= 0 && index <= array.length
                ? Option.some(array[index])
                : Option.none();
    }

    public static void set(Object @NotNull [] array, int index, Object value) {
        try {
            array[index] = value;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException(e.getMessage());
        }
    }

    //endregion

    //region Reversal Operations

    public static Object @NotNull [] reversed(Object @NotNull [] array) {
        final int length = array.length;
        switch (length) {
            case 0:
                return array;
            case 1:
                return array.clone();
        }

        Object[] res = new Object[length];

        for (int i = 0; i < length; i++) {
            res[i] = array[length - i - 1];
        }
        return res;
    }

    public static @NotNull Iterator<Object> reverseIterator(Object @NotNull [] array) {
        return GenericArrays.iterator(array);
    }

    //endregion

    public static void shuffle(Object @NotNull [] array) {
        shuffle(array, ThreadLocalRandom.current());
    }

    public static void shuffle(Object @NotNull [] array, @NotNull Random random) {
        final int size = array.length;
        if (size <= 1) {
            return;
        }

        for (int i = size; i > 1; i--) {
            final int k = random.nextInt(i);
            final Object tmp = array[i - 1];
            array[i - 1] = array[k];
            array[k] = tmp;
        }
    }

    //region Element Retrieval Operations

    public static @NotNull Option<Object> find(Object @NotNull [] array, @NotNull Predicate predicate) {
        for (Object e : array) {
            if (predicate.test(e)) {
                return Option.some(e);
            }
        }
        return Option.none();
    }

    public static Object first(Object @NotNull [] array) {
        try {
            return array[0];
        } catch (ArrayIndexOutOfBoundsException ignored) {
            throw new NoSuchElementException();
        }
    }

    public static Object last(Object @NotNull [] array) {
        final int length = array.length;
        if (length == 0) {
            throw new NoSuchElementException();
        }
        return array[length - 1];
    }

    //endregion

    //region Element Conditions

    public static boolean contains(Object @NotNull [] array, Object value) {
        if (value == null) {
            for (Object o : array) {
                if (null == o) {
                    return true;
                }
            }
        } else {
            for (Object o : array) {
                if (value.equals(o)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean containsAll(Object @NotNull [] array, Object @NotNull [] values) {
        for (Object value : values) {
            if (!contains(array, value)) {
                return false;
            }
        }
        return true;
    }

    public static boolean containsAll(Object @NotNull [] array, @NotNull Iterable<?> values) {
        for (Object value : values) {
            if (!contains(array, value)) {
                return false;
            }
        }
        return true;
    }

    public static boolean anyMatch(Object @NotNull [] array, @NotNull Predicate<?> predicate) {
        for (Object e : array) {
            if (((Predicate) predicate).test(e)) {
                return true;
            }
        }
        return false;
    }

    public static boolean allMatch(Object @NotNull [] array, @NotNull Predicate<?> predicate) {
        for (Object e : array) {
            if (!((Predicate) predicate).test(e)) {
                return false;
            }
        }
        return true;
    }

    public static boolean noneMatch(Object @NotNull [] array, @NotNull Predicate<?> predicate) {
        for (Object e : array) {
            if (((Predicate) predicate).test(e)) {
                return false;
            }
        }
        return true;
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
    public static int indexOf(Object @NotNull [] array, Object value, int beginIndex) {
        final int length = array.length;

        if (beginIndex >= length) {
            return -1;
        }

        if (value == null) {
            for (int i = Math.max(beginIndex, 0); i < length; i++) {
                if (array[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = Math.max(beginIndex, 0); i < length; i++) {
                if (value.equals(array[i])) {
                    return i;
                }
            }
        }

        return -1;
    }

    @Contract(pure = true)
    public static int indexWhere(Object @NotNull [] array, @NotNull Predicate<?> predicate) {
        final int length = array.length;
        for (int i = 0; i < length; i++) {
            if (((Predicate) predicate).test(array[i])) {
                return i;
            }
        }
        return -1;
    }

    @Contract(pure = true)
    public static int indexWhere(Object @NotNull [] array, @NotNull Predicate<?> predicate, int beginIndex) {
        final int length = array.length;

        if (beginIndex >= length) {
            return -1;
        }

        for (int i = Math.max(beginIndex, 0); i < length; i++) {
            if (((Predicate) predicate).test(array[i])) {
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
    public static int lastIndexOf(Object @NotNull [] array, Object value, int endIndex) {
        if (endIndex < 0) {
            return -1;
        }

        if (value == null) {
            for (int i = Integer.min(endIndex, array.length - 1); i >= 0; i--) {
                if (array[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = Integer.min(endIndex, array.length - 1); i >= 0; i--) {
                if (value.equals(array[i])) {
                    return i;
                }
            }
        }

        return -1;
    }

    @Contract(pure = true)
    public static int lastIndexWhere(Object @NotNull [] array, @NotNull Predicate<?> predicate) {
        for (int i = array.length - 1; i >= 0; i--) {
            if (((Predicate) predicate).test(array[i])) {
                return i;
            }
        }

        return -1;
    }

    @Contract(pure = true)
    public static int lastIndexWhere(Object @NotNull [] array, @NotNull Predicate<?> predicate, int endIndex) {
        if (endIndex < 0) {
            return -1;
        }

        for (int i = endIndex; i >= 0; i--) {
            if (((Predicate) predicate).test(array[i])) {
                return i;
            }
        }

        return -1;
    }

    //endregion

    //region Misc Operations

    public static Object @NotNull [] slice(Object @NotNull [] array, int beginIndex, int endIndex) {
        final int length = array.length;
        Conditions.checkPositionIndices(beginIndex, endIndex, length);

        final int newLength = endIndex - beginIndex;
        if (newLength == 0) {
            return EMPTY;
        }

        Object[] res = new Object[newLength];
        System.arraycopy(array, beginIndex, res, 0, newLength);
        return res;
    }

    public static Object @NotNull [] drop(Object @NotNull [] array, int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        if (n == 0) {
            return EMPTY;
        }
        final int length = array.length;
        if (n >= length) {
            return EMPTY;
        }
        return Arrays.copyOfRange(array, n, array.length);
    }

    public static Object @NotNull [] dropLast(Object @NotNull [] array, int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        if (n == 0) {
            return array.clone();
        }
        return take(array, array.length - n);
    }

    public static Object @NotNull [] dropWhile(Object @NotNull [] array, @NotNull Predicate<?> predicate) {
        final int length = array.length;

        int idx = 0;
        while (idx < length && ((Predicate) predicate).test(array[idx])) {
            ++idx;
        }

        return idx >= length
                ? EMPTY
                : Arrays.copyOfRange(array, idx, length);
    }

    public static Object @NotNull [] take(Object @NotNull [] array, int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        if (n == 0) {
            return EMPTY;
        }
        final int length = array.length;
        if (n >= length) {
            return array.clone();
        }
        return Arrays.copyOfRange(array, 0, n);
    }

    public static Object @NotNull [] takeLast(Object @NotNull [] array, int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        if (n == 0) {
            return EMPTY;
        }
        return drop(array, array.length - n);
    }

    public static Object @NotNull [] takeWhile(Object @NotNull [] array, @NotNull Predicate<?> predicate) {
        final int length = array.length;

        if (length == 0) {
            return array.clone();
        }

        int count = 0;
        while (count < length && ((Predicate) predicate).test(array[count])) { // implicit null check of predicate
            ++count;
        }

        if (count == 0) {
            return EMPTY;
        }

        return Arrays.copyOf(array, count);
    }

    public static Object @NotNull [] updated(Object @NotNull [] array, int index, Object newValue) {
        final int size = array.length;

        Conditions.checkElementIndex(index, size);

        Object[] newValues = array.clone();
        newValues[index] = newValue;
        return newValues;
    }

    public static Object @NotNull [] concat(Object @NotNull [] a, Object @NotNull [] b) {
        Object[] dest = new Object[a.length + b.length];
        System.arraycopy(a, 0, dest, 0, a.length);
        System.arraycopy(b, 0, dest, a.length, b.length);
        return dest;
    }

    public static Object @NotNull [] filter(Object @NotNull [] array, @NotNull Predicate<?> predicate) {
        final int length = array.length;
        if (length == 0) {
            return array.clone();
        }

        Object[] tmp = new Object[length];
        int count = 0;
        for (Object e : array) {
            if (((Predicate) predicate).test(e)) {
                tmp[count++] = e;
            }
        }

        if (count == 0) {
            return EMPTY;
        }
        if (count == length) {
            return tmp;
        }
        return Arrays.copyOf(array, count);
    }

    public static Object @NotNull [] filterNot(Object @NotNull [] array, @NotNull Predicate<?> predicate) {
        final int length = array.length;
        if (length == 0) {
            return EMPTY;
        }

        Object[] tmp = new Object[length];
        int count = 0;
        for (Object e : array) {
            if (!((Predicate) predicate).test(e)) {
                tmp[count++] = e;
            }
        }

        if (count == 0) {
            return EMPTY;
        }
        if (count == length) {
            return tmp;
        }
        return Arrays.copyOf(array, count);
    }

    public static @NotNull Object @NotNull [] filterNotNull(Object @NotNull [] array) {
        final int length = array.length;
        if (length == 0) {
            return array.clone();
        }

        Object[] tmp = new Object[length];
        int count = 0;
        for (Object e : array) {
            if (e != null) {
                tmp[count++] = e;
            }
        }

        if (count == 0) {
            return EMPTY;
        }
        if (count == length) {
            return tmp;
        }
        return Arrays.copyOf(array, count);
    }

    public static Object @NotNull [] map(Object @NotNull [] array, @NotNull Function<?, ?> mapper) {
        final int length = array.length;
        final Object[] res = new Object[length];
        for (int i = 0; i < length; i++) {
            res[i] = ((Function) mapper).apply(array[i]);
        }
        return res;
    }

    public static Object @NotNull [] mapIndexed(
            Object @NotNull [] array,
            @NotNull IndexedFunction<?, ?> mapper) {
        final int length = array.length;
        final Object[] res = new Object[length];
        for (int i = 0; i < length; i++) {
            res[i] = ((Function) mapper).apply(array[i]);
        }
        return res;
    }

    public static @NotNull Object @NotNull [] mapNotNull(
            Object @NotNull [] array,
            @NotNull Function<?, ?> mapper) {
        final int length = array.length;
        if (length == 0) {
            return EMPTY;
        }

        final Object[] tmp = new Object[length];
        int c = 0;
        for (Object e : array) {
            Object u = ((Function) mapper).apply(e);
            if (u != null) {
                tmp[c++] = u;
            }
        }
        if (c == length) {
            return tmp;
        }
        return Arrays.copyOf(tmp, c);
    }

    public static @NotNull Object @NotNull [] mapIndexedNotNull(
            Object @NotNull [] array,
            @NotNull IndexedFunction<?, ?> mapper) {
        final int length = array.length;
        if (length == 0) {
            return EMPTY;
        }

        final Object[] tmp = new Object[length];
        int c = 0;
        for (int i = 0; i < length; i++) {
            Object u = ((IndexedFunction) mapper).apply(i, array[i]);
            if (u != null) {
                tmp[c++] = u;
            }
        }
        if (c == length) {
            return tmp;
        }
        return Arrays.copyOf(tmp, c);
    }

    public static Object @NotNull [] mapMulti(
            Object @NotNull [] array,
            @NotNull BiConsumer<?, ? super Consumer<? super Object>> mapper) {
        if (array.length == 0) {
            return EMPTY;
        }
        final ArrayList<Object> tmp = new ArrayList<>();
        Consumer<Object> consumer = tmp::add;
        for (Object o : array) {
            ((BiConsumer) mapper).accept(o, consumer);
        }
        return tmp.toArray();
    }

    public static Object @NotNull [] mapIndexedMulti(
            Object @NotNull [] array,
            @NotNull IndexedBiConsumer<?, ? super Consumer<? super Object>> mapper) {
        final int length = array.length;
        if (length == 0) {
            return EMPTY;
        }
        final ArrayList<Object> tmp = new ArrayList<>();
        Consumer<Object> consumer = tmp::add;
        for (int i = 0; i < length; i++) {
            ((IndexedBiConsumer) mapper).accept(i, array[i], consumer);
        }
        return tmp.toArray();
    }

    public static Object @NotNull [] flatMap(
            Object @NotNull [] array,
            @NotNull Function<?, ? extends Iterable<?>> mapper) {
        if (array.length == 0) {  // implicit null check of array
            return EMPTY;
        }
        ArrayList<Object> tmp = new ArrayList<>();
        for (Object e : array) {
            for (Object u : ((Function<Object, Iterable<?>>) mapper).apply(e)) {
                tmp.add(u);
            }
        }

        return tmp.toArray();
    }

    public static Tuple2<Object, Object> @NotNull [] zip(Object @NotNull [] array, Object @NotNull [] other) {
        final int length = Integer.min(array.length, other.length);
        Tuple2<Object, Object>[] res = (Tuple2<Object, Object>[]) new Tuple2<?, ?>[length];
        for (int i = 0; i < length; i++) {
            res[i] = Tuple.of(array[i], other[i]);
        }
        return res;
    }

    public static Tuple2<Object, Object> @NotNull [] zip(Object @NotNull [] array, Iterable<?> other) {
        final int length = array.length;  // implicit null check of array
        Tuple2<Object, Object>[] tmp = (Tuple2<Object, Object>[]) new Tuple2<?, ?>[length];
        if (length == 0) {
            return tmp;
        }

        int count = 0;
        Iterator<?> it = other.iterator();
        while (count < length && it.hasNext()) {
            tmp[count] = Tuple.of(array[count], it.next());
            count += 1;
        }

        return count == length ? tmp : Arrays.copyOf(tmp, count);
    }

    public static Object @NotNull [] @NotNull [] chunked(Object @NotNull [] array, int size) {
        if (size <= 0) {
            throw new IllegalArgumentException();
        }

        int arrayLength = array.length;
        if (arrayLength == 0) {
            return new Object[0][];
        }

        int x = arrayLength / size;
        int r = arrayLength % size;

        Object[][] res = new Object[r == 0 ? x : x + 1][];

        for (int i = 0; i < x; i++) {
            res[i] = Arrays.copyOfRange(array, i * size, (i + 1) * size);
        }
        if (r != 0) {
            res[x] = Arrays.copyOfRange(array, x * size, x * size + r);
        }

        return res;
    }

    public static Object @NotNull [] @NotNull [] windowed(Object @NotNull [] array, int size) {
        return windowed(array, size, 1, false);
    }

    public static Object @NotNull [] @NotNull [] windowed(Object @NotNull [] array, int size, int step) {
        return windowed(array, size, step, false);
    }

    public static Object @NotNull [] @NotNull [] windowed(Object @NotNull [] array, int size, int step, boolean partialWindows) {
        final int arrayLength = array.length;  // implicit null check of array
        if (size <= 0 || step <= 0) {
            if (size == step) {
                throw new IllegalArgumentException("size " + size + " must be greater than zero.");
            } else {
                throw new IllegalArgumentException("Both size " + size + " and step " + step + " must be greater than zero.");
            }
        }

        final int resultCapacity = arrayLength / step + (arrayLength % step == 0 ? 0 : 1);
        Object[][] ans = new Object[resultCapacity][];
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

    public static Object max(Object @NotNull [] array) {
        final int length = array.length; // implicit null check of array
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

    public static @Nullable Object maxOrNull(Object @NotNull [] array) {
        return array.length != 0 ? max(array) : null;
    }

    public static Object min(Object @NotNull [] array) {
        final int length = array.length; // implicit null check of array
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

    public static @Nullable Object minOrNull(Object @NotNull [] array) {
        return array.length != 0 ? min(array) : null;
    }

    //endregion

    //region String Representation

    public static <A extends Appendable> @NotNull A joinTo(
            Object @NotNull [] array,
            @NotNull A buffer
    ) {
        return joinTo(array, buffer, ", ", "", "");
    }

    public static <A extends Appendable> @NotNull A joinTo(
            Object @NotNull [] array,
            @NotNull A buffer,
            CharSequence separator
    ) {
        return joinTo(array, buffer, separator, "", "");
    }

    public static <A extends Appendable> @NotNull A joinTo(
            Object @NotNull [] array,
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

    public static <A extends Appendable> @NotNull A joinTo(
            Object @NotNull [] array,
            @NotNull A buffer,
            @NotNull Function<?, ? extends CharSequence> transform
    ) {
        return joinTo(array, buffer, ", ", "", "", transform);
    }

    public static <A extends Appendable> @NotNull A joinTo(
            Object @NotNull [] array,
            @NotNull A buffer,
            CharSequence separator,
            @NotNull Function<?, ? extends CharSequence> transform
    ) {
        return joinTo(array, buffer, separator, "", "", transform);
    }

    public static <A extends Appendable> @NotNull A joinTo(
            Object @NotNull [] array,
            @NotNull A buffer,
            CharSequence separator, CharSequence prefix, CharSequence postfix,
            @NotNull Function<?, ? extends CharSequence> transform
    ) {
        final int length = array.length;
        try {
            if (length == 0) {
                buffer.append(prefix).append(postfix);
                return buffer;
            }
            buffer.append(prefix).append(((Function<Object, CharSequence>) transform).apply(array[0]));
            for (int i = 1; i < length; i++) {
                buffer.append(separator);
                buffer.append(((Function<Object, CharSequence>) transform).apply(array[i]));
            }
            buffer.append(postfix);
            return buffer;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static @NotNull String joinToString(
            Object @NotNull [] array
    ) {
        return joinTo(array, new StringBuilder()).toString();
    }

    public static @NotNull String joinToString(
            Object @NotNull [] array,
            CharSequence separator
    ) {
        return joinTo(array, new StringBuilder(), separator).toString();
    }

    public static @NotNull String joinToString(
            Object @NotNull [] array,
            CharSequence separator, CharSequence prefix, CharSequence postfix
    ) {
        return joinTo(array, new StringBuilder(), separator, prefix, postfix).toString();
    }

    public static @NotNull String joinToString(
            Object @NotNull [] array,
            @NotNull Function<?, ? extends CharSequence> transform
    ) {
        return joinTo(array, new StringBuilder(), transform).toString();
    }

    public static @NotNull String joinToString(
            Object @NotNull [] array,
            CharSequence separator,
            @NotNull Function<?, ? extends CharSequence> transform
    ) {
        return joinTo(array, new StringBuilder(), separator, transform).toString();
    }

    public static @NotNull String joinToString(
            Object @NotNull [] array,
            CharSequence separator, CharSequence prefix, CharSequence postfix,
            @NotNull Function<?, ? extends CharSequence> transform
    ) {
        return joinTo(array, new StringBuilder(), separator, prefix, postfix, transform).toString();
    }

    //endregion

}
