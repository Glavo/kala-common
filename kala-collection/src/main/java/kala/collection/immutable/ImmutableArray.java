package kala.collection.immutable;

import kala.collection.ArraySeq;
import kala.collection.base.GenericArrays;
import kala.collection.base.Traversable;
import kala.function.CheckedFunction;
import kala.function.CheckedIndexedFunction;
import kala.function.CheckedPredicate;
import kala.function.IndexedFunction;
import kala.collection.IndexedSeq;
import kala.collection.SeqLike;
import kala.collection.internal.CollectionHelper;
import kala.Conditions;
import kala.tuple.Tuple2;
import kala.annotations.Covariant;
import kala.annotations.StaticClass;
import kala.collection.mutable.ArrayBuffer;
import kala.collection.factory.CollectionFactory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static kala.Conditions.checkPositionIndices;

@SuppressWarnings("unchecked")
public final class ImmutableArray<@Covariant E> extends ArraySeq<E>
        implements ImmutableSeq<E>, ImmutableSeqOps<E, ImmutableArray<?>, ImmutableArray<E>>, IndexedSeq<E>, Serializable {
    private static final long serialVersionUID = 1845940935381169058L;

    public static final ImmutableArray<?> EMPTY = new ImmutableArray<>();

    private static final ImmutableArray.Factory<?> FACTORY = new Factory<>();

    //region Constructors

    private ImmutableArray() {
        this(GenericArrays.EMPTY_OBJECT_ARRAY);
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

    //region Static Factories

    public static <E> @NotNull CollectionFactory<E, ?, ImmutableArray<E>> factory() {
        return (ImmutableArray.Factory<E>) FACTORY;
    }

    public static <E> @NotNull Collector<E, ?, ImmutableArray<E>> collector() {
        return factory();
    }

    public static <E> @NotNull ImmutableArray<E> empty() {
        return (ImmutableArray<E>) EMPTY;
    }

    public static <E> @NotNull ImmutableArray<E> of() {
        return (ImmutableArray<E>) EMPTY;
    }

    public static <E> @NotNull ImmutableArray<E> of(E value1) {
        return new ImmutableArray<>(new Object[]{value1});
    }

    public static <E> @NotNull ImmutableArray<E> of(E value1, E value2) {
        return new ImmutableArray<>(new Object[]{value1, value2});
    }

    public static <E> @NotNull ImmutableArray<E> of(E value1, E value2, E value3) {
        return new ImmutableArray<>(new Object[]{value1, value2, value3});
    }

    public static <E> @NotNull ImmutableArray<E> of(E value1, E value2, E value3, E value4) {
        return new ImmutableArray<>(new Object[]{value1, value2, value3, value4});
    }

    public static <E> @NotNull ImmutableArray<E> of(E value1, E value2, E value3, E value4, E value5) {
        return new ImmutableArray<>(new Object[]{value1, value2, value3, value4, value5});
    }

    @SafeVarargs
    @Contract("_ -> new")
    public static <E> @NotNull ImmutableArray<E> of(E... values) {
        return from(values);
    }

    public static <E> @NotNull ImmutableArray<E> from(E @NotNull [] values) {
        if (values.length == 0) { // implicit null check of values
            return empty();
        }
        return new ImmutableArray<>(values.clone());
    }

    public static <E> @NotNull ImmutableArray<E> from(@NotNull Traversable<? extends E> values) {
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

    public static <E> @NotNull ImmutableArray<E> from(@NotNull java.util.Collection<? extends E> values) {
        if (values.size() == 0) {
            return empty();
        }
        return new ImmutableArray<>(values.toArray());
    }

    public static <E> @NotNull ImmutableArray<E> from(@NotNull Iterable<? extends E> values) {
        Objects.requireNonNull(values);

        if (values instanceof ImmutableArray) {
            return ((ImmutableArray<E>) values);
        }
        if (values instanceof Traversable) {
            return from((Traversable<E>) values);
        }
        if (values instanceof java.util.Collection) {
            return from(((java.util.Collection<E>) values));
        }

        return ArrayBuffer.<E>from(values).toImmutableArray();
    }

    public static <E> @NotNull ImmutableArray<E> from(@NotNull Iterator<? extends E> it) {
        if (!it.hasNext()) {
            return empty();
        }
        ArrayBuffer<E> buffer = new ArrayBuffer<>();
        while (it.hasNext()) {
            buffer.append(it.next());
        }
        return buffer.toImmutableArray();
    }

    public static <E> @NotNull ImmutableArray<E> from(@NotNull Stream<? extends E> stream) {
        final Object[] arr = stream.toArray();
        return arr.length == 0 ? ImmutableArray.empty() : new ImmutableArray<>(arr);
    }

    public static <E> @NotNull ImmutableArray<E> fill(int n, E value) {
        if (n <= 0) {
            return empty();
        }

        Object[] ans = new Object[n];
        if (value != null) {
            Arrays.fill(ans, value);
        }
        return new ImmutableArray<>(ans);
    }

    public static <E> @NotNull ImmutableArray<E> fill(int n, @NotNull Supplier<? extends E> supplier) {
        if (n <= 0) {
            return empty();
        }

        Object[] ans = new Object[n];
        for (int i = 0; i < n; i++) {
            ans[i] = supplier.get();
        }
        return new ImmutableArray<>(ans);
    }

    public static <E> @NotNull ImmutableArray<E> fill(int n, @NotNull IntFunction<? extends E> init) {
        if (n <= 0) {
            return empty();
        }

        Object[] ans = new Object[n];
        for (int i = 0; i < n; i++) {
            ans[i] = init.apply(i);
        }
        return new ImmutableArray<>(ans);
    }

    @StaticClass
    public final static class Unsafe {
        private Unsafe() {
        }

        @Contract("_ -> new")
        public static <E> @NotNull ImmutableArray<E> wrap(E @NotNull [] array) {
            Objects.requireNonNull(array);
            return new ImmutableArray<>(array);
        }
    }

    //endregion

    final Object @NotNull [] getArray() {
        return elements;
    }

    //region Collection Operations

    @Override
    public final @NotNull String className() {
        return "ImmutableArray";
    }

    @Override
    public final <U> @NotNull CollectionFactory<U, ?, ImmutableArray<U>> iterableFactory() {
        return factory();
    }

    @Override
    public final @NotNull Spliterator<E> spliterator() {
        return Spliterators.spliterator(elements, Spliterator.IMMUTABLE);
    }

    //endregion

    //region Addition Operations

    @Override
    public final @NotNull ImmutableArray<E> prepended(E value) {
        Object[] newValues = new Object[elements.length + 1];
        newValues[0] = value;
        System.arraycopy(elements, 0, newValues, 1, elements.length);

        return new ImmutableArray<>(newValues);
    }

    @Override
    public final @NotNull ImmutableArray<E> prependedAll(E @NotNull [] values) {
        if (values.length == 0) { // implicit null check of prefix
            return this;
        }

        Object[] newValues = Arrays.copyOf(values, values.length + elements.length, Object[].class);
        System.arraycopy(elements, 0, newValues, values.length, elements.length);

        return new ImmutableArray<>(newValues);
    }

    @Override
    public final @NotNull ImmutableArray<E> prependedAll(@NotNull Iterable<? extends E> values) {
        Objects.requireNonNull(values);

        Object[] data = values instanceof ImmutableArray<?> ?
                ((ImmutableArray<?>) values).elements : CollectionHelper.asArray(values);
        Object[] newValues = new Object[data.length + elements.length];

        System.arraycopy(data, 0, newValues, 0, data.length);
        System.arraycopy(elements, 0, newValues, data.length, elements.length);

        return new ImmutableArray<>(newValues);
    }

    @Override
    public final @NotNull ImmutableArray<E> appended(E value) {
        final Object[] elements = this.elements;
        final int size = elements.length;

        Object[] newValues = Arrays.copyOf(elements, size + 1);
        newValues[size] = value;

        return new ImmutableArray<>(newValues);
    }

    @Override
    public final @NotNull ImmutableArray<E> appendedAll(E @NotNull [] values) {
        if (values.length == 0) { // implicit null check of values
            return this;
        }

        final Object[] elements = this.elements;
        final int size = elements.length;

        Object[] newValues = Arrays.copyOf(elements, values.length + size);
        System.arraycopy(values, 0, newValues, size, values.length);

        return new ImmutableArray<>(newValues);
    }

    @Override
    public final @NotNull ImmutableArray<E> appendedAll(@NotNull Iterable<? extends E> values) {
        Objects.requireNonNull(values);

        Object[] data = values instanceof ImmutableArray<?>
                ? ((ImmutableArray<?>) values).elements
                : CollectionHelper.asArray(values);
        Object[] newValues = Arrays.copyOf(elements, elements.length + data.length);
        System.arraycopy(data, 0, newValues, elements.length, data.length);

        return new ImmutableArray<>(newValues);
    }

    //endregion

    @Override
    public final @NotNull ImmutableArray<E> slice(int beginIndex, int endIndex) {
        final Object[] elements = this.elements;
        final int size = elements.length;
        checkPositionIndices(beginIndex, endIndex, size);

        final int ns = endIndex - beginIndex;
        if (ns == 0) {
            return empty();
        }
        if (ns == size) {
            return this;
        }

        return new ImmutableArray<>(Arrays.copyOfRange(elements, beginIndex, endIndex));
    }

    @Override
    public final @NotNull ImmutableArray<E> drop(int n) {
        if (n <= 0) {
            return this;
        }

        final Object[] elements = this.elements;
        final int size = elements.length;

        if (n >= size) {
            return empty();
        }

        return new ImmutableArray<>(Arrays.copyOfRange(elements, n, size));
    }

    @Override
    public final @NotNull ImmutableArray<E> dropLast(int n) {
        if (n <= 0) {
            return this;
        }
        return take(size() - n);
    }

    @Override
    public final @NotNull ImmutableArray<E> dropWhile(@NotNull Predicate<? super E> predicate) {
        final Object[] elements = this.elements;
        final int size = elements.length;

        int idx = 0;
        while (idx < size && predicate.test((E) elements[idx])) {
            ++idx;
        }

        if (idx >= size) {
            return empty();
        }
        return new ImmutableArray<>(Arrays.copyOfRange(elements, idx, size));
    }

    @Override
    public final @NotNull ImmutableArray<E> take(int n) {
        final Object[] elements = this.elements;
        final int size = elements.length;

        if (n <= 0) {
            return empty();
        }

        if (n >= size) {
            return this;
        }

        return new ImmutableArray<>(Arrays.copyOf(elements, n));
    }

    @Override
    public final @NotNull ImmutableArray<E> takeLast(int n) {
        if (n <= 0) {
            return empty();
        }
        return drop(size() - n);
    }

    @Override
    public final @NotNull ImmutableArray<E> takeWhile(@NotNull Predicate<? super E> predicate) {
        final Object[] elements = this.elements;
        final int size = elements.length;

        if (size == 0) {
            return empty();
        }

        int count = 0;
        while (count < size && predicate.test((E) elements[count])) { // implicit null check of predicate
            ++count;
        }

        if (count == 0) {
            return empty();
        }
        if (count == size) {
            return this;
        }

        return new ImmutableArray<>(Arrays.copyOf(elements, count));
    }

    @Override
    public final @NotNull ImmutableArray<E> updated(int index, E newValue) {
        final Object[] elements = this.elements;
        final int size = elements.length;

        Conditions.checkElementIndex(index, size);

        Object[] newValues = elements.clone();
        newValues[index] = newValue;
        return new ImmutableArray<>(newValues);
    }

    @Override
    public final @NotNull ImmutableArray<E> concat(@NotNull SeqLike<? extends E> other) {
        return appendedAll(other);
    }

    @Override
    public final @NotNull ImmutableArray<E> concat(@NotNull List<? extends E> other) {
        return appendedAll(other);
    }


    @Override
    public final @NotNull ImmutableArray<E> filter(@NotNull Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);

        final Object[] elements = this.elements;
        final int size = elements.length;

        if (size == 0) {
            return this;
        }

        Object[] tmp = new Object[size];
        int c = 0;

        for (Object value : elements) {
            E v = (E) value;
            if (predicate.test(v)) {
                tmp[c++] = v;
            }
        }

        if (c == 0) {
            return empty();
        }
        if (c == size) {
            return this;
        }

        return new ImmutableArray<>(Arrays.copyOf(tmp, c));
    }

    @Override
    public final @NotNull <Ex extends Throwable> ImmutableArray<E> filterChecked(
            @NotNull CheckedPredicate<? super E, ? extends Ex> predicate) {
        return filter(predicate);
    }

    @Override
    public final @NotNull ImmutableArray<E> filterUnchecked(@NotNull CheckedPredicate<? super E, ?> predicate) {
        return filter(predicate);
    }

    @Override
    public final @NotNull ImmutableArray<E> filterNot(@NotNull Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);

        final Object[] elements = this.elements;
        final int size = elements.length;

        if (size == 0) {
            return this;
        }

        Object[] tmp = new Object[size];
        int c = 0;

        for (Object value : elements) {
            E v = (E) value;
            if (!predicate.test(v)) {
                tmp[c++] = v;
            }
        }

        if (c == 0) {
            return empty();
        }
        if (c == size) {
            return this;
        }

        return new ImmutableArray<>(Arrays.copyOf(tmp, c));
    }

    @Override
    public final @NotNull <Ex extends Throwable> ImmutableArray<E> filterNotChecked(
            @NotNull CheckedPredicate<? super E, ? extends Ex> predicate) {
        return filterNot(predicate);
    }

    @Override
    public final @NotNull ImmutableArray<E> filterNotUnchecked(@NotNull CheckedPredicate<? super E, ?> predicate) {
        return filterNot(predicate);
    }

    @Override
    public final @NotNull ImmutableArray<@NotNull E> filterNotNull() {
        final Object[] elements = this.elements;
        final int size = elements.length;

        if (size == 0) {
            return this;
        }

        Object[] tmp = new Object[size];
        int c = 0;

        for (Object value : elements) {
            if (value != null) {
                tmp[c++] = value;
            }
        }

        if (c == 0) {
            return empty();
        }
        if (c == size) {
            return this;
        }

        return new ImmutableArray<>(Arrays.copyOf(tmp, c));
    }

    @Override
    public @NotNull <U> ImmutableArray<@NotNull U> filterIsInstance(@NotNull Class<? extends U> clazz) {
        final Object[] elements = this.elements;
        final int size = elements.length;

        if (size == 0) {
            return ImmutableArray.empty();
        }

        Object[] tmp = new Object[size];
        int c = 0;

        for (Object value : elements) {
            if (clazz.isInstance(value)) {
                tmp[c++] = value;
            }
        }

        if (c == 0) {
            return empty();
        }
        if (c == size) {
            return ((ImmutableArray<U>) this);
        }

        return new ImmutableArray<>(Arrays.copyOf(tmp, c));
    }

    @Override
    public final <U> @NotNull ImmutableArray<U> map(@NotNull Function<? super E, ? extends U> mapper) {
        final Object[] elements = this.elements;
        final int size = elements.length;

        if (size == 0) {
            return empty();
        }

        Object[] newValues = new Object[size];

        for (int i = 0; i < size; i++) {
            newValues[i] = mapper.apply((E) elements[i]);
        }

        return new ImmutableArray<>(newValues);
    }

    @Override
    public final @NotNull <U, Ex extends Throwable> ImmutableArray<U> mapChecked(
            @NotNull CheckedFunction<? super E, ? extends U, ? extends Ex> mapper) throws Ex {
        return map(mapper);
    }

    @Override
    public final @NotNull <U> ImmutableArray<U> mapUnchecked(@NotNull CheckedFunction<? super E, ? extends U, ?> mapper) {
        return map(mapper);
    }

    @Override
    public final <U> @NotNull ImmutableArray<U> mapIndexed(@NotNull IndexedFunction<? super E, ? extends U> mapper) {
        final Object[] elements = this.elements;
        final int size = elements.length;

        if (size == 0) {
            return empty();
        }

        Object[] newValues = new Object[size];
        for (int i = 0; i < size; i++) {
            newValues[i] = mapper.apply(i, (E) elements[i]);
        }
        return new ImmutableArray<>(newValues);
    }

    @Override
    public final @NotNull <U, Ex extends Throwable> ImmutableArray<U> mapIndexedChecked(
            @NotNull CheckedIndexedFunction<? super E, ? extends U, ? extends Ex> mapper) throws Ex {
        return mapIndexed(mapper);
    }

    @Override
    public final @NotNull <U> ImmutableArray<U> mapIndexedUnchecked(@NotNull CheckedIndexedFunction<? super E, ? extends U, ?> mapper) {
        return mapIndexed(mapper);
    }

    @Override
    public final <U> @NotNull ImmutableArray<@NotNull U> mapNotNull(@NotNull Function<? super E, ? extends @Nullable U> mapper) {
        final Object[] elements = this.elements;
        final int size = elements.length;

        if (size == 0) {
            return ImmutableArray.empty();
        }

        Object[] tmp = new Object[size];
        int c = 0;

        for (Object value : elements) {
            final U u = mapper.apply((E) value);
            if (u != null) {
                tmp[c++] = u;
            }
        }

        if (c == 0) {
            return empty();
        }
        if (c == size) {
            return new ImmutableArray<>(tmp);
        }

        return new ImmutableArray<>(Arrays.copyOf(tmp, c));
    }

    @Override
    public final @NotNull <U, Ex extends Throwable> ImmutableArray<U> mapNotNullChecked(
            @NotNull CheckedFunction<? super E, ? extends U, ? extends Ex> mapper) throws Ex {
        return mapNotNull(mapper);
    }

    @Override
    public final @NotNull <U> ImmutableArray<U> mapNotNullUnchecked(@NotNull CheckedFunction<? super E, ? extends U, ?> mapper) {
        return mapNotNull(mapper);
    }

    @Override
    public final @NotNull <U> ImmutableArray<@NotNull U> mapIndexedNotNull(@NotNull IndexedFunction<? super E, ? extends @Nullable U> mapper) {
        final Object[] elements = this.elements;
        final int size = elements.length;

        if (size == 0) {
            return ImmutableArray.empty();
        }

        Object[] tmp = new Object[size];
        int c = 0;

        for (int i = 0; i < size; i++) {
            final U u = mapper.apply(i, (E) elements[i]);
            if (u != null) {
                tmp[c++] = u;
            }
        }

        if (c == 0) {
            return empty();
        }
        if (c == size) {
            return new ImmutableArray<>(tmp);
        }

        return new ImmutableArray<>(Arrays.copyOf(tmp, c));
    }

    @Override
    public final @NotNull <U, Ex extends Throwable> ImmutableArray<@NotNull U> mapIndexedNotNullChecked(
            @NotNull CheckedIndexedFunction<? super E, @Nullable ? extends U, ? extends Ex> mapper) {
        return mapIndexedNotNull(mapper);
    }

    @Override
    public final <U> @NotNull ImmutableArray<@NotNull U> mapIndexedNotNullUnchecked(
            @NotNull CheckedIndexedFunction<? super E, ? extends U, ?> mapper) {
        return mapIndexedNotNull(mapper);
    }

    @Override
    public final <U> @NotNull ImmutableArray<U> flatMap(@NotNull Function<? super E, ? extends Iterable<? extends U>> mapper) {
        final Object[] elements = this.elements;
        final int size = elements.length;
        if (size == 0) {
            return empty();
        }

        ArrayBuffer<U> builder = new ArrayBuffer<>();
        for (Object value : elements) {
            builder.appendAll(mapper.apply((E) value));
        }
        return builder.toImmutableArray();
    }

    @Override
    public final @NotNull <U, Ex extends Throwable> ImmutableArray<U> flatMapChecked(
            @NotNull CheckedFunction<? super E, ? extends Iterable<? extends U>, ? extends Ex> mapper) throws Ex {
        return flatMap(mapper);
    }

    @Override
    public final <U> @NotNull ImmutableArray<U> flatMapUnchecked(
            @NotNull CheckedFunction<? super E, ? extends Iterable<? extends U>, ?> mapper) {
        return flatMap(mapper);
    }

    @Override
    public final @NotNull ImmutableArray<E> sorted() {
        final Object[] elements = this.elements;
        if (elements.length == 0 || elements.length == 1) {
            return this;
        }

        Object[] newValues = elements.clone();
        Arrays.sort(newValues);
        return new ImmutableArray<>(newValues);
    }

    @Override
    public final @NotNull ImmutableArray<E> sorted(Comparator<? super E> comparator) {
        final Object[] elements = this.elements;
        if (elements.length == 0 || elements.length == 1) {
            return this;
        }

        Object[] newValues = elements.clone();
        Arrays.sort(newValues, (Comparator<Object>) comparator);
        return new ImmutableArray<>(newValues);
    }

    @Override
    public final @NotNull ImmutableArray<E> reversed() {
        final Object[] elements = this.elements;
        final int size = elements.length;
        if (size == 0) {
            return this;
        }
        Object[] res = new Object[size];
        for (int i = 0; i < size; i++) {
            res[i] = elements[size - i - 1];
        }
        return new ImmutableArray<>(res);
    }

    @Override
    public final @NotNull <U> ImmutableArray<@NotNull Tuple2<E, U>> zip(@NotNull Iterable<? extends U> other) {
        Iterator<? extends U> it = other.iterator(); // implicit null check of other
        if (!it.hasNext()) {
            return empty();
        }

        final Object[] elements = this.elements;
        final int size = elements.length;
        Object[] tmp = new Object[size];

        int i = 0;
        while (it.hasNext() && i < size) {
            tmp[i] = new Tuple2<>(elements[i], it.next());
            ++i;
        }

        if (i < size) {
            tmp = Arrays.copyOf(tmp, i);
        }
        return new ImmutableArray<>(tmp);
    }

    //@Override
    public final @NotNull Tuple2<@NotNull ImmutableArray<E>, @NotNull ImmutableArray<E>> span(@NotNull Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);

        final Object[] elements = this.elements;
        final int size = elements.length;

        if (size == 0) {
            return new Tuple2<>(empty(), empty());
        }

        int idx = 0;
        while (idx < size) {
            if (!predicate.test((E) elements[idx])) {
                break;
            }
            ++idx;
        }

        if (idx == 0) {
            return new Tuple2<>(empty(), this);
        } else if (idx == size) {
            return new Tuple2<>(this, empty());
        } else {
            return new Tuple2<>(
                    new ImmutableArray<>(Arrays.copyOfRange(elements, 0, idx)),
                    new ImmutableArray<>(Arrays.copyOfRange(elements, idx, size))
            );
        }

    }

    @Override
    public final @NotNull ImmutableArray<E> toImmutableArray() {
        return this;
    }

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
        public final ImmutableArray<E> from(@NotNull Iterator<? extends E> it) {
            return ImmutableArray.from(it);
        }

        @Override
        public final ImmutableArray<E> fill(int n, E value) {
            return ImmutableArray.fill(n, value);
        }

        @Override
        public final ImmutableArray<E> fill(int n, @NotNull Supplier<? extends E> supplier) {
            return ImmutableArray.fill(n, supplier);
        }

        @Override
        public final ImmutableArray<E> fill(int n, @NotNull IntFunction<? extends E> init) {
            return ImmutableArray.fill(n, init);
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
