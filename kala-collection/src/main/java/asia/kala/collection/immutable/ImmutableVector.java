/*
 * Part of the implementation of ImmutableVector modified from vavr BitMappedTrie:
 * https://github.com/vavr-io/vavr/blob/master/src/main/java/io/vavr/collection/BitMappedTrie.java
 *
 * License:
 * https://github.com/vavr-io/vavr/blob/master/LICENSE
 */

package asia.kala.collection.immutable;

import asia.kala.annotations.Covariant;
import asia.kala.collection.*;
import asia.kala.collection.mutable.ArrayBuffer;
import asia.kala.factory.CollectionFactory;
import asia.kala.function.IndexedFunction;
import asia.kala.traversable.JavaArray;
import asia.kala.traversable.Traversable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class ImmutableVector<@Covariant E> extends AbstractImmutableSeq<E>
        implements IndexedSeq<E>, Serializable {

    final Object[] prefix1;

    ImmutableVector(Object[] prefix1) {
        this.prefix1 = prefix1;
    }

    //region Narrow method

    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
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
            assert res.length == size;
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

    //endregion

    abstract int vectorSliceCount();

    abstract Object[] vectorSlice(int idx);

    abstract int vectorSlicePrefixLength(int idx);

    //region Collection Operations

    @Override
    public final String className() {
        return "ImmutableVector";
    }

    @Override
    public final <U> @NotNull CollectionFactory<U, ?, ImmutableVector<U>> iterableFactory() {
        return factory();
    }

    //endregion

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

    @Override
    public @NotNull ImmutableVector<E> appendedAll(@NotNull Iterable<? extends E> values) {
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

    @Override
    public @NotNull ImmutableVector<E> prependedAll(@NotNull Iterable<? extends E> values) {
        ImmutableVectors.VectorBuilder<E> builder = new ImmutableVectors.VectorBuilder<>();
        builder.addAll(values);
        builder.addVector(this);
        return builder.build();
    }

    //endregion


    @Override
    public @NotNull ImmutableVector<E> drop(int n) {
        return dropImpl(n);
    }

    @Override
    public @NotNull ImmutableVector<E> dropWhile(@NotNull Predicate<? super E> predicate) {
        return dropWhileImpl(predicate);
    }

    @Override
    public @NotNull ImmutableVector<E> take(int n) {
        return takeImpl(n);
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
    public @NotNull <U> ImmutableVector<U> map(@NotNull Function<? super E, ? extends U> mapper) {
        return mapImpl(mapper);
    }

    @Override
    public @NotNull <U> ImmutableVector<U> mapIndexed(@NotNull IndexedFunction<? super E, ? extends U> mapper) {
        return mapIndexedImpl(mapper);
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
        public final ImmutableVector<E> build(ImmutableVectors.@NotNull VectorBuilder<E> builder) {
            return builder.build();
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
    }

}
