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
import kala.collection.ArraySeq;
import kala.collection.SortedSet;
import kala.annotations.Covariant;
import kala.collection.factory.CollectionFactory;
import kala.collection.Set;
import kala.function.Predicates;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public interface ImmutableSet<@Covariant E> extends ImmutableCollection<E>, Set<E>, ImmutableAnySet<E> {

    @Contract(value = "_ -> param1", pure = true)
    static <E> ImmutableSet<E> narrow(ImmutableSet<? extends E> set) {
        return (ImmutableSet<E>) set;
    }

    //region Static Factories

    static <E> CollectionFactory<E, ?, ? extends ImmutableSet<E>> factory() {
        return ImmutableHashSet.factory();
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
    default @NotNull ImmutableSet<E> added(E value) {
        if (contains(value))
            return this;

        CollectionFactory<E, ?, ? extends ImmutableSet<E>> factory;
        if (this instanceof SortedSet<?>) {
            factory = (CollectionFactory<E, ?, ? extends ImmutableSet<E>>)
                    ((SortedSet<?>) this).iterableFactory(((SortedSet<?>) this).comparator());
        } else {
            factory = iterableFactory();
        }
        return AbstractImmutableSet.added(this, value, factory);
    }

    @Override
    @SuppressWarnings("unchecked")
    default @NotNull ImmutableSet<E> addedAll(@NotNull Iterable<? extends E> values) {
        CollectionFactory<E, ?, ? extends ImmutableSet<E>> factory;
        if (this instanceof SortedSet<?> sortedSet) {
            factory = (CollectionFactory<E, ?, ? extends ImmutableSet<E>>) sortedSet.iterableFactory(sortedSet.comparator());
        } else {
            factory = iterableFactory();
        }
        return AbstractImmutableSet.addedAll(this, values, factory);
    }

    @Override
    @DelegateBy("addedAll(Iterable<E>)")
    default @NotNull ImmutableSet<E> addedAll(E @NotNull [] values) {
        Objects.requireNonNull(values);
        return addedAll(ArraySeq.wrap(values));
    }

    default @NotNull ImmutableSet<E> removed(E value) {
        if (!contains(value))
            return this;

        CollectionFactory<E, ?, ? extends ImmutableSet<E>> factory;

        if (this instanceof SortedSet<?>) {
            factory = (CollectionFactory<E, ?, ? extends ImmutableSet<E>>)
                    ((SortedSet<?>) this).iterableFactory(((SortedSet<?>) this).comparator());
        } else {
            factory = iterableFactory();
        }

        if (sizeIs(1))
            return factory.empty();

        return AbstractImmutableSet.removed(this, value, factory);
    }

    default @NotNull ImmutableSet<E> removedAll(@NotNull Iterable<? extends E> values) {
        CollectionFactory<E, ?, ? extends ImmutableSet<E>> factory;
        if (this instanceof SortedSet<?>) {
            factory = (CollectionFactory<E, ?, ? extends ImmutableSet<E>>)
                    ((SortedSet<?>) this).iterableFactory(((SortedSet<?>) this).comparator());
        } else {
            factory = iterableFactory();
        }
        return AbstractImmutableSet.removedAll(this, values, factory);
    }

    default @NotNull ImmutableSet<E> removedAll(E @NotNull [] values) {
        return removedAll(ArraySeq.wrap(values));
    }

    @Override
    default @NotNull ImmutableSet<E> filter(@NotNull Predicate<? super E> predicate) {
        return filter(factory(), predicate);
    }

    @Override
    default @NotNull ImmutableSet<E> filterNot(@NotNull Predicate<? super E> predicate) {
        return filterNot(factory(), predicate);
    }

    @Override
    default @NotNull ImmutableSet<@NotNull E> filterNotNull() {
        return filter(Predicates.isNotNull());
    }

    @Override
    @SuppressWarnings("unchecked")
    default <U> @NotNull ImmutableSet<U> filterIsInstance(@NotNull Class<? extends U> clazz) {
        return (ImmutableSet<U>) filter(clazz::isInstance);
    }

    @Override
    default @NotNull ImmutableSet<E> toImmutableSet() {
        return this;
    }
}
