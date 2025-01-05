/*
 * Copyright 2025 Glavo
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

import kala.annotations.Covariant;
import kala.annotations.DelegateBy;
import kala.collection.Collection;
import kala.collection.factory.CollectionFactory;
import kala.function.Predicates;
import kala.tuple.Tuple;
import kala.tuple.Tuple2;
import kala.tuple.Tuple3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.function.*;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public interface ImmutableCollection<@Covariant E> extends Collection<E>, ImmutableAnyCollection<E> {

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

    static <E> @NotNull ImmutableCollection<E> fill(int n, @NotNull IntFunction<? extends E> init) {
        return ImmutableSeq.fill(n, init);
    }

    //endregion

    @Override
    default @NotNull String className() {
        return "ImmutableCollection";
    }

    @Override
    @Contract(pure = true)
    default <U> @NotNull CollectionFactory<U, ?, ? extends ImmutableCollection<U>> iterableFactory() {
        return factory();
    }

    @Contract(pure = true)
    default @NotNull ImmutableCollection<E> filter(@NotNull Predicate<? super E> predicate) {
        return filter(iterableFactory(), predicate);
    }

    @Contract(pure = true)
    default @NotNull ImmutableCollection<E> filterNot(@NotNull Predicate<? super E> predicate) {
        return filterNot(iterableFactory(), predicate);
    }

    @Contract(pure = true)
    default @NotNull ImmutableCollection<@NotNull E> filterNotNull() {
        return this.filter(Predicates.isNotNull());
    }

    @Override
    default <U> @NotNull ImmutableCollection<U> filterIsInstance(@NotNull Class<? extends U> clazz) {
        return (ImmutableCollection<U>) filter(clazz::isInstance);
    }

    @Contract(pure = true)
    default <U> @NotNull ImmutableCollection<U> map(@NotNull Function<? super E, ? extends U> mapper) {
        return map(this.<U>iterableFactory(), mapper);
    }

    @Contract(pure = true)
    default <U> @NotNull ImmutableCollection<@NotNull U> mapNotNull(@NotNull Function<? super E, ? extends @Nullable U> mapper) {
        return mapNotNull(this.<U>iterableFactory(), mapper);
    }

    @Override
    default @NotNull <U> ImmutableCollection<U> mapMulti(@NotNull BiConsumer<? super E, ? super Consumer<? super U>> mapper) {
        return AbstractImmutableCollection.mapMulti(this, mapper, iterableFactory());
    }

    @Contract(pure = true)
    default <U> @NotNull ImmutableCollection<U> flatMap(@NotNull Function<? super E, ? extends Iterable<? extends U>> mapper) {
        return flatMap(iterableFactory(), mapper);
    }

    @Override
    @DelegateBy("zip(Iterable<U>, BiFunction<E, U, R>)")
    default <U> @NotNull ImmutableCollection<@NotNull Tuple2<E, U>> zip(@NotNull Iterable<? extends U> other) {
        return zip(other, Tuple::of);
    }

    @Contract(pure = true)
    default <U, R> @NotNull ImmutableCollection<R> zip(@NotNull Iterable<? extends U> other, @NotNull BiFunction<? super E, ? super U, ? extends R> mapper) {
        return AbstractImmutableCollection.zip(this, other, mapper, this.<R>iterableFactory());
    }

    @Override
    default <U, V> @NotNull ImmutableCollection<@NotNull Tuple3<E, U, V>> zip3(@NotNull Iterable<? extends U> other1, @NotNull Iterable<? extends V> other2) {
        return AbstractImmutableCollection.zip3(this, other1, other2, this.<Tuple3<E, U, V>>iterableFactory());
    }

    @Override
    default @NotNull ImmutableCollection<E> distinct() {
        return distinct(this.iterableFactory());
    }
}
