package asia.kala.collection.immutable;

import asia.kala.Tuple2;
import asia.kala.annotations.Covariant;
import asia.kala.factory.CollectionFactory;
import asia.kala.collection.Seq;
import asia.kala.factory.CollectionFactory;
import asia.kala.function.IndexedFunction;
import org.jetbrains.annotations.ApiStatus;
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

    //region Factory methods

    @NotNull
    @SuppressWarnings("unchecked")
    static <E> CollectionFactory<E, ?, ? extends ImmutableSeq<E>> factory() {
        return (CollectionFactory<E, ?, ? extends ImmutableSeq<E>>) ImmutableSeqFactory.INSTANCE;
    }

    @NotNull
    static <E> ImmutableSeq<E> empty() {
        return ImmutableSeq.<E>factory().empty();
    }

    @NotNull
    static <E> ImmutableSeq<E> of() {
        return empty();
    }

    @NotNull
    static <E> ImmutableSeq<E> of(E value1) {
        return new ImmutableSeq1<>(value1);
    }

    @NotNull
    static <E> ImmutableSeq<E> of(E value1, E values2) {
        return new ImmutableSeq2<>(value1, values2);
    }

    @NotNull
    static <E> ImmutableSeq<E> of(E value1, E values2, E value3) {
        return new ImmutableSeq3<>(value1, values2, value3);
    }

    @NotNull
    static <E> ImmutableSeq<E> of(E value1, E values2, E value3, E value4) {
        return new ImmutableSeq4<>(value1, values2, value3, value4);
    }

    @NotNull
    static <E> ImmutableSeq<E> of(E value1, E values2, E value3, E value4, E value5) {
        return new ImmutableSeq5<>(value1, values2, value3, value4, value5);
    }

    @NotNull
    @SafeVarargs
    static <E> ImmutableSeq<E> of(E... values) {
        return ImmutableSeq.<E>factory().from(values);
    }

    @NotNull
    static <E> ImmutableSeq<E> from(E @NotNull [] values) {
        return ImmutableSeq.<E>factory().from(values);
    }

    @NotNull
    static <E> ImmutableSeq<E> from(@NotNull Iterable<? extends E> values) {
        return ImmutableSeq.<E>factory().from(values);
    }

    //endregion

    @NotNull
    @Contract(pure = true)
    default ImmutableSeq<E> updated(int index, E newValue) {
        return AbstractImmutableSeq.updated(this, index, newValue, iterableFactory());
    }

    @NotNull
    @Contract(pure = true)
    default ImmutableSeq<E> drop(int n) {
        return AbstractImmutableSeq.drop(this, n, iterableFactory());
    }

    @NotNull
    @Contract(pure = true)
    default ImmutableSeq<E> dropLast(int n) {
        return AbstractImmutableSeq.dropLast(this, n, iterableFactory());
    }

    @NotNull
    @Contract(pure = true)
    default ImmutableSeq<E> dropWhile(@NotNull Predicate<? super E> predicate) {
        return AbstractImmutableSeq.dropWhile(this, predicate, iterableFactory());
    }

    @NotNull
    @Contract(pure = true)
    default ImmutableSeq<E> take(int n) {
        return AbstractImmutableSeq.take(this, n, iterableFactory());
    }

    @NotNull
    default ImmutableSeq<E> takeLast(int n) {
        return AbstractImmutableSeq.takeLast(this, n, iterableFactory());
    }

    @NotNull
    @Contract(pure = true)
    default ImmutableSeq<E> takeWhile(@NotNull Predicate<? super E> predicate) {
        return AbstractImmutableSeq.takeWhile(this, predicate, iterableFactory());
    }

    @NotNull
    @Contract(pure = true)
    default ImmutableSeq<E> concat(@NotNull Seq<? extends E> other) {
        return AbstractImmutableSeq.concat(this, other, iterableFactory());
    }

    @NotNull
    @Contract(pure = true)
    default ImmutableSeq<E> prepended(E element) {
        return AbstractImmutableSeq.prepended(this, element, iterableFactory());
    }

    @NotNull
    @Contract(pure = true)
    default ImmutableSeq<E> prependedAll(@NotNull Iterable<? extends E> prefix) {
        return AbstractImmutableSeq.prependedAll(this, prefix, iterableFactory());
    }

    @NotNull
    @Contract(pure = true)
    default ImmutableSeq<E> prependedAll(E @NotNull [] prefix) {
        return AbstractImmutableSeq.prependedAll(this, prefix, iterableFactory());
    }

    @NotNull
    @Contract(pure = true)
    default ImmutableSeq<E> appended(E element) {
        return AbstractImmutableSeq.appended(this, element, iterableFactory());
    }

    @NotNull
    @Contract(pure = true)
    default ImmutableSeq<E> appendedAll(@NotNull Iterable<? extends E> postfix) {
        return AbstractImmutableSeq.prependedAll(this, postfix, iterableFactory());
    }

    @NotNull
    @Contract(pure = true)
    default ImmutableSeq<E> appendedAll(E @NotNull [] postfix) {
        return AbstractImmutableSeq.prependedAll(this, postfix, iterableFactory());
    }

    @NotNull
    @Contract(pure = true)
    @SuppressWarnings("unchecked")
    default ImmutableSeq<E> sorted() {
        return sorted((Comparator<? super E>) Comparator.naturalOrder());
    }

    @NotNull
    @Contract(pure = true)
    default ImmutableSeq<E> sorted(@NotNull Comparator<? super E> comparator) {
        return AbstractImmutableSeq.sorted(this, comparator, iterableFactory());
    }

    @NotNull
    @Contract(pure = true)
    default ImmutableSeq<E> reversed() {
        return AbstractImmutableSeq.reversed(this, iterableFactory());
    }

    @NotNull
    @Contract(pure = true)
    default <U> ImmutableSeq<U> mapIndexed(@NotNull IndexedFunction<? super E, ? extends U> mapper) {
        return AbstractImmutableSeq.mapIndexed(this, mapper, this.<U>iterableFactory());
    }

    //region ImmutableCollection members

    @Override
    default String className() {
        return "ImmutableSeq";
    }

    @NotNull
    @Override
    default <U> CollectionFactory<U, ?, ? extends ImmutableSeq<U>> iterableFactory() {
        return factory();
    }

    @NotNull
    @Override
    @Unmodifiable
    default List<E> asJava() {
        return Seq.super.asJava();
    }

    @NotNull
    @Override
    default <U> ImmutableSeq<U> map(@NotNull Function<? super E, ? extends U> mapper) {
        return AbstractImmutableCollection.map(this, mapper, this.<U>iterableFactory());
    }

    @NotNull
    @Override
    default ImmutableSeq<E> filter(@NotNull Predicate<? super E> predicate) {
        return AbstractImmutableCollection.filter(this, predicate, iterableFactory());
    }

    @NotNull
    @Override
    default ImmutableSeq<E> filterNot(@NotNull Predicate<? super E> predicate) {
        return AbstractImmutableCollection.filterNot(this, predicate, iterableFactory());
    }

    @NotNull
    @Override
    default ImmutableSeq<@NotNull E> filterNotNull() {
        return this.filter(Objects::nonNull);
    }

    @NotNull
    @Override
    default <U> ImmutableSeq<U> flatMap(@NotNull Function<? super E, ? extends Iterable<? extends U>> mapper) {
        return AbstractImmutableCollection.flatMap(this, mapper, iterableFactory());
    }

    @NotNull
    @Override
    default Tuple2<? extends ImmutableSeq<E>, ? extends ImmutableSeq<E>> span(@NotNull Predicate<? super E> predicate) {
        return AbstractImmutableCollection.span(this, predicate, iterableFactory());
    }

    @NotNull
    @Override
    default ImmutableSeq<E> toImmutableSeq() {
        return this;
    }

    //endregion

}
