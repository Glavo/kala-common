package org.glavo.kala.collection;

import org.glavo.kala.collection.base.Traversable;
import org.glavo.kala.collection.immutable.ImmutableArray;
import org.glavo.kala.collection.mutable.ArrayBuffer;
import org.glavo.kala.collection.factory.CollectionFactory;
import org.glavo.kala.collection.base.GenericArrays;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Debug;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.*;
import java.util.function.*;

@SuppressWarnings("unchecked")
@Debug.Renderer(hasChildren = "!isEmpty()", childrenArray = "elements")
public class ArraySeq<E> extends ArraySeqLike<E> implements Seq<E>, IndexedSeq<E>, Serializable {
    private static final long serialVersionUID = 4981379062449237945L;

    public static final ArraySeq<?> EMPTY = new ArraySeq<>(GenericArrays.EMPTY_OBJECT_ARRAY);

    private static final ArraySeq.Factory<?> FACTORY = new ArraySeq.Factory<>();

    protected ArraySeq(Object @NotNull [] elements) {
        super(elements);
    }

    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    public static <E> ArraySeq<E> narrow(ArraySeq<? extends E> seq) {
        return (ArraySeq<E>) seq;
    }

    //region Static Factories

    public static <E> @NotNull CollectionFactory<E, ?, ? extends ArraySeq<E>> factory() {
        return (Factory<E>) FACTORY;
    }

    @Contract("_ -> new")
    public static <E> @NotNull ArraySeq<E> wrap(@NotNull E[] array) {
        Objects.requireNonNull(array);
        return new ArraySeq<>(array);
    }

    public static <E> @NotNull ArraySeq<E> empty() {
        return (ArraySeq<E>) EMPTY;
    }

    public static <E> @NotNull ArraySeq<E> of() {
        return empty();
    }

    @Contract(value = "_ -> new", pure = true)
    public static <E> @NotNull ArraySeq<E> of(E value1) {
        return new ArraySeq<>(new Object[]{value1});
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static <E> @NotNull ArraySeq<E> of(E value1, E value2) {
        return new ArraySeq<>(new Object[]{value1, value2});
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public static <E> @NotNull ArraySeq<E> of(E value1, E value2, E value3) {
        return new ArraySeq<>(new Object[]{value1, value2, value3});
    }

    @Contract(value = "_, _, _, _ -> new", pure = true)
    public static <E> @NotNull ArraySeq<E> of(E value1, E value2, E value3, E value4) {
        return new ArraySeq<>(new Object[]{value1, value2, value3, value4});
    }

    @Contract(value = "_, _, _, _, _ -> new", pure = true)
    public static <E> @NotNull ArraySeq<E> of(E value1, E value2, E value3, E value4, E value5) {
        return new ArraySeq<>(new Object[]{value1, value2, value3, value4, value5});
    }

    @SafeVarargs
    @Contract(pure = true)
    public static <E> @NotNull ArraySeq<E> of(E... values) {
        return from(values);
    }

    @Contract(pure = true)
    public static <E> @NotNull ArraySeq<E> from(E @NotNull [] values) {
        return values.length == 0
                ? empty()
                : new ArraySeq<>(values.clone());
    }

    public static <E> @NotNull ArraySeq<E> from(@NotNull Traversable<? extends E> values) {
        if (values instanceof ImmutableArray<?>) {
            return (ArraySeq<E>) values;
        }

        if (values.knownSize() == 0) { // implicit null check of values
            return empty();
        }

        Object[] arr = values.toArray();
        if (arr.length == 0) {
            return empty();
        }
        return new ArraySeq<>(arr);
    }

    public static <E> @NotNull ArraySeq<E> from(@NotNull java.util.Collection<? extends E> values) {
        return values.size() == 0
                ? empty()
                : new ArraySeq<>(values.toArray());
    }

    public static <E> @NotNull ArraySeq<E> from(@NotNull Iterable<? extends E> values) {
        Objects.requireNonNull(values);

        if (values instanceof Traversable<?>) {
            return from((Traversable<E>) values);
        }
        if (values instanceof java.util.Collection<?>) {
            return from(((java.util.Collection<E>) values));
        }

        return new ArraySeq<>(ArrayBuffer.<E>from(values).toArray());
    }

    public static <E> @NotNull ArraySeq<E> from(@NotNull Iterator<? extends E> it) {
        if (!it.hasNext()) { // implicit null check of it
            return empty();
        }
        return new ArraySeq<>(ArrayBuffer.<E>from(it).toArray());
    }

    public static <E> @NotNull ArraySeq<E> fill(int n, E value) {
        if (n <= 0) {
            return empty();
        }

        Object[] ans = new Object[n];
        if (value != null) {
            Arrays.fill(ans, value);
        }
        return new ArraySeq<>(ans);
    }

    public static <E> @NotNull ArraySeq<E> fill(int n, @NotNull Supplier<? extends E> supplier) {
        if (n <= 0) {
            return empty();
        }

        Object[] ans = new Object[n];
        for (int i = 0; i < n; i++) {
            ans[i] = supplier.get();
        }
        return new ArraySeq<>(ans);
    }

    public static <E> @NotNull ArraySeq<E> fill(int n, @NotNull IntFunction<? extends E> init) {
        if (n <= 0) {
            return empty();
        }

        Object[] ans = new Object[n];
        for (int i = 0; i < n; i++) {
            ans[i] = init.apply(i);
        }
        return new ArraySeq<>(ans);
    }

    //endregion

    @Override
    public final int hashCode() {
        int ans = 0;
        for (Object o : elements) {
            ans = ans * 31 + Objects.hashCode(o);
        }
        return ans + Collection.SEQ_HASH_MAGIC;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Seq)) {
            return false;
        }
        return Seq.equals(this, ((Seq<?>) obj));
    }

    private static final class Factory<E> implements CollectionFactory<E, ArrayBuffer<E>, ArraySeq<E>> {

        @Override
        public final ArraySeq<E> empty() {
            return ArraySeq.empty();
        }

        @Override
        public final ArraySeq<E> from(E @NotNull [] values) {
            return ArraySeq.from(values);
        }

        @Override
        public final ArraySeq<E> from(@NotNull Iterable<? extends E> values) {
            return ArraySeq.from(values);
        }

        @Override
        public final ArraySeq<E> from(@NotNull Iterator<? extends E> it) {
            return ArraySeq.from(it);
        }

        @Override
        public final ArraySeq<E> fill(int n, E value) {
            return ArraySeq.fill(n, value);
        }

        @Override
        public final ArraySeq<E> fill(int n, @NotNull Supplier<? extends E> supplier) {
            return ArraySeq.fill(n, supplier);
        }

        @Override
        public final ArraySeq<E> fill(int n, @NotNull IntFunction<? extends E> init) {
            return ArraySeq.fill(n, init);
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
        public final ArrayBuffer<E> mergeBuilder(@NotNull ArrayBuffer<E> builder1, @NotNull ArrayBuffer<E> builder2) {
            builder1.appendAll(builder2);
            return builder1;
        }

        @Override
        public final void sizeHint(@NotNull ArrayBuffer<E> buffer, int size) {
            buffer.sizeHint(size);
        }

        @Override
        public final ArraySeq<E> build(@NotNull ArrayBuffer<E> buffer) {
            return new ArraySeq<>(buffer.toArray());
        }
    }
}
