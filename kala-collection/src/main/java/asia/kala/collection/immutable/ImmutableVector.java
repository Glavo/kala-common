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
import asia.kala.traversable.JavaArray;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;

public abstract class ImmutableVector<@Covariant E> extends AbstractImmutableSeq<E>
        implements IndexedSeq<E>, Serializable {

    ImmutableVector() {
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
        return ImmutableVector.<E>factory().from(values);
    }

    public static <E> @NotNull ImmutableVector<E> from(@NotNull Iterable<? extends E> values) {
        return ImmutableVector.<E>factory().from(values);
    }

    public static <E> @NotNull ImmutableVector<E> from(@NotNull Iterator<? extends E> it) {
        return ImmutableVector.<E>factory().from(it);
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
    }

}
