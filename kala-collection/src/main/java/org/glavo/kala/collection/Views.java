package org.glavo.kala.collection;

import org.glavo.kala.Tuple2;
import org.glavo.kala.collection.immutable.ImmutableArray;
import org.glavo.kala.collection.immutable.ImmutableList;
import org.glavo.kala.collection.immutable.ImmutableSeq;
import org.glavo.kala.collection.immutable.ImmutableVector;
import org.glavo.kala.control.Option;
import org.glavo.kala.annotations.Covariant;
import org.glavo.kala.factory.CollectionFactory;
import org.glavo.kala.traversable.Iterators;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Stream;

final class Views {
    static class Of<@Covariant E, C extends Collection<E>> implements View<E> {
        protected final @NotNull C collection;

        Of(@NotNull C collection) {
            this.collection = collection;
        }

        //region Collection Operations

        @Override
        public final @NotNull Iterator<E> iterator() {
            return collection.iterator();
        }

        @Override
        public final @NotNull Spliterator<E> spliterator() {
            return collection.spliterator();
        }

        @Override
        public final @NotNull Stream<E> stream() {
            return collection.stream();
        }

        @Override
        public final @NotNull Stream<E> parallelStream() {
            return collection.parallelStream();
        }

        //endregion

        //region Size Info

        @Override
        public final boolean isEmpty() {
            return collection.isEmpty();
        }

        @Override
        public final int size() {
            return collection.size();
        }

        @Override
        public final int knownSize() {
            return collection.knownSize();
        }

        //endregion

        @Override
        public final @NotNull Option<E> find(@NotNull Predicate<? super E> predicate) {
            return collection.find(predicate);
        }

        //region Element Conditions

        @Override
        public final boolean contains(Object value) {
            return collection.contains(value);
        }

        @Override
        public final boolean containsAll(Object @NotNull [] values) {
            return collection.containsAll(values);
        }

        @Override
        public final boolean containsAll(@NotNull Iterable<?> values) {
            return collection.containsAll(values);
        }

        @Override
        public final boolean sameElements(@NotNull Iterable<?> other) {
            return collection.sameElements(other);
        }

        @Override
        public final boolean sameElements(@NotNull Iterable<?> other, boolean identity) {
            return collection.sameElements(other, identity);
        }

        @Override
        public final boolean anyMatch(@NotNull Predicate<? super E> predicate) {
            return collection.anyMatch(predicate);
        }

        @Override
        public final boolean allMatch(@NotNull Predicate<? super E> predicate) {
            return collection.allMatch(predicate);
        }

        @Override
        public final boolean noneMatch(@NotNull Predicate<? super E> predicate) {
            return collection.noneMatch(predicate);
        }

        //endregion

        //region Aggregate

        @Override
        public final int count(@NotNull Predicate<? super E> predicate) {
            return collection.count(predicate);
        }

        @Override
        public final E max() {
            return collection.max();
        }

        @Override
        public final E max(@NotNull Comparator<? super E> comparator) {
            return collection.max(comparator);
        }

        @Override
        public @Nullable E maxOrNull() {
            return collection.maxOrNull();
        }

        @Override
        public @Nullable E maxOrNull(@NotNull Comparator<? super E> comparator) {
            return collection.maxOrNull(comparator);
        }

        @Override
        public final @NotNull Option<E> maxOption() {
            return collection.maxOption();
        }

        @Override
        public final @NotNull Option<E> maxOption(@NotNull Comparator<? super E> comparator) {
            return collection.maxOption(comparator);
        }

        @Override
        public final E min() {
            return collection.min();
        }

        @Override
        public final E min(@NotNull Comparator<? super E> comparator) {
            return collection.min(comparator);
        }

        @Override
        public @Nullable E minOrNull() {
            return collection.minOrNull();
        }

        @Override
        public @Nullable E minOrNull(@NotNull Comparator<? super E> comparator) {
            return collection.minOrNull(comparator);
        }

        @Override
        public final @NotNull Option<E> minOption() {
            return collection.minOption();
        }

        @Override
        public final @NotNull Option<E> minOption(@NotNull Comparator<? super E> comparator) {
            return collection.minOption(comparator);
        }

        @Override
        public final E fold(E zero, @NotNull BiFunction<? super E, ? super E, ? extends E> op) {
            return collection.fold(zero, op);
        }

        @Override
        public final <U> U foldLeft(U zero, @NotNull BiFunction<? super U, ? super E, ? extends U> op) {
            return collection.foldLeft(zero, op);
        }

        @Override
        public final <U> U foldRight(U zero, @NotNull BiFunction<? super E, ? super U, ? extends U> op) {
            return collection.foldRight(zero, op);
        }

        @Override
        public final E reduce(@NotNull BiFunction<? super E, ? super E, ? extends E> op) throws NoSuchElementException {
            return collection.reduce(op);
        }

        @Override
        public final @NotNull Option<E> reduceOption(@NotNull BiFunction<? super E, ? super E, ? extends E> op) {
            return collection.reduceOption(op);
        }

        @Override
        public final E reduceLeft(@NotNull BiFunction<? super E, ? super E, ? extends E> op) throws NoSuchElementException {
            return collection.reduceLeft(op);
        }

        @Override
        public final @NotNull Option<E> reduceLeftOption(@NotNull BiFunction<? super E, ? super E, ? extends E> op) {
            return collection.reduceLeftOption(op);
        }

