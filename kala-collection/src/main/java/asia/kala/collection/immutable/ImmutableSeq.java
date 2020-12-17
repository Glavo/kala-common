package asia.kala.collection.immutable;

import asia.kala.Tuple2;
import asia.kala.annotations.Covariant;
import asia.kala.comparator.Comparators;
import asia.kala.factory.CollectionFactory;
import asia.kala.collection.Seq;
import asia.kala.function.IndexedFunction;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public interface ImmutableSeq<@Covariant E> extends ImmutableCollection<E>, Seq<E> {
    //region Narrow method

    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    static <E> ImmutableSeq<E> narrow(ImmutableSeq<? extends E> seq) {
        return (ImmutableSeq<E>) seq;
    }

    //endregion

    //region Static Factories

    @NotNull
    @SuppressWarnings("unchecked")
    static <E> CollectionFactory<E, ?, ? extends ImmutableSeq<E>> factory() {
        return (CollectionFactory<E, ?, ? extends ImmutableSeq<E>>) ImmutableSeqFactory.INSTANCE;
    }

    static <E> @NotNull ImmutableSeq<E> empty() {
        return ImmutableSeq0.instance();
    }

    static <E> @NotNull ImmutableSeq<E> of() {
        return empty();
    }

    static <E> @NotNull ImmutableSeq<E> of(E value1) {
        return new ImmutableSeq1<>(value1);
    }

    static <E> @NotNull ImmutableSeq<E> of(E value1, E values2) {
        return new ImmutableSeq2<>(value1, values2);
    }

    static <E> @NotNull ImmutableSeq<E> of(E value1, E values2, E value3) {
        return new ImmutableSeq3<>(value1, values2, value3);
    }

    static <E> @NotNull ImmutableSeq<E> of(E value1, E values2, E value3, E value4) {
        return new ImmutableSeq4<>(value1, values2, value3, value4);
    }

    static <E> @NotNull ImmutableSeq<E> of(E value1, E values2, E value3, E value4, E value5) {
        return new ImmutableSeq5<>(value1, values2, value3, value4, value5);
    }

    @SafeVarargs
    static <E> @NotNull ImmutableSeq<E> of(E... values) {
        return ImmutableSeq.<E>factory().from(values);
    }

    static <E> @NotNull ImmutableSeq<E> from(E @NotNull [] values) {
        return ImmutableSeq.<E>factory().from(values);
    }

    static <E> @NotNull ImmutableSeq<E> from(@NotNull Iterable<? extends E> values) {
        return ImmutableSeq.<E>factory().from(values);
    }

    static <E> @NotNull ImmutableSeq<E> from(@NotNull Iterator<? extends E> it) {
        return ImmutableSeq.<E>factory().from(it);
    }

    //endregion

    //region Collection Operations

    @Override
    default String className() {
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
    default @NotNull ImmutableSeq<E> updated(int index, E newValue) {
        return AbstractImmutableSeq.updated(this, index, newValue, iterableFactory());
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
    default @NotNull ImmutableSeq<E> concat(@NotNull Seq<? extends E> other) {
        return AbstractImmutableSeq.concat(this, other, iterableFactory());
    }

    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> appended(E value) {
        return AbstractImmutableSeq.appended(this, value, iterableFactory());
    }

    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> appendedAll(@NotNull Iterable<? extends E> postfix) {
        return AbstractImmutableSeq.prependedAll(this, postfix, iterableFactory());
    }

    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> appendedAll(E @NotNull [] postfix) {
        return AbstractImmutableSeq.prependedAll(this, postfix, iterableFactory());
    }

    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> prepended(E value) {
        return AbstractImmutableSeq.prepended(this, value, iterableFactory());
    }

    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> prependedAll(E @NotNull [] prefix) {
        return AbstractImmutableSeq.prependedAll(this, prefix, iterableFactory());
    }

    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> prependedAll(@NotNull Iterable<? extends E> prefix) {
        return AbstractImmutableSeq.prependedAll(this, prefix, iterableFactory());
    }

    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> sorted() {
        return sorted(Comparators.naturalOrder());
    }

    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> sorted(@NotNull Comparator<? super E> comparator) {
        return AbstractImmutableSeq.sorted(this, comparator, iterableFactory());
    }

    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> reversed() {
        return AbstractImmutableSeq.reversed(this, iterableFactory());
    }

    @Contract(pure = true)
    default <U> @NotNull ImmutableSeq<U> mapIndexed(@NotNull IndexedFunction<? super E, ? extends U> mapper) {
        return AbstractImmutableSeq.mapIndexed(this, mapper, this.<U>iterableFactory());
    }

    @Override
    default <U> @NotNull ImmutableSeq<U> map(@NotNull Function<? super E, ? extends U> mapper) {
        return AbstractImmutableCollection.map(this, mapper, this.<U>iterableFactory());
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

    @Override
    default <U> @NotNull ImmutableSeq<U> flatMap(@NotNull Function<? super E, ? extends Iterable<? extends U>> mapper) {
        return AbstractImmutableCollection.flatMap(this, mapper, iterableFactory());
    }

    @Override
    default @NotNull Tuple2<? extends ImmutableSeq<E>, ? extends ImmutableSeq<E>> span(@NotNull Predicate<? super E> predicate) {
        return AbstractImmutableCollection.span(this, predicate, iterableFactory());
    }

    @Override
    default @NotNull ImmutableSeq<E> toImmutableSeq() {
        return this;
    }

}
