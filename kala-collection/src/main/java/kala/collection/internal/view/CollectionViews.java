/*
 * Copyright 2024 Glavo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kala.collection.internal.view;

import kala.collection.*;
import kala.collection.Map;
import kala.collection.base.Iterators;
import kala.collection.immutable.*;
import kala.control.Option;
import kala.annotations.Covariant;
import kala.collection.factory.CollectionFactory;
import kala.tuple.Tuple3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public final class CollectionViews {
    private CollectionViews() {
    }

    public static class Empty<E> extends AbstractCollectionView<E> {
        public static final Empty<?> INSTANCE = new Empty<>();

        @Override
        public final @NotNull Iterator<E> iterator() {
            return Iterators.empty();
        }

        @Override
        public @NotNull Spliterator<E> spliterator() {
            return Spliterators.emptySpliterator();
        }

        @Override
        public @NotNull Stream<E> stream() {
            return Stream.empty();
        }

        @Override
        public @NotNull Stream<E> parallelStream() {
            return Stream.<E>empty().parallel();
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
        public @NotNull CollectionView<E> filter(@NotNull Predicate<? super E> predicate) {
            return this;
        }

        @Override
        public @NotNull CollectionView<E> filterNot(@NotNull Predicate<? super E> predicate) {
            return this;
        }

        @Override
        public @NotNull CollectionView<@NotNull E> filterNotNull() {
            return this;
        }

        @Override
        public @NotNull <U> CollectionView<U> map(@NotNull Function<? super E, ? extends U> mapper) {
            return ((CollectionView<U>) this);
        }

        @Override
        public @NotNull <U> CollectionView<U> flatMap(@NotNull Function<? super E, ? extends Iterable<? extends U>> mapper) {
            return ((CollectionView<U>) this);
        }

        @Override
        public final Object @NotNull [] toArray() {
            return new Object[0];
        }

        @Override
        public final String toString() {
            return className() + "[]";
        }
    }

    public static class Single<E> extends AbstractCollectionView<E> {
        protected final E value;

        public Single(E value) {
            this.value = value;
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            return Iterators.of(value);
        }

        @Override
        public @NotNull Spliterator<E> spliterator() {
            return Spliterators.spliterator(Iterators.of(value), 1, 0);
        }

        @Override
        public @NotNull Stream<E> stream() {
            return Stream.of(value);
        }

        @Override
        public @NotNull Stream<E> parallelStream() {
            return Stream.of(value).parallel();
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
        public void forEach(@NotNull Consumer<? super E> action) {
            action.accept(value);
        }
    }

    public static class Of<@Covariant E, C extends CollectionLike<E>> extends AbstractCollectionView<E> {
        protected final @NotNull C source;

        public Of(@NotNull C source) {
            this.source = source;
        }

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
        public final E max(@NotNull Comparator<? super E> comparator) {
            return source.max(comparator);
        }

        @Override
        public final E min(@NotNull Comparator<? super E> comparator) {
            return source.min(comparator);
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
        public final <U> U @NotNull [] toArray(@NotNull Class<U> type) {
            return source.toArray(type);
        }

        @Override
        public final <U> U @NotNull [] toArray(@NotNull IntFunction<U[]> generator) {
            return source.toArray(generator);
        }

        @Override
        public <U> U @NotNull [] toArray(U @NotNull [] array) {
            return source.toArray(array);
        }

        @Override
        public final @NotNull ImmutableSeq<E> toSeq() {
            return source.toSeq();
        }

        @Override
        public final @NotNull ImmutableArray<E> toArraySeq() {
            return source.toArraySeq();
        }

        @Override
        public final @NotNull <K, V> Map<K, V> associate(
                @NotNull Function<? super E, ? extends java.util.Map.Entry<? extends K, ? extends V>> transform) {
            return source.associate(transform);
        }

        @Override
        public final @NotNull <K, V> Map<K, V> associateBy(
                @NotNull Function<? super E, ? extends K> keySelector, @NotNull Function<? super E, ? extends V> valueTransform) {
            return source.associateBy(keySelector, valueTransform);
        }

        @Override
        public final @NotNull <K> Map<K, E> associateBy(@NotNull Function<? super E, ? extends K> keySelector) {
            return source.associateBy(keySelector);
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
        public <A extends Appendable> @NotNull A joinTo(@NotNull A buffer, @NotNull Function<? super E, ? extends CharSequence> transform) {
            return source.joinTo(buffer, transform);
        }

        @Override
        public <A extends Appendable> @NotNull A joinTo(@NotNull A buffer, CharSequence separator, @NotNull Function<? super E, ? extends CharSequence> transform) {
            return source.joinTo(buffer, separator, transform);
        }

        @Override
        @Contract(value = "_, _, _, _, _ -> param1", mutates = "param1")
        public <A extends Appendable> @NotNull A joinTo(@NotNull A buffer, CharSequence separator, CharSequence prefix, CharSequence postfix, @NotNull Function<? super E, ? extends CharSequence> transform) {
            return source.joinTo(buffer, separator, prefix, postfix, transform);
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
        public @NotNull String joinToString(@NotNull Function<? super E, ? extends CharSequence> transform) {
            return source.joinToString(transform);
        }

        @Override
        public @NotNull String joinToString(CharSequence separator, @NotNull Function<? super E, ? extends CharSequence> transform) {
            return source.joinToString(separator, transform);
        }

        @Override
        public @NotNull String joinToString(CharSequence separator, CharSequence prefix, CharSequence postfix, @NotNull Function<? super E, ? extends CharSequence> transform) {
            return source.joinToString(separator, prefix, postfix, transform);
        }

        @Override
        public final String toString() {
            return joinToString(", ", className() + "[", "]");
        }

        //endregion

    }

    public static class OfJava<@Covariant E, C extends java.util.Collection<E>> extends AbstractCollectionView<E> {
        protected final @NotNull C source;

        public OfJava(@NotNull C source) {
            this.source = source;
        }

        @Override
        public @NotNull Iterator<E> iterator() {
            return source.iterator();
        }

        @Override
        public Spliterator<E> spliterator() {
            return source.spliterator();
        }

        @Override
        public @NotNull Stream<E> stream() {
            return source.stream();
        }

        @Override
        public @NotNull Stream<E> parallelStream() {
            return source.parallelStream();
        }
    }

    public static final class Mapped<@Covariant E, T> extends AbstractCollectionView<E> {

        private final @NotNull CollectionView<T> source;

        private final @NotNull Function<? super T, ? extends E> mapper;

        public Mapped(@NotNull CollectionView<T> source, @NotNull Function<? super T, ? extends E> mapper) {
            this.source = source;
            this.mapper = mapper;
        }

        @Override
        public @NotNull Iterator<E> iterator() {
            return Iterators.map(source.iterator(), mapper);
        }

        @Override
        public @NotNull Spliterator<E> spliterator() {
            return new MappedSpliterator<>(source.spliterator(), mapper);
        }

        @Override
        public @NotNull Stream<E> stream() {
            return source.stream().map(mapper);
        }

        @Override
        public @NotNull Stream<E> parallelStream() {
            return source.parallelStream().map(mapper);
        }

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
    }

    public static class MapNotNull<E, T> extends AbstractCollectionView<E> {
        private final @NotNull CollectionView<T> source;
        private final @NotNull Function<? super T, ? extends E> mapper;

        public MapNotNull(@NotNull CollectionView<T> source, @NotNull Function<? super T, ? extends E> mapper) {
            this.source = source;
            this.mapper = mapper;
        }

        @Override
        public @NotNull Iterator<E> iterator() {
            return Iterators.mapNotNull(source.iterator(), mapper);
        }
    }

    public static class MapMulti<E, T> extends AbstractCollectionView<E> {
        private final @NotNull CollectionView<T> source;
        private final @NotNull BiConsumer<? super T, ? super Consumer<? super E>> mapper;

        public MapMulti(@NotNull CollectionView<T> source, @NotNull BiConsumer<? super T, ? super Consumer<? super E>> mapper) {
            this.source = source;
            this.mapper = mapper;
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            return Iterators.mapMulti(source.iterator(), mapper);
        }
    }

    public static final class Filter<@Covariant E> extends AbstractCollectionView<E> {
        private final @NotNull CollectionView<E> source;
        private final @NotNull Predicate<? super E> predicate;

        public Filter(@NotNull CollectionView<E> source, @NotNull Predicate<? super E> predicate) {
            this.source = source;
            this.predicate = predicate;
        }

        @Override
        public @NotNull Iterator<E> iterator() {
            return Iterators.filter(source.iterator(), predicate);
        }

        @Override
        public @NotNull Stream<E> stream() {
            return source.stream().filter(predicate);
        }

        @Override
        public @NotNull Stream<E> parallelStream() {
            return source.stream().filter(predicate).parallel();
        }
    }

    public static final class FilterNot<@Covariant E> extends AbstractCollectionView<E> {
        private final @NotNull CollectionView<E> source;

        private final @NotNull Predicate<? super E> predicate;

        public FilterNot(@NotNull CollectionView<E> source, @NotNull Predicate<? super E> predicate) {
            this.source = source;
            this.predicate = predicate;
        }

        @Override
        public @NotNull Iterator<E> iterator() {
            return Iterators.filterNot(source.iterator(), predicate);
        }

        @Override
        public @NotNull Stream<E> stream() {
            return source.stream().filter(predicate.negate());
        }

        @Override
        public @NotNull Stream<E> parallelStream() {
            return source.stream().filter(predicate.negate()).parallel();
        }
    }

    public static final class FilterNotNull<@Covariant E> extends AbstractCollectionView<E> {
        private final @NotNull CollectionView<E> source;

        public FilterNotNull(@NotNull CollectionView<E> source) {
            this.source = source;
        }

        @Override
        public @NotNull Iterator<E> iterator() {
            return Iterators.filterNotNull(source.iterator());
        }
    }

    public static final class FlatMapped<@Covariant E, T> extends AbstractCollectionView<E> {
        private final @NotNull CollectionView<? extends T> source;
        private final @NotNull Function<? super T, ? extends Iterable<? extends E>> mapper;

        public FlatMapped(
                @NotNull CollectionView<? extends T> source,
                @NotNull Function<? super T, ? extends Iterable<? extends E>> mapper) {
            this.source = source;
            this.mapper = mapper;
        }

        @Override
        public @NotNull Iterator<E> iterator() {
            return Iterators.concat(source.map(it -> mapper.apply(it).iterator()));
        }
    }

    public static final class Zip<E, U, R> extends AbstractCollectionView<R> {
        private final @NotNull CollectionView<? extends E> source;
        private final @NotNull Iterable<? extends U> other;
        private final @NotNull BiFunction<? super E, ? super U, ? extends R> mapper;

        public Zip(@NotNull CollectionView<? extends E> source, @NotNull Iterable<? extends U> other, @NotNull BiFunction<? super E, ? super U, ? extends R> mapper) {
            this.source = source;
            this.other = other;
            this.mapper = mapper;
        }

        @Override
        public @NotNull Iterator<R> iterator() {
            return Iterators.zip(source.iterator(), other.iterator(), mapper);
        }
    }

    public static final class Zip3<E, U, V> extends AbstractCollectionView<Tuple3<E, U, V>> {
        private final @NotNull CollectionView<? extends E> source;
        private final @NotNull Iterable<? extends U> other1;
        private final @NotNull Iterable<? extends V> other2;

        public Zip3(@NotNull CollectionView<? extends E> source, @NotNull Iterable<? extends U> other1, @NotNull Iterable<? extends V> other2) {
            this.source = source;
            this.other1 = other1;
            this.other2 = other2;
        }

        @Override
        public @NotNull Iterator<Tuple3<E, U, V>> iterator() {
            return Iterators.zip3(source.iterator(), other1.iterator(), other2.iterator());
        }
    }

    private static final class MappedSpliterator<E, T> implements Spliterator<E> {
        private final @NotNull Spliterator<? extends T> source;
        private final @NotNull Function<? super T, ? extends E> mapper;

        MappedSpliterator(@NotNull Spliterator<? extends T> source, @NotNull Function<? super T, ? extends E> mapper) {
            this.source = source;
            this.mapper = mapper;
        }

        @Override
        public boolean tryAdvance(Consumer<? super E> action) {
            Objects.requireNonNull(action);
            return source.tryAdvance((Consumer<T> & Serializable) (t) -> action.accept(mapper.apply(t)));
        }

        @Override
        public Spliterator<E> trySplit() {
            final Spliterator<? extends T> ss = source.trySplit();
            return ss != null ? new MappedSpliterator<>(ss, mapper) : null;
        }

        @Override
        public long estimateSize() {
            return source.estimateSize();
        }

        @Override
        public int characteristics() {
            return source.characteristics();
        }
    }
}
