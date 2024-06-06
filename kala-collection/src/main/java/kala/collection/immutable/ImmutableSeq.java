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
package kala.collection.immutable;

import kala.annotations.DelegateBy;
import kala.collection.Seq;
import kala.collection.SeqLike;
import kala.collection.base.Traversable;
import kala.function.*;
import kala.tuple.Tuple;
import kala.tuple.Tuple2;
import kala.annotations.Covariant;
import kala.comparator.Comparators;
import kala.collection.factory.CollectionFactory;
import kala.tuple.Tuple3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.Collection;
import java.util.function.*;
import java.util.stream.Stream;

@SuppressWarnings({"unchecked"})
public interface ImmutableSeq<@Covariant E> extends ImmutableCollection<E>, Seq<E>, ImmutableAnySeq<E> {
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
        return ImmutableVector.of(value1, value2, value3);
    }

    static <E> @NotNull ImmutableSeq<E> of(E value1, E value2, E value3, E value4) {
        return ImmutableVector.of(value1, value2, value3, value4);
    }

    static <E> @NotNull ImmutableSeq<E> of(E value1, E value2, E value3, E value4, E value5) {
        return ImmutableVector.of(value1, value2, value3, value4, value5);
    }

    @SafeVarargs
    static <E> @NotNull ImmutableSeq<E> of(E... values) {
        return ImmutableSeq.from(values);
    }

    static <E> @NotNull ImmutableSeq<E> from(E @NotNull [] values) {
        return switch (values.length) {
            case 0 -> ImmutableSeq.empty();
            case 1 -> ImmutableSeq.of(values[0]);
            case 2 -> ImmutableSeq.of(values[0], values[1]);
            case 3 -> ImmutableSeq.of(values[0], values[1], values[2]);
            case 4 -> ImmutableSeq.of(values[0], values[1], values[2], values[3]);
            case 5 -> ImmutableSeq.of(values[0], values[1], values[2], values[3], values[4]);
            default -> ImmutableVector.from(values);
        };
    }

    static <E> @NotNull ImmutableSeq<E> from(java.util.@NotNull Collection<? extends E> values) {
        final int size = values.size();
        if (size == 0) {
            return ImmutableSeq.empty();
        }

        if (size <= ImmutableVectors.WIDTH) {
            final Object[] arr = values.toArray();
            final int length = arr.length;
            //noinspection ConstantConditions
            assert length == size;
            return switch (length) {
                case 1 -> (ImmutableSeq<E>) ImmutableSeq.of(arr[0]);
                case 2 -> (ImmutableSeq<E>) ImmutableSeq.of(arr[0], arr[1]);
                case 3 -> (ImmutableSeq<E>) ImmutableSeq.of(arr[0], arr[1], arr[2]);
                case 4 -> (ImmutableSeq<E>) ImmutableSeq.of(arr[0], arr[1], arr[2], arr[3]);
                case 5 -> (ImmutableSeq<E>) ImmutableSeq.of(arr[0], arr[1], arr[2], arr[3], arr[4]);
                default -> new ImmutableVectors.Vector1<>(arr);
            };
        }
        return ImmutableVector.from(values.iterator());
    }

    static <E> @NotNull ImmutableSeq<E> from(@NotNull Traversable<? extends E> values) {
        if (values instanceof ImmutableSeq<?>) {
            return (ImmutableSeq<E>) values;
        }
        final int knownSize = values.knownSize(); // implicit null check of values
        if (knownSize == 0) {
            return empty();
        }
        if (knownSize > 0 && knownSize <= ImmutableVectors.WIDTH) {
            Object[] arr = new Object[knownSize];
            final int cn = values.copyToArray(arr);
            assert cn == knownSize;
            return switch (knownSize) {
                case 1 -> (ImmutableSeq<E>) ImmutableSeq.of(arr[0]);
                case 2 -> (ImmutableSeq<E>) ImmutableSeq.of(arr[0], arr[1]);
                case 3 -> (ImmutableSeq<E>) ImmutableSeq.of(arr[0], arr[1], arr[2]);
                case 4 -> (ImmutableSeq<E>) ImmutableSeq.of(arr[0], arr[1], arr[2], arr[3]);
                case 5 -> (ImmutableSeq<E>) ImmutableSeq.of(arr[0], arr[1], arr[2], arr[3], arr[4]);
                default -> new ImmutableVectors.Vector1<>(arr);
            };
        }
        return from(values.iterator());
    }

    static <E> @NotNull ImmutableSeq<E> from(@NotNull Iterable<? extends E> values) {
        if (values instanceof Traversable) {
            return from(((Traversable<E>) values));
        }
        if (values instanceof java.util.Collection) {
            return from(((Collection<E>) values));
        }
        return from(values.iterator());
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
        return switch (n) {
            case 1 -> ImmutableSeq.of(supplier.get());
            case 2 -> ImmutableSeq.of(supplier.get(), supplier.get());
            case 3 -> ImmutableSeq.of(supplier.get(), supplier.get(), supplier.get());
            case 4 -> ImmutableSeq.of(supplier.get(), supplier.get(), supplier.get(), supplier.get());
            case 5 -> ImmutableSeq.of(supplier.get(), supplier.get(), supplier.get(), supplier.get(), supplier.get());
            default -> ImmutableVector.fill(n, supplier);
        };
    }

    static <E> @NotNull ImmutableSeq<E> fill(int n, @NotNull IntFunction<? extends E> init) {
        if (n <= 0) {
            return ImmutableSeq.empty();
        }
        return switch (n) {
            case 1 -> ImmutableSeq.of(init.apply(0));
            case 2 -> ImmutableSeq.of(init.apply(0), init.apply(1));
            case 3 -> ImmutableSeq.of(init.apply(0), init.apply(1), init.apply(2));
            case 4 -> ImmutableSeq.of(init.apply(0), init.apply(1), init.apply(2), init.apply(3));
            case 5 -> ImmutableSeq.of(init.apply(0), init.apply(1), init.apply(2), init.apply(3), init.apply(4));
            default -> ImmutableVector.fill(n, init);
        };
    }

    static <E> @NotNull ImmutableSeq<E> generateUntil(@NotNull Supplier<? extends E> supplier, @NotNull Predicate<? super E> predicate) {
        return ImmutableArray.generateUntil(supplier, predicate);
    }

    static <E> @NotNull ImmutableSeq<E> generateUntilNull(@NotNull Supplier<? extends @Nullable E> supplier) {
        return ImmutableArray.generateUntilNull(supplier);
    }

    //endregion

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

    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> takeLast(int n) {
        return AbstractImmutableSeq.takeLast(this, n, iterableFactory());
    }

    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> takeWhile(@NotNull Predicate<? super E> predicate) {
        return AbstractImmutableSeq.takeWhile(this, predicate, iterableFactory());
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
    default @NotNull ImmutableSeq<E> updated(int index, E newValue) {
        return AbstractImmutableSeq.updated(this, index, newValue, iterableFactory());
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
    default @NotNull ImmutableSeq<E> inserted(int index, E value) {
        return AbstractImmutableSeq.inserted(this, index, value, iterableFactory());
    }

    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> removedAt(int index) {
        return AbstractImmutableSeq.removedAt(this, index, iterableFactory());
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
        return filter(iterableFactory(), predicate);
    }

    @Override
    default @NotNull ImmutableSeq<E> filterNot(@NotNull Predicate<? super E> predicate) {
        return filterNot(iterableFactory(), predicate);
    }

    @Override
    default @NotNull ImmutableSeq<@NotNull E> filterNotNull() {
        return this.filter(Predicates.isNotNull());
    }

    default <U> @NotNull ImmutableSeq<@NotNull U> filterIsInstance(@NotNull Class<? extends U> clazz) {
        return ((ImmutableSeq<U>) this.filter(Predicates.instanceOf(clazz)));
    }

    @Override
    default <U> @NotNull ImmutableSeq<U> map(@NotNull Function<? super E, ? extends U> mapper) {
        return map(this.<U>iterableFactory(), mapper);
    }

    @Contract(pure = true)
    default <U> @NotNull ImmutableSeq<U> mapIndexed(@NotNull IndexedFunction<? super E, ? extends U> mapper) {
        return AbstractImmutableSeq.mapIndexed(this, mapper, this.<U>iterableFactory());
    }

    @Override
    default <U> @NotNull ImmutableSeq<@NotNull U> mapNotNull(@NotNull Function<? super E, ? extends @Nullable U> mapper) {
        return mapNotNull(this.<U>iterableFactory(), mapper);
    }

    default <U> @NotNull ImmutableSeq<@NotNull U> mapIndexedNotNull(
            @NotNull IndexedFunction<? super E, ? extends @Nullable U> mapper) {
        return AbstractImmutableSeq.mapIndexedNotNull(this, mapper, this.<U>iterableFactory());
    }

    @Override
    default @NotNull <U> ImmutableSeq<U> mapMulti(@NotNull BiConsumer<? super E, ? super Consumer<? super U>> mapper) {
        return AbstractImmutableCollection.mapMulti(this, mapper, iterableFactory());
    }

    @Override
    default @NotNull <U> ImmutableSeq<U> mapIndexedMulti(@NotNull IndexedBiConsumer<? super E, ? super Consumer<? super U>> mapper) {
        return AbstractImmutableSeq.mapIndexedMulti(this, mapper, iterableFactory());
    }

    @Override
    default <U> @NotNull ImmutableSeq<U> flatMap(@NotNull Function<? super E, ? extends Iterable<? extends U>> mapper) {
        return flatMap(iterableFactory(), mapper);
    }

    @Override
    @DelegateBy("zip(Iterable<U>, BiFunction<E, U, R>)")
    default <U> @NotNull ImmutableSeq<@NotNull Tuple2<E, U>> zip(@NotNull Iterable<? extends U> other) {
        return zip(other, Tuple::of);
    }

    @Contract(pure = true)
    default <U, R> @NotNull ImmutableSeq<R> zip(@NotNull Iterable<? extends U> other, @NotNull BiFunction<? super E, ? super U, ? extends R> mapper) {
        return AbstractImmutableCollection.zip(this, other, mapper, this.<R>iterableFactory());
    }

    @Override
    default <U, V> @NotNull ImmutableSeq<@NotNull Tuple3<E, U, V>> zip3(@NotNull Iterable<? extends U> other1, @NotNull Iterable<? extends V> other2) {
        return AbstractImmutableCollection.zip3(this, other1, other2, this.<Tuple3<E, U, V>>iterableFactory());
    }

    @Override
    default <R> Tuple2<? extends ImmutableSeq<E>, ? extends ImmutableSeq<E>> partition(@NotNull Predicate<? super E> predicate) {
        return partition(iterableFactory(), predicate);
    }

    @Override
    default @NotNull ImmutableSeq<E> distinct() {
        return distinct(this.iterableFactory());
    }

    @Override
    default @NotNull ImmutableSeq<E> toImmutableSeq() {
        return this;
    }

}
