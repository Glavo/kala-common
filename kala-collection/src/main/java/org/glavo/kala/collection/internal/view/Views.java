package org.glavo.kala.collection.internal.view;

import org.glavo.kala.collection.AbstractView;
import org.glavo.kala.collection.Collection;
import org.glavo.kala.collection.Seq;
import org.glavo.kala.collection.View;
import org.glavo.kala.tuple.Tuple2;
import org.glavo.kala.collection.immutable.ImmutableArray;
import org.glavo.kala.collection.immutable.ImmutableList;
import org.glavo.kala.collection.immutable.ImmutableSeq;
import org.glavo.kala.collection.immutable.ImmutableVector;
import org.glavo.kala.control.Option;
import org.glavo.kala.annotations.Covariant;
import org.glavo.kala.collection.factory.CollectionFactory;
import org.glavo.kala.collection.base.Iterators;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public final class Views {
    public static class Empty<E> extends AbstractView<E> {
        public static final Empty<?> INSTANCE = new Empty<Object>();

        @Override
        public final @NotNull Iterator<E> iterator() {
            return Iterators.empty();
        }

        //region Size Info

        @Override
        public final boolean isEmpty() {
            return true;
        }

        @Override
        public final int size() {
            return 0;
        }

        @Override
        public final int knownSize() {
            return 0;
        }

        //endregion

        @Override
        public @NotNull View<E> filter(@NotNull Predicate<? super E> predicate) {
            return this;
        }

        @Override
        public @NotNull View<E> filterNot(@NotNull Predicate<? super E> predicate) {
            return this;
        }

        @Override
        public @NotNull <U> View<U> map(@NotNull Function<? super E, ? extends U> mapper) {
            return ((View<U>) this);
        }

        @Override
        public @NotNull <U> View<U> flatMap(@NotNull Function<? super E, ? extends Iterable<? extends U>> mapper) {
            return ((View<U>) this);
        }

        @Override
        public final Object @NotNull [] toArray() {
            return new Object[0];
        }

        @Override
        public final @NotNull ImmutableVector<E> toImmutableVector() {
            return ImmutableVector.empty();
        }

        @Override
        public String toString() {
            return className() + "[]";
        }
    }

    public static <E> View<E> empty() {
        return (View<E>) Empty.INSTANCE;
    }

    public static class Single<E> extends AbstractView<E> {
        protected final E value;

        public Single(E value) {
            this.value = value;
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            return Iterators.of(value);
        }

        @Override
        public @NotNull Stream<E> stream() {
            return Stream.of(value);
        }

        //region Size Info

        @Override
        public final boolean isEmpty() {
            return false;
        }

        @Override
        public final int size() {
            return 1;
        }

        @Override
        public final int knownSize() {
            return 1;
        }

        //endregion
        
        @Override
        public @NotNull View<E> filter(@NotNull Predicate<? super E> predicate) {
            return predicate.test(value) ? this : empty();
        }

        @Override
        public @NotNull View<E> filterNot(@NotNull Predicate<? super E> predicate) {
            return predicate.test(value) ? empty() : this;
        }

        @Override
        public @NotNull View<@NotNull E> filterNotNull() {
            return value != null ? this : empty();
        }

        @Override
        public @NotNull <U> View<U> map(@NotNull Function<? super E, ? extends U> mapper) {
            return new Single<>(mapper.apply(value));
        }
    }

    public static class Of<@Covariant E, C extends Collection<E>> extends AbstractView<E> {
        protected final @NotNull C source;

        public Of(@NotNull C source) {
            this.source = source;
        }

        //region Collection Operations

        @Override
        public final @NotNull Iterator<E> iterator() {
            return source.iterator();
        }

        @Override
        public final @NotNull Spliterator<E> spliterator() {
            return source.spliterator();
        }

        @Override
        public final @NotNull Stream<E> stream() {
            return source.stream();
        }

        @Override
        public final @NotNull Stream<E> parallelStream() {
            return source.parallelStream();
        }

        //endregion

        //region Size Info

        @Override
        public boolean isEmpty() {
            return source.isEmpty();
        }

        @Override
        public int size() {
            return source.size();
        }

        @Override
        public int knownSize() {
            return source.knownSize();
        }

        //endregion

        @Override
        public final @NotNull Option<E> find(@NotNull Predicate<? super E> predicate) {
            return source.find(predicate);
        }

        //region Element Conditions

        @Override
        public final boolean contains(Object value) {
            return source.contains(value);
        }

        @Override
        public final boolean containsAll(Object @NotNull [] values) {
            return source.containsAll(values);
        }

        @Override
        public final boolean containsAll(@NotNull Iterable<?> values) {
            return source.containsAll(values);
        }

        @Override
        public final boolean sameElements(@NotNull Iterable<?> other) {
            return source.sameElements(other);
        }

        @Override
        public final boolean sameElements(@NotNull Iterable<?> other, boolean identity) {
            return source.sameElements(other, identity);
        }

        @Override
        public final boolean anyMatch(@NotNull Predicate<? super E> predicate) {
            return source.anyMatch(predicate);
        }

        @Override
        public final boolean allMatch(@NotNull Predicate<? super E> predicate) {
            return source.allMatch(predicate);
        }

        @Override
        public final boolean noneMatch(@NotNull Predicate<? super E> predicate) {
            return source.noneMatch(predicate);
        }

        //endregion

        //region Aggregate

        @Override
        public final int count(@NotNull Predicate<? super E> predicate) {
            return source.count(predicate);
        }

        @Override
        public final E max() {
            return source.max();
        }

        @Override
        public final E max(@NotNull Comparator<? super E> comparator) {
            return source.max(comparator);
        }

        @Override
        public @Nullable E maxOrNull() {
            return source.maxOrNull();
        }

        @Override
        public @Nullable E maxOrNull(@NotNull Comparator<? super E> comparator) {
            return source.maxOrNull(comparator);
        }

        @Override
        public final @NotNull Option<E> maxOption() {
            return source.maxOption();
        }

        @Override
        public final @NotNull Option<E> maxOption(@NotNull Comparator<? super E> comparator) {
            return source.maxOption(comparator);
        }

        @Override
        public final E min() {
            return source.min();
        }

        @Override
        public final E min(@NotNull Comparator<? super E> comparator) {
            return source.min(comparator);
        }

        @Override
        public @Nullable E minOrNull() {
            return source.minOrNull();
        }

        @Override
        public @Nullable E minOrNull(@NotNull Comparator<? super E> comparator) {
            return source.minOrNull(comparator);
        }

        @Override
        public final @NotNull Option<E> minOption() {
            return source.minOption();
        }

        @Override
        public final @NotNull Option<E> minOption(@NotNull Comparator<? super E> comparator) {
            return source.minOption(comparator);
        }

        @Override
        public final E fold(E zero, @NotNull BiFunction<? super E, ? super E, ? extends E> op) {
            return source.fold(zero, op);
        }

        @Override
        public final <U> U foldLeft(U zero, @NotNull BiFunction<? super U, ? super E, ? extends U> op) {
            return source.foldLeft(zero, op);
        }

        @Override
        public final <U> U foldRight(U zero, @NotNull BiFunction<? super E, ? super U, ? extends U> op) {
            return source.foldRight(zero, op);
        }

        @Override
        public final E reduce(@NotNull BiFunction<? super E, ? super E, ? extends E> op) throws NoSuchElementException {
            return source.reduce(op);
        }

        @Override
        public final @NotNull Option<E> reduceOption(@NotNull BiFunction<? super E, ? super E, ? extends E> op) {
            return source.reduceOption(op);
        }

        @Override
        public final E reduceLeft(@NotNull BiFunction<? super E, ? super E, ? extends E> op) throws NoSuchElementException {
            return source.reduceLeft(op);
        }

        @Override
        public final @NotNull Option<E> reduceLeftOption(@NotNull BiFunction<? super E, ? super E, ? extends E> op) {
            return source.reduceLeftOption(op);
        }

        @Override
        public final E reduceRight(@NotNull BiFunction<? super E, ? super E, ? extends E> op) throws NoSuchElementException {
            return source.reduceRight(op);
        }

        @Override
        public final @NotNull Option<E> reduceRightOption(@NotNull BiFunction<? super E, ? super E, ? extends E> op) {
            return source.reduceRightOption(op);
        }

        //endregion

        //region Conversion Operations

        @Override
        public final <R, Builder> R collect(@NotNull Collector<? super E, Builder, ? extends R> collector) {
            return source.collect(collector);
        }

        @Override
        public final <R, Builder> R collect(@NotNull CollectionFactory<? super E, Builder, ? extends R> factory) {
            return source.collect(factory);
        }

        @Override
        public final Object @NotNull [] toArray() {
            return source.toArray();
        }

        @Override
        public final <U> U @NotNull [] toArray(@NotNull IntFunction<U[]> generator) {
            return source.toArray(generator);
        }

        @Override
        public final <U> U @NotNull [] toArray(@NotNull Class<U> type) {
            return source.toArray(type);
        }

        @Override
        public final @NotNull Seq<E> toSeq() {
            return source.toSeq();
        }

        @Override
        public final @NotNull ImmutableSeq<E> toImmutableSeq() {
            return source.toImmutableSeq();
        }

        @Override
        public final @NotNull ImmutableArray<E> toImmutableArray() {
            return source.toImmutableArray();
        }

        @Override
        public final @NotNull ImmutableList<E> toImmutableList() {
            return source.toImmutableList();
        }

        @Override
        public final @NotNull ImmutableVector<E> toImmutableVector() {
            return source.toImmutableVector();
        }

        //endregion

        //region Traverse Operations

        @Override
        public final void forEach(@NotNull Consumer<? super E> action) {
            source.forEach(action);
        }

        //endregion

        //region String Representation

        @Override
        public final <A extends Appendable> @NotNull A joinTo(@NotNull A buffer) {
            return source.joinTo(buffer);
        }

        @Override
        public final <A extends Appendable> @NotNull A joinTo(@NotNull A buffer, @NotNull CharSequence separator) {
            return source.joinTo(buffer, separator);
        }

        @Override
        public final <A extends Appendable> @NotNull A joinTo(@NotNull A buffer, @NotNull CharSequence separator, @NotNull CharSequence prefix, @NotNull CharSequence postfix) {
            return source.joinTo(buffer, separator, prefix, postfix);
        }

        @Override
        public final @NotNull String joinToString() {
            return source.joinToString();
        }

        @Override
        public final @NotNull String joinToString(@NotNull CharSequence separator) {
            return source.joinToString(separator);
        }

        @Override
        public final @NotNull String joinToString(@NotNull CharSequence separator, @NotNull CharSequence prefix, @NotNull CharSequence postfix) {
            return source.joinToString(separator, prefix, postfix);
        }


        @Override
        public final String toString() {
            return joinToString(", ", className() + "[", "]");
        }

        //endregion

    }

    public static final class Mapped<@Covariant E, T> extends AbstractView<E> {

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

    public static final class Filter<@Covariant E> extends AbstractView<E> {

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

    public static final class FlatMapped<@Covariant E, T> extends AbstractView<E> {

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

    public static final class Zip<E, U> extends AbstractView<Tuple2<E, U>> {
        private final @NotNull View<? extends E> source;
        private final @NotNull Iterable<? extends U> other;

        public Zip(@NotNull View<? extends E> source, @NotNull Iterable<? extends U> other) {
            this.source = source;
            this.other = other;
        }

        @Override
        public final @NotNull Iterator<Tuple2<E, U>> iterator() {
            return Iterators.zip(source.iterator(), other.iterator());
        }
    }
}
