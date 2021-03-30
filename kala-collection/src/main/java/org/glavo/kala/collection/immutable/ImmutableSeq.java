package org.glavo.kala.collection.immutable;

import org.glavo.kala.collection.*;
import org.glavo.kala.tuple.Tuple2;
import org.glavo.kala.annotations.Covariant;
import org.glavo.kala.comparator.Comparators;
import org.glavo.kala.collection.factory.CollectionFactory;
import org.glavo.kala.function.IndexedFunction;
import org.glavo.kala.tuple.primitive.IntObjTuple2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

@SuppressWarnings({"unchecked"})
public interface ImmutableSeq<@Covariant E> extends ImmutableCollection<E>, Seq<E>, FullSeqLike<E> {
    //region Narrow method

    @Contract(value = "_ -> param1", pure = true)
    static <E> ImmutableSeq<E> narrow(ImmutableSeq<? extends E> seq) {
        return (ImmutableSeq<E>) seq;
    }

    //endregion

    //region Static Factories

    static <E> @NotNull CollectionFactory<E, ?, ImmutableSeq<E>> factory() {
        return (ImmutableSeqs.Factory<E>) ImmutableSeqs.FACTORY;
    }

    public static <E> @NotNull Collector<E, ?, ImmutableSeq<E>> collector() {
        return factory();
    }

    static <E> @NotNull ImmutableSeq<E> empty() {
        return (ImmutableSeq<E>) ImmutableSeqs.Seq0.INSTANCE;
    }

    static <E> @NotNull ImmutableSeq<E> of() {
        return ImmutableSeq.empty();
    }

    static <E> @NotNull ImmutableSeq<E> of(E value1) {
        return new ImmutableSeqs.Seq1<>(value1);
    }

    static <E> @NotNull ImmutableSeq<E> of(E value1, E value2) {
        return new ImmutableSeqs.Seq2<>(value1, value2);
    }

    static <E> @NotNull ImmutableSeq<E> of(E value1, E value2, E value3) {
        return new ImmutableSeqs.Seq3<>(value1, value2, value3);
    }

    static <E> @NotNull ImmutableSeq<E> of(E value1, E value2, E value3, E value4) {
        return new ImmutableSeqs.Seq4<>(value1, value2, value3, value4);
    }

    static <E> @NotNull ImmutableSeq<E> of(E value1, E value2, E value3, E value4, E value5) {
        return new ImmutableSeqs.Seq5<>(value1, value2, value3, value4, value5);
    }

    @SafeVarargs
    static <E> @NotNull ImmutableSeq<E> of(E... values) {
        return ImmutableSeq.from(values);
    }

    static <E> @NotNull ImmutableSeq<E> from(E @NotNull [] values) {
        switch (values.length) {
            case 0:
                return ImmutableSeq.empty();
            case 1:
                return ImmutableSeq.of(values[0]);
            case 2:
                return ImmutableSeq.of(values[0], values[1]);
            case 3:
                return ImmutableSeq.of(values[0], values[1], values[2]);
            case 4:
                return ImmutableSeq.of(values[0], values[1], values[2], values[3]);
            case 5:
                return ImmutableSeq.of(values[0], values[1], values[2], values[3], values[4]);
        }
        return ImmutableVector.from(values);
    }

    static <E> @NotNull ImmutableSeq<E> from(@NotNull Iterable<? extends E> values) {
        return from(values.iterator()); // TODO
    }

    static <E> @NotNull ImmutableSeq<E> from(@NotNull Iterator<? extends E> it) {
        if (!it.hasNext()) {
            return ImmutableSeq.empty();
        }
        ImmutableVectors.VectorBuilder<E> builder = new ImmutableVectors.VectorBuilder<>();
        while (it.hasNext()) {
            builder.add(it.next());
        }
        return builder.buildSeq();
    }

    static <E> @NotNull ImmutableSeq<E> from(@NotNull Stream<? extends E> stream) {
        return stream.collect(factory());
    }

    static <E> @NotNull ImmutableSeq<E> fill(int n, E value) {
        if (n <= 0) {
            return ImmutableSeq.empty();
        }
        if (n == 1) {
            return new ImmutableSeqs.Seq1<>(value);
        }
        return new ImmutableSeqs.CopiesSeq<>(n, value);
    }

    static <E> @NotNull ImmutableSeq<E> fill(int n, @NotNull Supplier<? extends E> supplier) {
        if (n <= 0) {
            return ImmutableSeq.empty();
        }
        switch (n) {
            case 1:
                return ImmutableSeq.of(supplier.get());
            case 2:
                return ImmutableSeq.of(supplier.get(), supplier.get());
            case 3:
                return ImmutableSeq.of(supplier.get(), supplier.get(), supplier.get());
            case 4:
                return ImmutableSeq.of(supplier.get(), supplier.get(), supplier.get(), supplier.get());
            case 5:
                return ImmutableSeq.of(supplier.get(), supplier.get(), supplier.get(), supplier.get(), supplier.get());
        }
        return ImmutableVector.fill(n, supplier);
    }

    static <E> @NotNull ImmutableSeq<E> fill(int n, @NotNull IntFunction<? extends E> init) {
        if (n <= 0) {
            return ImmutableSeq.empty();
        }
        switch (n) {
            case 1:
                return ImmutableSeq.of(init.apply(0));
            case 2:
                return ImmutableSeq.of(init.apply(0), init.apply(1));
            case 3:
                return ImmutableSeq.of(init.apply(0), init.apply(1), init.apply(2));
            case 4:
                return ImmutableSeq.of(init.apply(0), init.apply(1), init.apply(2), init.apply(3));
            case 5:
                return ImmutableSeq.of(init.apply(0), init.apply(1), init.apply(2), init.apply(3), init.apply(4));
        }
        return ImmutableVector.fill(n, init);
    }

