/*
 * Part of the implementation of ImmutableVector modified from vavr BitMappedTrie:
 * https://github.com/vavr-io/vavr/blob/master/src/main/java/io/vavr/collection/BitMappedTrie.java
 *
 * License:
 * https://github.com/vavr-io/vavr/blob/master/LICENSE
 */

package kala.collection.immutable;

import kala.collection.IndexedSeq;
import kala.collection.base.AnyTraversable;
import kala.collection.base.Traversable;
import kala.function.IndexedFunction;
import kala.tuple.Tuple2;
import kala.annotations.Covariant;
import kala.Conditions;
import kala.collection.factory.CollectionFactory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public abstract class ImmutableVector<@Covariant E> extends AbstractImmutableSeq<E>
        implements IndexedSeq<E>, Serializable {

    final Object[] prefix1;

    ImmutableVector(Object[] prefix1) {
        this.prefix1 = prefix1;
    }

    //region Narrow method

    @Contract(value = "_ -> param1", pure = true)
    static <E> ImmutableVector<E> narrow(ImmutableVector<? extends E> vector) {
        return (ImmutableVector<E>) vector;
    }

    //endregion

    //region Static Factories

    private static final Factory<?> FACTORY = new Factory<>();

    @SuppressWarnings("unchecked")
    public static <E> @NotNull CollectionFactory<E, ?, ImmutableVector<E>> factory() {
        return (Factory<E>) FACTORY;
    }

    public static <E> @NotNull Collector<E, ?, ImmutableVector<E>> collector() {
        return factory();
    }

    @SuppressWarnings("unchecked")
    public static <E> @NotNull ImmutableVector<E> empty() {
        return (ImmutableVector<E>) ImmutableVectors.Vector0.INSTANCE;
    }

    public static <E> @NotNull ImmutableVector<E> of() {
        return empty();
    }

    public static <E> @NotNull ImmutableVector<E> of(E value1) {
        return new ImmutableVectors.Vector1<>(new Object[]{value1});
    }

    public static <E> @NotNull ImmutableVector<E> of(E value1, E value2) {
        return new ImmutableVectors.Vector1<>(new Object[]{value1, value2});
    }

    public static <E> @NotNull ImmutableVector<E> of(E value1, E value2, E value3) {
        return new ImmutableVectors.Vector1<>(new Object[]{value1, value2, value3});
    }

    public static <E> @NotNull ImmutableVector<E> of(E value1, E value2, E value3, E value4) {
        return new ImmutableVectors.Vector1<>(new Object[]{value1, value2, value3, value4});
    }

    public static <E> @NotNull ImmutableVector<E> of(E value1, E value2, E value3, E value4, E value5) {
        return new ImmutableVectors.Vector1<>(new Object[]{value1, value2, value3, value4, value5});
    }

    @SafeVarargs
    public static <E> @NotNull ImmutableVector<E> of(E... values) {
        return from(values);
    }

    public static <E> @NotNull ImmutableVector<E> from(E @NotNull [] values) {
        final int size = values.length; // implicit null check of values
        if (size == 0) {
            return empty();
        }
        if (size <= ImmutableVectors.WIDTH) {
            Object[] res = new Object[size];
            System.arraycopy(values, 0, res, 0, size);
            return new ImmutableVectors.Vector1<>(res);
        }
        ImmutableVectors.VectorBuilder<E> builder = new ImmutableVectors.VectorBuilder<>();
        builder.addAll(values);
        return builder.build();
    }

    public static <E> @NotNull ImmutableVector<E> from(@NotNull java.util.Collection<? extends E> values) {
        final int size = values.size();
        if (size == 0) {
            return empty();
        }
        if (size <= ImmutableVectors.WIDTH) {
            Object[] res = new Object[size];
            res = values.toArray(res);
            return new ImmutableVectors.Vector1<>(res);
        }
        return from(values.iterator());
    }

    @SuppressWarnings("unchecked")
    public static <E> @NotNull ImmutableVector<E> from(@NotNull Traversable<? extends E> values) {
        if (values instanceof ImmutableVector<?>) {
            return ((ImmutableVector<E>) values);
        }
        final int knownSize = values.knownSize(); // implicit null check of values
        if (knownSize == 0) {
            return empty();
        }
        if (knownSize > 0 && knownSize <= ImmutableVectors.WIDTH) {
            if (values instanceof ImmutableArray<?>) {
                Object[] arr = ((ImmutableArray<? extends E>) values).getArray();
                if (arr.getClass() == Object[].class) {
                    return new ImmutableVectors.Vector1<>(arr);
                }
            }
            Object[] arr = new Object[knownSize];
            int cn = values.copyToArray(arr);
            assert cn == knownSize;
            return new ImmutableVectors.Vector1<>(arr);
        }
        return from(values.iterator());
    }

    @SuppressWarnings("unchecked")
    public static <E> @NotNull ImmutableVector<E> from(@NotNull Iterable<? extends E> values) {
        if (values instanceof Traversable<?>) {
            return from((Traversable<E>) values);
        }
        if (values instanceof java.util.Collection<?>) {
            return from(((Collection<E>) values));
        }
        return from(values.iterator());
    }

    public static <E> @NotNull ImmutableVector<E> from(@NotNull Iterator<? extends E> it) {
        if (!it.hasNext()) { // implicit null check of it
            return empty();
        }
        ImmutableVectors.VectorBuilder<E> builder = new ImmutableVectors.VectorBuilder<>();
        while (it.hasNext()) {
            builder.add(it.next());
        }
        return builder.build();
    }

    public static <E> @NotNull ImmutableVector<E> from(@NotNull Stream<? extends E> stream) {
        return stream.collect(factory());
    }

    public static <E> @NotNull ImmutableVector<E> fill(int n, E value) {
        if (n <= 0) {
            return empty();
        }
        if (n <= ImmutableVectors.WIDTH) {
            Object[] arr = new Object[n];
            if (value != null) {
                Arrays.fill(arr, value);
            }
            return new ImmutableVectors.Vector1<>(arr);
        }

        ImmutableVectors.VectorBuilder<E> builder = new ImmutableVectors.VectorBuilder<>();
        while (n-- > 0) {
            builder.add(value);
        }
        return builder.build();
    }

    public static <E> @NotNull ImmutableVector<E> fill(int n, @NotNull Supplier<? extends E> supplier) {
        if (n <= 0) {
            return empty();
        }

        if (n <= ImmutableVectors.WIDTH) {
            Object[] arr = new Object[n];
            for (int i = 0; i < n; i++) {
                arr[i] = supplier.get();
            }
            return new ImmutableVectors.Vector1<>(arr);
        }

        ImmutableVectors.VectorBuilder<E> builder = new ImmutableVectors.VectorBuilder<>();
        while (n-- > 0) {
            builder.add(supplier.get());
        }
        return builder.build();
    }

    public static <E> @NotNull ImmutableVector<E> fill(int n, @NotNull IntFunction<? extends E> init) {
        if (n <= 0) {
            return empty();
        }

        if (n <= ImmutableVectors.WIDTH) {
            Object[] arr = new Object[n];
            for (int i = 0; i < n; i++) {
                arr[i] = init.apply(i);
            }
            return new ImmutableVectors.Vector1<>(arr);
        }

        ImmutableVectors.VectorBuilder<E> builder = new ImmutableVectors.VectorBuilder<>();
        for (int i = 0; i < n; i++) {
            builder.add(init.apply(i));
        }
        return builder.build();
    }

    //endregion

    abstract int vectorSliceCount();

    abstract Object[] vectorSlice(int idx);

    abstract int vectorSlicePrefixLength(int idx);

    //region Collection Operations

    @Override
    public final @NotNull String className() {
        return "ImmutableVector";
    }

    @Override
    public final <U> @NotNull CollectionFactory<U, ?, ImmutableVector<U>> iterableFactory() {
        return factory();
    }

    //endregion

    @Override
    public final boolean isEmpty() {
        return this == ImmutableVectors.Vector0.INSTANCE;
    }

    //region Addition Operations

    @Override
    public @NotNull ImmutableVector<E> appended(E value) {
        ImmutableVectors.VectorBuilder<E> builder = new ImmutableVectors.VectorBuilder<>();
        builder.initFrom(this);
        builder.add(value);
        return builder.build();
    }

    @Override
    public @NotNull ImmutableVector<E> appendedAll(E @NotNull [] values) {
        ImmutableVectors.VectorBuilder<E> builder = new ImmutableVectors.VectorBuilder<>();
        builder.initFrom(this);
        builder.addAll(values);
        return builder.build();
    }

    public @NotNull ImmutableVector<E> appendedAll(@NotNull ImmutableVector<? extends E> values) {
        if (values == ImmutableVectors.Vector0.INSTANCE) {
            return this;
        }
        ImmutableVectors.VectorBuilder<E> builder = new ImmutableVectors.VectorBuilder<>();
        builder.initFrom(this);
        builder.addVector(values);
        return builder.build();
    }

    @Override
    public @NotNull ImmutableVector<E> appendedAll(@NotNull Iterable<? extends E> values) {
        if (values instanceof ImmutableVector<?>) {
            return appendedAll(((ImmutableVector<E>) values));
        }
        if (AnyTraversable.knownSize(values) == 0) {
            return this;
        }
        ImmutableVectors.VectorBuilder<E> builder = new ImmutableVectors.VectorBuilder<>();
        builder.initFrom(this);
        builder.addAll(values);
        return builder.build();
    }

    @Override
    public @NotNull ImmutableVector<E> prepended(E value) {
        ImmutableVectors.VectorBuilder<E> builder = new ImmutableVectors.VectorBuilder<>();
        builder.add(value);
        builder.addVector(this);
        return builder.build();
    }

    @Override
    public @NotNull ImmutableVector<E> prependedAll(E @NotNull [] values) {
        ImmutableVectors.VectorBuilder<E> builder = new ImmutableVectors.VectorBuilder<>();
        builder.addAll(values);
        builder.addVector(this);
        return builder.build();
    }

    public @NotNull ImmutableVector<E> prependedAll(@NotNull ImmutableVector<? extends E> values) {
        if (values == ImmutableVectors.Vector0.INSTANCE) {
            return this;
        }
        ImmutableVectors.VectorBuilder<E> builder = new ImmutableVectors.VectorBuilder<>();
        builder.initFrom(values);
        builder.addVector(this);
        return builder.build();
    }

    @Override
    public @NotNull ImmutableVector<E> prependedAll(@NotNull Iterable<? extends E> values) {
        if (values instanceof ImmutableVector<?>) {
            return prependedAll(((ImmutableVector<E>) values));
        }
        if (AnyTraversable.knownSize(values) == 0) {
            return this;
        }
        ImmutableVectors.VectorBuilder<E> builder = new ImmutableVectors.VectorBuilder<>();
        builder.addAll(values);
        builder.addVector(this);
        return builder.build();
    }

    //endregion

    abstract @NotNull ImmutableVector<E> slice0(int lo, int hi);

    @Override
    public final @NotNull ImmutableVector<E> slice(int beginIndex, int endIndex) {
        final int size = this.size();
        Conditions.checkPositionIndices(beginIndex, endIndex, size);
        final int newSize = endIndex - beginIndex;
        if (newSize == 0) {
            return ImmutableVector.empty();
        }
        if (newSize == size) {
            return this;
        }
        return slice0(beginIndex, endIndex);
    }

    @Override
    public final @NotNull ImmutableVector<E> drop(int n) {
        if (n <= 0) {
            return this;
        }
        final int size = this.size();
        if (n >= size) {
            return ImmutableVector.empty();
        }

        return slice0(n, size);
    }

    @Override
    public @NotNull ImmutableVector<E> dropWhile(@NotNull Predicate<? super E> predicate) {
        return dropWhileImpl(predicate);
    }

    @Override
    public final @NotNull ImmutableVector<E> take(int n) {
        if (n <= 0) {
            return ImmutableVector.empty();
        }
        final int size = this.size();
        if (n >= size) {
            return this;
        }
        return slice0(0, n);
    }

    @Override
    public @NotNull ImmutableVector<E> takeWhile(@NotNull Predicate<? super E> predicate) {
        return takeWhileImpl(predicate);
    }

    @Override
    public @NotNull ImmutableVector<E> updated(int index, E newValue) {
        return updatedImpl(index, newValue);
    }

    abstract ImmutableVector<E> filterImpl(Predicate<? super E> predicate, boolean isFlipped);

    @Override
    public final @NotNull ImmutableVector<E> filter(@NotNull Predicate<? super E> predicate) {
        return filterImpl(predicate, false);
    }

    @Override
    public final @NotNull ImmutableVector<E> filterNot(@NotNull Predicate<? super E> predicate) {
        return filterImpl(predicate, true);
    }

    @Override
    public @NotNull <U> ImmutableVector<@NotNull U> filterIsInstance(@NotNull Class<? extends U> clazz) {
        return ((ImmutableVector<U>) filter(clazz::isInstance));
    }

    @Override
    public @NotNull <U> ImmutableVector<U> map(@NotNull Function<? super E, ? extends U> mapper) {
        ImmutableVectors.VectorBuilder<U> builder = new ImmutableVectors.VectorBuilder<>();
        for (E e : this) {
            builder.add(mapper.apply(e));
        }
        return builder.build();
    }

    @Override
    public @NotNull <U> ImmutableVector<U> mapIndexed(@NotNull IndexedFunction<? super E, ? extends U> mapper) {
        ImmutableVectors.VectorBuilder<U> builder = new ImmutableVectors.VectorBuilder<>();
        int idx = 0;
        for (E e : this) {
            builder.add(mapper.apply(idx++, e));
        }
        return builder.build();
    }

    @Override
    public @NotNull <U> ImmutableVector<U> flatMap(@NotNull Function<? super E, ? extends Iterable<? extends U>> mapper) {
        if (this == ImmutableVectors.Vector0.INSTANCE) {
            return ImmutableVector.empty();
        }
        ImmutableVectors.VectorBuilder<U> builder = new ImmutableVectors.VectorBuilder<>();
        for (E e : this) {
            builder.addAll(mapper.apply(e));
        }
        return builder.build();
    }

    @Override
    public @NotNull <U> ImmutableVector<@NotNull Tuple2<E, U>> zip(@NotNull Iterable<? extends U> other) {
        return zipImpl(other);
    }

    @Override
    public final @NotNull ImmutableVector<E> toImmutableVector() {
        return this;
    }

    static final class Factory<E> implements CollectionFactory<E, ImmutableVectors.VectorBuilder<E>, ImmutableVector<E>> {

        @Override
        public final ImmutableVector<E> empty() {
            return ImmutableVector.empty();
        }

        @Override
        public final ImmutableVectors.VectorBuilder<E> newBuilder() {
            return new ImmutableVectors.VectorBuilder<>();
        }

        @Override
        public final void addToBuilder(ImmutableVectors.@NotNull VectorBuilder<E> builder, E value) {
            builder.add(value);
        }

        @Override
        public final ImmutableVectors.VectorBuilder<E> mergeBuilder(ImmutableVectors.@NotNull VectorBuilder<E> builder1, ImmutableVectors.@NotNull VectorBuilder<E> builder2) {
            builder1.addVector(builder2.build());
            return builder1;
        }

        @Override
        public final ImmutableVector<E> build(ImmutableVectors.@NotNull VectorBuilder<E> builder) {
            return builder.build();
        }

        @Override
        public final ImmutableVector<E> from(E @NotNull [] values) {
            return ImmutableVector.from(values);
        }

        @Override
        public final ImmutableVector<E> from(@NotNull Iterable<? extends E> values) {
            return ImmutableVector.from(values);
        }

        @Override
        public final ImmutableVector<E> from(@NotNull Iterator<? extends E> it) {
            return ImmutableVector.from(it);
        }

        @Override
        public final ImmutableVector<E> fill(int n, E value) {
            return ImmutableVector.fill(n, value);
        }

        @Override
        public final ImmutableVector<E> fill(int n, @NotNull Supplier<? extends E> supplier) {
            return ImmutableVector.fill(n, supplier);
        }

        @Override
        public final ImmutableVector<E> fill(int n, @NotNull IntFunction<? extends E> init) {
            return ImmutableVector.fill(n, init);
        }
    }

}
