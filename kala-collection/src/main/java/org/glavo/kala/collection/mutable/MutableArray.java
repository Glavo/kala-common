package org.glavo.kala.collection.mutable;

import org.glavo.kala.function.IndexedFunction;
import org.glavo.kala.traversable.Traversable;
import org.glavo.kala.factory.CollectionFactory;
import org.glavo.kala.traversable.JavaArray;
import org.glavo.kala.collection.ArraySeq;
import org.glavo.kala.collection.IndexedSeq;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public final class MutableArray<E> extends ArraySeq<E> implements MutableSeq<E>, IndexedSeq<E>, Serializable {
    private static final long serialVersionUID = 6278999671163491762L;

    public static final MutableArray<?> EMPTY = new MutableArray<>(JavaArray.EMPTY_OBJECT_ARRAY);

    private static final MutableArray.Factory<?> FACTORY = new Factory<>();

    private final boolean isChecked;

    //region Constructors

    MutableArray(@NotNull Object[] array) {
        this(array, false);
    }

    MutableArray(@NotNull Object[] array, boolean isChecked) {
        super(array);
        this.isChecked = isChecked;
    }

    public MutableArray(int size) {
        this(new Object[size]);
    }

    //endregion

    //region Static Factories

    public static <E> @NotNull CollectionFactory<E, ?, MutableArray<E>> factory() {
        return (Factory<E>) FACTORY;
    }

    public static <E> @NotNull MutableArray<E> empty() {
        return (MutableArray<E>) EMPTY;
    }

    public static <E> @NotNull MutableArray<E> of() {
        return (MutableArray<E>) EMPTY;
    }

    @Contract("_ -> new")
    public static <E> @NotNull MutableArray<E> of(E value1) {
        return new MutableArray<>(new Object[]{value1});
    }

    @Contract("_, _ -> new")
    public static <E> @NotNull MutableArray<E> of(E value1, E value2) {
        return new MutableArray<>(new Object[]{value1, value2});
    }

    @Contract("_, _, _ -> new")
    public static <E> @NotNull MutableArray<E> of(E value1, E value2, E value3) {
        return new MutableArray<>(new Object[]{value1, value2, value3});
    }

    @Contract("_, _, _, _ -> new")
    public static <E> @NotNull MutableArray<E> of(E value1, E value2, E value3, E value4) {
        return new MutableArray<>(new Object[]{value1, value2, value3, value4});
    }

    @Contract("_, _, _, _, _ -> new")
    public static <E> @NotNull MutableArray<E> of(E value1, E value2, E value3, E value4, E value5) {
        return new MutableArray<>(new Object[]{value1, value2, value3, value4, value5});
    }

    public static <E> @NotNull MutableArray<E> of(E... values) {
        return from(values);
    }

    public static <E> @NotNull MutableArray<E> from(E @NotNull [] values) {
        final int length = values.length; // implicit null check of values+
        if (length == 0) {
            return empty();
        }

        Object[] newValues = new Object[length];
        System.arraycopy(values, 0, newValues, 0, length);
        return new MutableArray<>(newValues);
    }

    public static <E> @NotNull MutableArray<E> from(@NotNull Traversable<? extends E> values) {
        return values.knownSize() == 0 // implicit null check of values
                ? empty()
                : new MutableArray<>(values.toArray());

    }

    public static <E> @NotNull MutableArray<E> from(@NotNull java.util.Collection<? extends E> values) {
        return values.size() == 0 // implicit null check of values
                ? empty()
                : new MutableArray<>(values.toArray());

    }

    public static <E> @NotNull MutableArray<E> from(@NotNull Iterable<? extends E> values) {
        Objects.requireNonNull(values);

        if (values instanceof Traversable<?>) {
            return from((Traversable<E>) values);
        }

        if (values instanceof java.util.Collection<?>) {
            return from(((java.util.Collection<E>) values));
        }

        return from(values.iterator());
    }

    public static <E> @NotNull MutableArray<E> from(@NotNull Iterator<? extends E> it) {
        if (!it.hasNext()) { // implicit null check of it
            return empty();
        }
        ArrayBuffer<E> buffer = new ArrayBuffer<>();
        while (it.hasNext()) {
            buffer.append(it.next());
        }
        return new MutableArray<>(buffer.toArray());
    }

    public static <E> @NotNull MutableArray<E> fill(int n, E value) {
        if (n <= 0) {
            return empty();
        }

        Object[] ans = new Object[n];
        if (value != null) {
            Arrays.fill(ans, value);
        }
        return new MutableArray<>(ans);
    }

    public static <E> @NotNull MutableArray<E> fill(int n, @NotNull Supplier<? extends E> supplier) {
        if (n <= 0) {
            return empty();
        }

        Object[] ans = new Object[n];
        for (int i = 0; i < n; i++) {
            ans[i] = supplier.get();
        }
        return new MutableArray<>(ans);
    }

    public static <E> @NotNull MutableArray<E> fill(int n, @NotNull IntFunction<? extends E> init) {
        if (n <= 0) {
            return empty();
        }

        Object[] ans = new Object[n];
        for (int i = 0; i < n; i++) {
            ans[i] = init.apply(i);
        }
        return new MutableArray<>(ans);
    }

    public static <E> @NotNull MutableArray<E> wrap(E @NotNull [] array) {
        Objects.requireNonNull(array);
        return new MutableArray<>(array, true);
    }

    //endregion

    //region Collection Operations

    @Override
    public final String className() {
        return "MutableArray";
    }

    @Override
    public final <U> @NotNull CollectionFactory<U, ?, MutableArray<U>> iterableFactory() {
        return factory();
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public final @NotNull MutableArray<E> clone() {
        return new MutableArray<E>(this.elements.clone(), isChecked);
    }

    //endregion

    public final Object @NotNull [] getArray() {
        return elements;
    }

    public final boolean isChecked() {
        return isChecked;
    }

    @Override
    public final void set(int index, E newValue) {
        try {
            elements[index] = newValue;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException(e.getMessage());
        }
    }

    @Override
    public final void mapInPlace(@NotNull Function<? super E, ? extends E> mapper) {
        final Object[] elements = this.elements;
        for (int i = 0; i < elements.length; i++) {
            elements[i] = mapper.apply((E) elements[i]);
        }
    }

    @Override
    public final void mapInPlaceIndexed(@NotNull IndexedFunction<? super E, ? extends E> mapper) {
        final Object[] elements = this.elements;
        for (int i = 0; i < elements.length; i++) {
            elements[i] = mapper.apply(i, (E) elements[i]);
        }
    }

    @Override
    public final void sort(@NotNull Comparator<? super E> comparator) {
        Arrays.sort(elements, (Comparator<? super Object>) comparator);
    }


    private static final class Factory<E> implements CollectionFactory<E, ArrayBuffer<E>, MutableArray<E>> {
        Factory() {
        }

        @Override
        public final MutableArray<E> from(E @NotNull [] values) {
            return MutableArray.from(values);
        }

        @Override
        public final MutableArray<E> from(@NotNull Iterable<? extends E> values) {
            return MutableArray.from(values);
        }

        @Override
        public @NotNull MutableArray<E> from(@NotNull Iterator<? extends E> it) {
            return MutableArray.from(it);
        }

        @Override
        public final MutableArray<E> fill(int n, E value) {
            return MutableArray.fill(n, value);
        }

        @Override
        public final MutableArray<E> fill(int n, @NotNull Supplier<? extends E> supplier) {
            return MutableArray.fill(n, supplier);
        }

        @Override
        public final MutableArray<E> fill(int n, @NotNull IntFunction<? extends E> init) {
            return MutableArray.fill(n, init);
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
        public final ArrayBuffer<E> mergeBuilder(@NotNull ArrayBuffer<E> builder1, @NotNull ArrayBuffer<E> builder2) {
            builder1.appendAll(builder2);
            return builder1;
        }

        @Override
        public final MutableArray<E> build(@NotNull ArrayBuffer<E> buffer) {
            return new MutableArray<>(buffer.toArray());
        }
    }
}