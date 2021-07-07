package kala.collection.immutable;

import kala.collection.Collection;
import kala.collection.FullCollectionLike;
import kala.function.CheckedFunction;
import kala.function.CheckedPredicate;
import kala.function.Predicates;
import kala.tuple.Tuple2;
import kala.annotations.Covariant;
import kala.collection.factory.CollectionFactory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public interface ImmutableCollection<@Covariant E> extends Collection<E>, FullCollectionLike<E> {

    //region Narrow method

    @Contract(value = "_ -> param1", pure = true)
    static <E> ImmutableCollection<E> narrow(ImmutableCollection<? extends E> collection) {
        return (ImmutableCollection<E>) collection;
    }

    //endregion

    //region Static Factories

    static <E> @NotNull CollectionFactory<E, ?, ImmutableCollection<E>> factory() {
        return CollectionFactory.narrow(ImmutableSeq.factory());
    }

    static <E> @NotNull Collector<E, ?, ImmutableCollection<E>> collector() {
        return factory();
    }

    static <E> @NotNull ImmutableCollection<E> of() {
        return ImmutableSeq.of();
    }

    static <E> @NotNull ImmutableCollection<E> of(E value1) {
        return ImmutableSeq.of(value1);
    }

    static <E> @NotNull ImmutableCollection<E> of(E value1, E value2) {
        return ImmutableSeq.of(value1, value2);
    }

    static <E> @NotNull ImmutableCollection<E> of(E value1, E value2, E value3) {
        return ImmutableSeq.of(value1, value2, value3);
    }

    static <E> @NotNull ImmutableCollection<E> of(E value1, E value2, E value3, E value4) {
        return ImmutableSeq.of(value1, value2, value3, value4);
    }

    static <E> @NotNull ImmutableCollection<E> of(E value1, E value2, E value3, E value4, E value5) {
        return ImmutableSeq.of(value1, value2, value3, value4, value5);
    }

    @SafeVarargs
    static <E> @NotNull ImmutableCollection<E> of(E... values) {
        return ImmutableSeq.from(values);
    }

    static <E> @NotNull ImmutableCollection<E> from(E @NotNull [] values) {
        return ImmutableSeq.from(values);
    }

    static <E> @NotNull ImmutableCollection<E> from(@NotNull Iterable<? extends E> values) {
        return ImmutableSeq.from(values);
    }

    static <E> @NotNull ImmutableCollection<E> from(@NotNull Iterator<? extends E> it) {
        return ImmutableSeq.from(it);
    }

    static <E> @NotNull ImmutableCollection<E> from(@NotNull Stream<? extends E> stream) {
        return ImmutableSeq.from(stream);
    }

    static <E> @NotNull ImmutableCollection<E> fill(int n, E value) {
        return ImmutableSeq.fill(n, value);
    }

    static <E> @NotNull ImmutableCollection<E> fill(int n, @NotNull Supplier<? extends E> supplier) {
        return ImmutableSeq.fill(n, supplier);
    }

    static <E> @NotNull ImmutableCollection<E> fill(int n, @NotNull IntFunction<? extends E> init) {
        return ImmutableSeq.fill(n, init);
    }

    //endregion

    //region Collection Operations

    @Override
    default @NotNull String className() {
        return "ImmutableCollection";
    }

    @Override
    @Contract(pure = true)
    default <U> @NotNull CollectionFactory<U, ?, ? extends ImmutableCollection<U>> iterableFactory() {
        return factory();
    }

    @Override
    default @NotNull Spliterator<E> spliterator() {
        final int knownSize = knownSize();
        if (knownSize == 0) {
            return Spliterators.emptySpliterator();
        } else if (knownSize > 0) {
            return Spliterators.spliterator(iterator(), knownSize, Spliterator.IMMUTABLE);
        } else {
            return Spliterators.spliteratorUnknownSize(iterator(), Spliterator.IMMUTABLE);
        }
    }

    //endregion

    @Contract(pure = true)
    default <U> @NotNull ImmutableCollection<U> map(@NotNull Function<? super E, ? extends U> mapper) {
        return AbstractImmutableCollection.map(this, mapper, this.<U>iterableFactory());
    }

    @Contract(pure = true)
    default <U, Ex extends Throwable> @NotNull ImmutableCollection<U> mapChecked(
            @NotNull CheckedFunction<? super E, ? extends U, ? extends Ex> mapper) throws Ex {
        return map(mapper);
    }

    @Contract(pure = true)
    default <U> @NotNull ImmutableCollection<U> mapUnchecked(@NotNull CheckedFunction<? super E, ? extends U, ?> mapper) {
        return map(mapper);
    }

    @Contract(pure = true)
    default <U> @NotNull ImmutableCollection<@NotNull U> mapNotNull(@NotNull Function<? super E, ? extends @Nullable U> mapper) {
        return AbstractImmutableCollection.mapNotNull(this, mapper, this.<U>iterableFactory());
    }

    @Contract(pure = true)
    default <U, Ex extends Throwable> @NotNull ImmutableCollection<U> mapNotNullChecked(
            @NotNull CheckedFunction<? super E, ? extends U, ? extends Ex> mapper) throws Ex {
        return mapNotNull(mapper);
    }

    @Contract(pure = true)
    default <U> @NotNull ImmutableCollection<U> mapNotNullUnchecked(@NotNull CheckedFunction<? super E, ? extends U, ?> mapper) {
        return mapNotNull(mapper);
    }

    @Contract(pure = true)
    default @NotNull ImmutableCollection<E> filter(@NotNull Predicate<? super E> predicate) {
        return AbstractImmutableCollection.filter(this, predicate, iterableFactory());
    }

    default @NotNull <Ex extends Throwable> ImmutableCollection<E> filterChecked(
            @NotNull CheckedPredicate<? super E, ? extends Ex> predicate) throws Ex {
        return filter(predicate);
    }

    default @NotNull ImmutableCollection<E> filterUnchecked(
            @NotNull CheckedPredicate<? super E, ?> predicate) {
        return filter(predicate);
    }

    @Contract(pure = true)
    default @NotNull ImmutableCollection<E> filterNot(@NotNull Predicate<? super E> predicate) {
        return AbstractImmutableCollection.filterNot(this, predicate, iterableFactory());
    }

    default @NotNull <Ex extends Throwable> ImmutableCollection<E> filterNotChecked(
            @NotNull CheckedPredicate<? super E, ? extends Ex> predicate) throws Ex {
        return filterNot(predicate);
    }

    default @NotNull ImmutableCollection<E> filterNotUnchecked(
            @NotNull CheckedPredicate<? super E, ?> predicate) {
        return filterNot(predicate);
    }

    @Contract(pure = true)
    default @NotNull ImmutableCollection<@NotNull E> filterNotNull() {
        return this.filter(Predicates.isNotNull());
    }

    @Override
    default <U> @NotNull ImmutableCollection<@NotNull U> filterIsInstance(@NotNull Class<? extends U> clazz) {
        return (ImmutableCollection<U>) filter(clazz::isInstance);
    }

    @Contract(pure = true)
    default <U> @NotNull ImmutableCollection<U> flatMap(@NotNull Function<? super E, ? extends Iterable<? extends U>> mapper) {
        return AbstractImmutableCollection.flatMap(this, mapper, iterableFactory());
    }

    @Contract(pure = true)
    default <U, Ex extends Throwable> @NotNull ImmutableCollection<U> flatMapChecked(
            @NotNull CheckedFunction<? super E, ? extends Iterable<? extends U>, ? extends Ex> mapper) throws Ex {
        return flatMap(mapper);
    }

    @Contract(pure = true)
    default <U> @NotNull ImmutableCollection<U> flatMapUnchecked(
            @NotNull CheckedFunction<? super E, ? extends Iterable<? extends U>, ?> mapper) {
        return flatMap(mapper);
    }

    default <U> @NotNull ImmutableCollection<@NotNull Tuple2<E, U>> zip(@NotNull Iterable<? extends U> other) {
        return AbstractImmutableCollection.zip(this, other, this.<Tuple2<E, U>>iterableFactory());
    }
}
