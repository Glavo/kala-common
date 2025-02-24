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

import kala.annotations.Covariant;
import kala.annotations.DelegateBy;
import kala.collection.factory.CollectionBuilder;
import kala.collection.factory.CollectionFactory;
import kala.collection.Set;
import kala.function.CheckedConsumer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public interface ImmutableSet<@Covariant E> extends ImmutableCollection<E>, Set<E>, ImmutableAnySet<E> {

    @Contract(value = "_ -> param1", pure = true)
    static <E> ImmutableSet<E> narrow(ImmutableSet<? extends E> set) {
        return (ImmutableSet<E>) set;
    }

    //region Static Factories

    static <E> @NotNull CollectionFactory<E, ?, ImmutableSet<E>> factory() {
        return CollectionFactory.narrow(ImmutableHashSet.factory());
    }

    @Contract("-> new")
    static <E> @NotNull CollectionBuilder<E, ImmutableSet<E>> newBuilder() {
        return ImmutableSet.<E>factory().newCollectionBuilder();
    }

    static <E> @NotNull ImmutableSet<E> empty() {
        return ImmutableHashSet.empty();
    }

    static <E> @NotNull ImmutableSet<E> of() {
        return ImmutableHashSet.of();
    }

    static <E> @NotNull ImmutableSet<E> of(E value1) {
        return ImmutableHashSet.of(value1);
    }

    static <E> @NotNull ImmutableSet<E> of(E value1, E value2) {
        return ImmutableHashSet.of(value1, value2);
    }

    static <E> @NotNull ImmutableSet<E> of(E value1, E value2, E value3) {
        return ImmutableHashSet.of(value1, value2, value3);
    }

    static <E> @NotNull ImmutableSet<E> of(E value1, E value2, E value3, E value4) {
        return ImmutableHashSet.of(value1, value2, value3, value4);
    }

    static <E> @NotNull ImmutableSet<E> of(E value1, E value2, E value3, E value4, E value5) {
        return ImmutableHashSet.of(value1, value2, value3, value4, value5);
    }

    static <E> @NotNull ImmutableSet<E> of(E... values) {
        return ImmutableHashSet.of(values);
    }

    static <E> @NotNull ImmutableSet<E> from(E @NotNull [] values) {
        return ImmutableHashSet.from(values);
    }

    static <E> @NotNull ImmutableSet<E> from(@NotNull Iterable<? extends E> values) {
        return ImmutableHashSet.from(values);
    }

    static <E> @NotNull ImmutableSet<E> from(@NotNull Iterator<? extends E> it) {
        return ImmutableHashSet.from(it);
    }

    static <E> @NotNull ImmutableSet<E> from(@NotNull Stream<? extends E> stream) {
        return ImmutableHashSet.from(stream);
    }

    //endregion

    @Override
    default @NotNull String className() {
        return "ImmutableSet";
    }

    @Override
    default <U> @NotNull CollectionFactory<U, ?, ? extends ImmutableSet<U>> iterableFactory() {
        return factory();
    }

    @Override
    @ApiStatus.NonExtendable
    default @NotNull ImmutableCollection<E> toCollection() {
        return this;
    }

    @Override
    @ApiStatus.NonExtendable
    default @NotNull ImmutableSet<E> toSet() {
        return this;
    }

    @Override
    @ApiStatus.NonExtendable
    @DelegateBy("forEach(Consumer<E, Ex>)")
    @Contract(value = "_ -> this", pure = true)
    default @NotNull ImmutableSet<E> onEach(@NotNull Consumer<? super E> action) {
        forEach(action);
        return this;
    }

    @Override
    @ApiStatus.NonExtendable
    @DelegateBy("onEach(Consumer<E, Ex>)")
    @Contract(value = "_ -> this", pure = true)
    default <Ex extends Throwable> @NotNull ImmutableSet<E> onEachChecked(@NotNull CheckedConsumer<? super E, ? extends Ex> action) throws Ex {
        return onEach(action);
    }

    @Override
    @ApiStatus.NonExtendable
    @DelegateBy("onEach(Consumer<T, Ex>)")
    @Contract(value = "_ -> this", pure = true)
    default @NotNull ImmutableSet<E> onEachUnchecked(@NotNull CheckedConsumer<? super E, ?> action) {
        return onEach(action);
    }
}