    //endregion

    //region Collection Operations

    @Override
    default @NotNull String className() {
        return "ImmutableSeq";
    }

    @Override
    default <U> @NotNull CollectionFactory<U, ?, ? extends ImmutableSeq<U>> iterableFactory() {
        return factory();
    }

    @Override
    default @NotNull @Unmodifiable List<E> asJava() {
        return Seq.super.asJava();
    }

    //endregion

    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> slice(int beginIndex, int endIndex) {
        return AbstractImmutableSeq.slice(this, beginIndex, endIndex, iterableFactory());
    }

    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> drop(int n) {
        return AbstractImmutableSeq.drop(this, n, iterableFactory());
    }

    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> dropLast(int n) {
        return AbstractImmutableSeq.dropLast(this, n, iterableFactory());
    }

    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> dropWhile(@NotNull Predicate<? super E> predicate) {
        return AbstractImmutableSeq.dropWhile(this, predicate, iterableFactory());
    }

    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> take(int n) {
        return AbstractImmutableSeq.take(this, n, iterableFactory());
    }

    default @NotNull ImmutableSeq<E> takeLast(int n) {
        return AbstractImmutableSeq.takeLast(this, n, iterableFactory());
    }

    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> takeWhile(@NotNull Predicate<? super E> predicate) {
        return AbstractImmutableSeq.takeWhile(this, predicate, iterableFactory());
    }

    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> updated(int index, E newValue) {
        return AbstractImmutableSeq.updated(this, index, newValue, iterableFactory());
    }

    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> concat(@NotNull SeqLike<? extends E> other) {
        return AbstractImmutableSeq.concat(this, other, iterableFactory());
    }

    @Override
    default @NotNull ImmutableSeq<E> concat(@NotNull List<? extends E> other) {
        return AbstractImmutableSeq.concat(this, other, iterableFactory());
    }

    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> prepended(E value) {
        return AbstractImmutableSeq.prepended(this, value, iterableFactory());
    }

    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> prependedAll(E @NotNull [] values) {
        return AbstractImmutableSeq.prependedAll(this, values, iterableFactory());
    }

    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> prependedAll(@NotNull Iterable<? extends E> values) {
        return AbstractImmutableSeq.prependedAll(this, values, iterableFactory());
    }

    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> appended(E value) {
        return AbstractImmutableSeq.appended(this, value, iterableFactory());
    }

    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> appendedAll(@NotNull Iterable<? extends E> values) {
        return AbstractImmutableSeq.appendedAll(this, values, iterableFactory());
    }

    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> appendedAll(E @NotNull [] values) {
        return AbstractImmutableSeq.appendedAll(this, values, iterableFactory());
    }

    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> sorted() {
        return sorted(Comparators.naturalOrder());
    }

    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> sorted(Comparator<? super E> comparator) {
        return AbstractImmutableSeq.sorted(this, comparator, iterableFactory());
    }

    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> reversed() {
        return AbstractImmutableSeq.reversed(this, iterableFactory());
    }

    @Override
    default @NotNull ImmutableSeq<E> filter(@NotNull Predicate<? super E> predicate) {
        return AbstractImmutableCollection.filter(this, predicate, iterableFactory());
    }

    @Override
    default @NotNull ImmutableSeq<E> filterNot(@NotNull Predicate<? super E> predicate) {
        return AbstractImmutableCollection.filterNot(this, predicate, iterableFactory());
    }

    @Override
    default @NotNull ImmutableSeq<@NotNull E> filterNotNull() {
        return this.filter(Objects::nonNull);
    }

    default <U> @NotNull ImmutableSeq<@NotNull U> filterIsInstance(@NotNull Class<? extends U> clazz) {
        return ((ImmutableSeq<U>) this.filter(clazz::isInstance));
    }

    @Override
    default <U> @NotNull ImmutableSeq<U> map(@NotNull Function<? super E, ? extends U> mapper) {
        return AbstractImmutableCollection.map(this, mapper, this.<U>iterableFactory());
    }

    @Contract(pure = true)
    default <U> @NotNull ImmutableSeq<U> mapIndexed(@NotNull IndexedFunction<? super E, ? extends U> mapper) {
        return AbstractImmutableSeq.mapIndexed(this, mapper, this.<U>iterableFactory());
    }

    @Override
    default <U> @NotNull ImmutableSeq<@NotNull U> mapNotNull(@NotNull Function<? super E, ? extends @Nullable U> mapper) {
        return AbstractImmutableCollection.mapNotNull(this, mapper, this.<U>iterableFactory());
    }

    default <U> @NotNull ImmutableSeq<@NotNull U> mapIndexedNotNull(@NotNull IndexedFunction<? super E, ? extends @Nullable U> mapper) {
        return AbstractImmutableSeq.mapIndexedNotNull(this, mapper, this.<U>iterableFactory());
    }

    @Override
    default <U> @NotNull ImmutableSeq<U> flatMap(@NotNull Function<? super E, ? extends Iterable<? extends U>> mapper) {
        return AbstractImmutableCollection.flatMap(this, mapper, iterableFactory());
    }

    @Override
    default <U> @NotNull ImmutableSeq<@NotNull Tuple2<E, U>> zip(@NotNull Iterable<? extends U> other) {
        return AbstractImmutableCollection.zip(this, other, this.<Tuple2<E, U>>iterableFactory());
    }

    @Override
    default @NotNull ImmutableSeq<E> toImmutableSeq() {
        return this;
    }

}