        @Override
        public final E reduceRight(@NotNull BiFunction<? super E, ? super E, ? extends E> op) throws NoSuchElementException {
            return collection.reduceRight(op);
        }

        @Override
        public final @NotNull Option<E> reduceRightOption(@NotNull BiFunction<? super E, ? super E, ? extends E> op) {
            return collection.reduceRightOption(op);
        }

        //endregion

        //region Conversion Operations

        @Override
        public final <R, Builder> R collect(@NotNull Collector<? super E, Builder, ? extends R> collector) {
            return collection.collect(collector);
        }

        @Override
        public final <R, Builder> R collect(@NotNull CollectionFactory<? super E, Builder, ? extends R> factory) {
            return collection.collect(factory);
        }

        @Override
        public final Object @NotNull [] toArray() {
            return collection.toArray();
        }

        @Override
        public final <U> U @NotNull [] toArray(@NotNull IntFunction<U[]> generator) {
            return collection.toArray(generator);
        }

        @Override
        public final <U> U @NotNull [] toArray(@NotNull Class<U> type) {
            return collection.toArray(type);
        }

        @Override
        public final @NotNull Seq<E> toSeq() {
            return collection.toSeq();
        }

        @Override
        public final @NotNull ImmutableSeq<E> toImmutableSeq() {
            return collection.toImmutableSeq();
        }

        @Override
        public final @NotNull ImmutableArray<E> toImmutableArray() {
            return collection.toImmutableArray();
        }

        @Override
        public final @NotNull ImmutableList<E> toImmutableList() {
            return collection.toImmutableList();
        }

        @Override
        public final @NotNull ImmutableVector<E> toImmutableVector() {
            return collection.toImmutableVector();
        }

        //endregion

        //region Traverse Operations

        @Override
        public final void forEach(@NotNull Consumer<? super E> action) {
            collection.forEach(action);
        }

        //endregion

        //region String Representation

        @Override
        public final <A extends Appendable> @NotNull A joinTo(@NotNull A buffer) {
            return collection.joinTo(buffer);
        }

        @Override
        public final <A extends Appendable> @NotNull A joinTo(@NotNull A buffer, @NotNull CharSequence separator) {
            return collection.joinTo(buffer, separator);
        }

        @Override
        public final <A extends Appendable> @NotNull A joinTo(@NotNull A buffer, @NotNull CharSequence separator, @NotNull CharSequence prefix, @NotNull CharSequence postfix) {
            return collection.joinTo(buffer, separator, prefix, postfix);
        }

        @Override
        public final @NotNull String joinToString() {
            return collection.joinToString();
        }

        @Override
        public final @NotNull String joinToString(@NotNull CharSequence separator) {
            return collection.joinToString(separator);
        }

        @Override
        public final @NotNull String joinToString(@NotNull CharSequence separator, @NotNull CharSequence prefix, @NotNull CharSequence postfix) {
            return collection.joinToString(separator, prefix, postfix);
        }


        @Override
        public final String toString() {
            return joinToString(", ", className() + "[", "]");
        }

        //endregion

    }

    static final class Mapped<@Covariant E, T> extends AbstractView<E> {

        private final @NotNull View<T> source;

        private final @NotNull Function<? super T, ? extends E> mapper;

        public Mapped(@NotNull View<T> source, @NotNull Function<? super T, ? extends E> mapper) {
            this.source = source;
            this.mapper = mapper;
        }

        //region Collection Operations

        @Override
        public final @NotNull Iterator<E> iterator() {
            return Iterators.map(source.iterator(), mapper);
        }

        @Override
        public final @NotNull Stream<E> stream() {
            return source.stream().map(mapper);
        }

        @Override
        public final @NotNull Stream<E> parallelStream() {
            return source.parallelStream().map(mapper);
        }

        //endregion

        //region Size Info

        @Override
        public final boolean isEmpty() {
            return source.isEmpty();
        }

        @Override
        public final int size() {
            return source.size();
        }

        @Override
        public final int knownSize() {
            return source.knownSize();
        }

        //endregion
    }

    static final class Filter<@Covariant E> extends AbstractView<E> {

        private final @NotNull View<E> source;

        private final @NotNull Predicate<? super E> predicate;

        public Filter(@NotNull View<E> source, @NotNull Predicate<? super E> predicate) {
            this.source = source;
            this.predicate = predicate;
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            return Iterators.filter(source.iterator(), predicate);
        }
    }

    static final class FlatMapped<@Covariant E, T> extends AbstractView<E> {

        private final @NotNull View<? extends T> source;

        private final @NotNull Function<? super T, ? extends Iterable<? extends E>> mapper;

        public FlatMapped(
                @NotNull View<? extends T> source,
                @NotNull Function<? super T, ? extends Iterable<? extends E>> mapper) {
            this.source = source;
            this.mapper = mapper;
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            return Iterators.concat(source.map(it -> mapper.apply(it).iterator()));
        }
    }

    static final class Zip<E, U> extends AbstractView<Tuple2<E, U>> {
        private final @NotNull View<? extends E> source;
        private final @NotNull Iterable<? extends U> other;

        Zip(@NotNull View<? extends E> source, @NotNull Iterable<? extends U> other) {
            this.source = source;
            this.other = other;
        }

        @Override
        public final @NotNull Iterator<Tuple2<E, U>> iterator() {
            return Iterators.zip(source.iterator(), other.iterator());
        }
    }
}
