package asia.kala.collection.immutable;

import asia.kala.collection.internal.CollectionHelper;
import asia.kala.traversable.Traversable;
import asia.kala.Tuple2;
import asia.kala.annotations.Covariant;
import asia.kala.annotations.StaticClass;
import asia.kala.collection.*;
import asia.kala.collection.mutable.ArrayBuffer;
import asia.kala.factory.CollectionFactory;
import asia.kala.function.IndexedFunction;
import asia.kala.traversable.JavaArray;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public final class ImmutableArray<@Covariant E> extends ArraySeq<E>
        implements ImmutableSeq<E>, ImmutableSeqOps<E, ImmutableArray<?>, ImmutableArray<E>>, IndexedSeq<E>, Serializable {
    private static final long serialVersionUID = 1845940935381169058L;

    public static final ImmutableArray<?> EMPTY = new ImmutableArray<>();

    private static final ImmutableArray.Factory<?> FACTORY = new Factory<>();

    //region Constructors

    private ImmutableArray() {
        this(JavaArray.EMPTY_OBJECT_ARRAY);
    }

    private ImmutableArray(Object[] array) {
        super(array);
    }

    //endregion

    //region Narrow method

    @Contract(value = "_ -> param1", pure = true)
    public static <E> ImmutableArray<E> narrow(ImmutableArray<? extends E> array) {
        return (ImmutableArray<E>) array;
    }

    //endregion

    //region Factory methods

    @NotNull
    public static <E> CollectionFactory<E, ?, ImmutableArray<E>> factory() {
        return (ImmutableArray.Factory<E>) FACTORY;
    }

    @NotNull
    public static <E> ImmutableArray<E> empty() {
        return (ImmutableArray<E>) EMPTY;
    }

    @NotNull
    public static <E> ImmutableArray<E> of() {
        return (ImmutableArray<E>) EMPTY;
    }

    @NotNull
    public static <E> ImmutableArray<E> of(E value1) {
        return new ImmutableArray<>(new Object[]{value1});
    }

    @NotNull
    public static <E> ImmutableArray<E> of(E value1, E value2) {
        return new ImmutableArray<>(new Object[]{value1, value2});
    }

    @NotNull
    public static <E> ImmutableArray<E> of(E value1, E value2, E value3) {
        return new ImmutableArray<>(new Object[]{value1, value2, value3});
    }

    @NotNull
    public static <E> ImmutableArray<E> of(E value1, E value2, E value3, E value4) {
        return new ImmutableArray<>(new Object[]{value1, value2, value3, value4});
    }

    @NotNull
    public static <E> ImmutableArray<E> of(E value1, E value2, E value3, E value4, E value5) {
        return new ImmutableArray<>(new Object[]{value1, value2, value3, value4, value5});
    }

    @NotNull
    @SafeVarargs
    @Contract("_ -> new")
    public static <E> ImmutableArray<E> of(E... values) {
        return from(values);
    }

    @NotNull
    public static <E> ImmutableArray<E> from(E @NotNull [] values) {
        if (values.length == 0) {
            return empty();
        }
        return new ImmutableArray<>(values.clone());
    }

    @NotNull
    public static <E> ImmutableArray<E> from(@NotNull Traversable<? extends E> values) {
        if (values instanceof ImmutableArray<?>) {
            return (ImmutableArray<E>) values;
        }

        if (values.knownSize() == 0) {
            return empty();
        }

        Object[] arr = values.toArray();
        if (arr.length == 0) {
            return empty();
        }
        return new ImmutableArray<>(arr);
    }

    @NotNull
    public static <E> ImmutableArray<E> from(@NotNull java.util.Collection<? extends E> values) {
        if (values.size() == 0) {
            return empty();
        }
        return new ImmutableArray<>(values.toArray());
    }

    @NotNull
    public static <E> ImmutableArray<E> from(@NotNull Iterable<? extends E> values) {
        Objects.requireNonNull(values);

        if (values instanceof Traversable<?>) {
            return from((Traversable<E>) values);
        }
        if (values instanceof java.util.Collection<?>) {
            return from(((java.util.Collection<E>) values));
        }

        return ArrayBuffer.<E>from(values).toImmutableArray();
    }

    @NotNull
    public static <E> ImmutableArray<E> from(@NotNull Iterator<? extends E> it) {
        if (!it.hasNext()) {
            return empty();
        }
        ArrayBuffer<E> buffer = new ArrayBuffer<>();
        while (it.hasNext()) {
            buffer.append(it.next());
        }
        return buffer.toImmutableArray();
    }

    @NotNull
    public static <E> ImmutableArray<E> fill(int n, E value) {
        if (n <= 0) {
            return empty();
        }

        Object[] ans = new Object[n];
        if (value != null) {
            Arrays.fill(ans, value);
        }
        return new ImmutableArray<>(ans);
    }

    @NotNull
    public static <E> ImmutableArray<E> fill(int n, @NotNull Supplier<? extends E> supplier) {
        if (n <= 0) {
            return empty();
        }

        Object[] ans = new Object[n];
        for (int i = 0; i < n; i++) {
            ans[i] = supplier.get();
        }
        return new ImmutableArray<>(ans);
    }

    @NotNull
    public static <E> ImmutableArray<E> fill(int n, @NotNull IntFunction<? extends E> supplier) {
        if (n <= 0) {
            return empty();
        }

        Object[] ans = new Object[n];
        for (int i = 0; i < n; i++) {
            ans[i] = supplier.apply(i);
        }
        return new ImmutableArray<>(ans);
    }

    @StaticClass
    public static class Unsafe {
        @NotNull
        @Contract("_ -> new")
        public static <E> ImmutableArray<E> wrap(E @NotNull [] array) {
            Objects.requireNonNull(array);
            return new ImmutableArray<>(array);
        }
    }

    //endregion

    //region ImmutableSeq members

    @NotNull
    @Override
    public final ImmutableArray<E> updated(int index, E newValue) {
        if (index < 0 || index >= array.length) {
            throw new IndexOutOfBoundsException("Index out of range: " + index);
        }
        Object[] newValues = array.clone();
        newValues[index] = newValue;
        return new ImmutableArray<>(newValues);
    }

    @NotNull
    @Override
    public final ImmutableArray<E> drop(int n) {
        if (n <= 0) {
            return this;
        }

        if (n >= array.length) {
            return empty();
        }

        return new ImmutableArray<>(Arrays.copyOfRange(array, n, array.length));
    }

    @NotNull
    @Override
    public final ImmutableArray<E> dropLast(int n) {
        return take(size() - n);
    }

    @NotNull
    @Override
    public final ImmutableArray<E> dropWhile(@NotNull Predicate<? super E> predicate) {
        int idx = 0;
        while (idx < array.length && predicate.test((E) array[idx])) {
            ++idx;
        }

        if (idx >= array.length) {
            return empty();
        }
        return new ImmutableArray<>(Arrays.copyOfRange(array, idx, array.length));
    }

    @NotNull
    @Override
    public final ImmutableArray<E> take(int n) {
        if (n <= 0) {
            return empty();
        }

        if (n >= array.length) {
            return this;
        }

        Object[] newValues = new Object[n];
        System.arraycopy(array, 0, newValues, 0, n);

        return new ImmutableArray<>(newValues);
    }

    @NotNull
    @Override
    public final ImmutableArray<E> takeLast(int n) {
        return drop(size() - n);
    }

    @NotNull
    @Override
    public final ImmutableArray<E> takeWhile(@NotNull Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);
        if (array.length == 0) {
            return empty();
        }

        int count = 0;
        while (count < array.length && predicate.test((E) array[count])) {
            ++count;
        }

        if (count == 0) {
            return empty();
        }

        return new ImmutableArray<>(Arrays.copyOf(array, count));
    }

    @NotNull
    @Override
    public final ImmutableArray<E> concat(@NotNull Seq<? extends E> other) {
        return appendedAll(other);
    }

    @NotNull
    @Override
    public final ImmutableArray<E> prepended(E element) {
        Object[] newValues = new Object[array.length + 1];
        newValues[0] = element;
        System.arraycopy(array, 0, newValues, 1, array.length);

        return new ImmutableArray<>(newValues);
    }

    @NotNull
    @Override
    public final ImmutableArray<E> prependedAll(@NotNull Iterable<? extends E> prefix) {
        Objects.requireNonNull(prefix);

        Object[] data = prefix instanceof ImmutableArray<?> ?
                ((ImmutableArray<?>) prefix).array : CollectionHelper.asArray(prefix);
        Object[] newValues = new Object[data.length + array.length];

        System.arraycopy(data, 0, newValues, 0, data.length);
        System.arraycopy(array, 0, newValues, data.length, array.length);

        return new ImmutableArray<>(newValues);
    }

    @NotNull
    @Override
    public final ImmutableArray<E> prependedAll(E @NotNull [] prefix) {
        Objects.requireNonNull(prefix);

        Object[] newValues = new Object[prefix.length + array.length];
        System.arraycopy(prefix, 0, newValues, 0, prefix.length);
        System.arraycopy(array, 0, newValues, prefix.length, array.length);

        return new ImmutableArray<>(newValues);
    }

    @NotNull
    @Override
    public final ImmutableArray<E> appended(E element) {
        Object[] newValues = Arrays.copyOf(array, array.length + 1);
        newValues[array.length] = element;

        return new ImmutableArray<>(newValues);
    }

    @NotNull
    @Override
    public final ImmutableArray<E> appendedAll(@NotNull Iterable<? extends E> postfix) {
        Objects.requireNonNull(postfix);

        Object[] data = postfix instanceof ImmutableArray<?>
                ? ((ImmutableArray<?>) postfix).array
                : CollectionHelper.asArray(postfix);
        Object[] newValues = new Object[data.length + array.length];

        System.arraycopy(array, 0, newValues, 0, array.length);
        System.arraycopy(data, 0, newValues, array.length, data.length);

        return new ImmutableArray<>(newValues);
    }

    @NotNull
    @Override
    public final ImmutableArray<E> appendedAll(E @NotNull [] postfix) {
        Objects.requireNonNull(postfix);

        Object[] newValues = new Object[postfix.length + array.length];

        System.arraycopy(array, 0, newValues, 0, array.length);
        System.arraycopy(postfix, 0, newValues, array.length, postfix.length);

        return new ImmutableArray<>(newValues);
    }

    @NotNull
    @Override
    public final <U> ImmutableArray<U> mapIndexed(@NotNull IndexedFunction<? super E, ? extends U> mapper) {
        Objects.requireNonNull(mapper);

        Object[] newValues = new Object[array.length];
        for (int i = 0; i < array.length; i++) {
            newValues[i] = mapper.apply(i, (E) array[i]);
        }
        return new ImmutableArray<>(newValues);
    }

    @NotNull
    @Override
    public final ImmutableArray<E> sorted() {
        return sorted((Comparator<? super E>) Comparator.naturalOrder());
    }

    @NotNull
    @Override
    public final ImmutableArray<E> sorted(@NotNull Comparator<? super E> comparator) {
        final Object[] array = this.array;
        if (array.length == 0) {
            return this;
        }

        Object[] newValues = array.clone();
        Arrays.sort(newValues, (Comparator<? super Object>) comparator);
        return new ImmutableArray<>(newValues);
    }

    @NotNull
    @Override
    public final ImmutableArray<E> reversed() {
        final Object[] array = this.array;
        final int size = array.length;
        if (array.length == 0) {
            return this;
        }
        Object[] res = new Object[size];
        for (int i = 0; i < size; i++) {
            res[i] = array[size - i - 1];
        }
        return new ImmutableArray<>(res);
    }

    //endregion

    //region ImmutableCollection members

    @Override
    public final String className() {
        return "ImmutableArray";
    }

    @NotNull
    @Override
    public final Spliterator<E> spliterator() {
        return Spliterators.spliterator(array, Spliterator.IMMUTABLE);
    }

    @NotNull
    @Override
    public final <U> ImmutableArray<U> map(@NotNull Function<? super E, ? extends U> mapper) {
        Objects.requireNonNull(mapper);

        final Object[] values = this.array;
        final int length = values.length;

        if (length == 0) {
            return empty();
        }

        Object[] newValues = new Object[length];

        for (int i = 0; i < length; i++) {
            newValues[i] = mapper.apply((E) values[i]);
        }

        return new ImmutableArray<>(newValues);
    }

    @NotNull
    @Override
    public final ImmutableArray<E> filter(@NotNull Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);

        final Object[] values = this.array;
        final int length = values.length;

        if (length == 0) {
            return this;
        }

        Object[] temp = new Object[length];
        int c = 0;

        for (Object value : values) {
            E v = (E) value;
            if (predicate.test(v)) {
                temp[c++] = v;
            }
        }

        if (c == 0) {
            return empty();
        }

        return new ImmutableArray<>(Arrays.copyOf(temp, c));
    }

    @NotNull
    @Override
    public final ImmutableArray<E> filterNot(@NotNull Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);

        final Object[] values = this.array;
        final int length = values.length;

        if (length == 0) {
            return this;
        }

        Object[] temp = new Object[length];
        int c = 0;

        for (Object value : values) {
            E v = (E) value;
            if (!predicate.test(v)) {
                temp[c++] = v;
            }
        }

        if (c == 0) {
            return empty();
        }

        return new ImmutableArray<>(Arrays.copyOf(temp, c));
    }

    @NotNull
    @Override
    public final ImmutableArray<@NotNull E> filterNotNull() {
        final Object[] values = this.array;
        final int length = values.length;

        if (length == 0) {
            return this;
        }

        Object[] temp = new Object[length];
        int c = 0;

        for (Object value : values) {
            E v = (E) value;
            if (v != null) {
                temp[c++] = v;
            }
        }

        if (c == 0) {
            return empty();
        }

        return new ImmutableArray<>(Arrays.copyOf(temp, c));
    }

    @NotNull
    @Override
    public final <U> ImmutableArray<U> flatMap(@NotNull Function<? super E, ? extends Iterable<? extends U>> mapper) {
        return AbstractImmutableCollection.flatMap(this, mapper, iterableFactory());
    }

    @NotNull
    @Override
    public final Tuple2<ImmutableArray<E>, ImmutableArray<E>> span(@NotNull Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);

        final Object[] array = this.array;
        final int arrayLength = array.length;

        if (arrayLength == 0) {
            return new Tuple2<>(empty(), empty());
        }

        int idx = 0;
        while (idx < arrayLength) {
            if (!predicate.test((E) array[idx])) {
                break;
            }
            ++idx;
        }

        final int l1 = idx;
        final int l2 = arrayLength - idx;

        if (idx == 0) {
            return new Tuple2<>(empty(), this);
        } else if (idx == arrayLength) {
            return new Tuple2<>(this, empty());
        } else {
            return new Tuple2<>(
                    new ImmutableArray<>(Arrays.copyOfRange(array, 0, idx)),
                    new ImmutableArray<>(Arrays.copyOfRange(array, idx, arrayLength))
            );
        }

    }

    @NotNull
    @Override
    public final <U> CollectionFactory<U, ?, ImmutableArray<U>> iterableFactory() {
        return factory();
    }

    @NotNull
    @Override
    public final ImmutableArray<E> toImmutableArray() {
        return this;
    }

    //endregion

    private static final class Factory<E> implements CollectionFactory<E, ArrayBuffer<E>, ImmutableArray<E>> {
        Factory() {
        }

        @Override
        public final ImmutableArray<E> empty() {
            return ImmutableArray.empty();
        }

        @Override
        public final ImmutableArray<E> from(E @NotNull [] values) {
            return ImmutableArray.from(values);
        }

        @Override
        public final ImmutableArray<E> from(@NotNull Iterable<? extends E> values) {
            return ImmutableArray.from(values);
        }

        @Override
        public final ArrayBuffer<E> newBuilder() {
            return new ArrayBuffer<>();
        }

        @Override
        public final void addToBuilder(@NotNull ArrayBuffer<E> buffer, E value) {
            buffer.append(value);
        }

        @Override
        public final void sizeHint(@NotNull ArrayBuffer<E> buffer, int size) {
            buffer.sizeHint(size);
        }

        @Override
        public final ArrayBuffer<E> mergeBuilder(@NotNull ArrayBuffer<E> buffer1, @NotNull ArrayBuffer<E> buffer2) {
            buffer1.appendAll(buffer2);
            return buffer1;
        }

        @Override
        public final ImmutableArray<E> build(@NotNull ArrayBuffer<E> buffer) {
            return buffer.toImmutableArray();
        }

    }
}
