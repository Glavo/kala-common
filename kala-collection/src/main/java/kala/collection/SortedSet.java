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
package kala.collection;

import kala.annotations.Covariant;
import kala.annotations.DelegateBy;
import kala.collection.base.OrderedTraversable;
import kala.collection.immutable.ImmutableSortedArraySet;
import kala.collection.immutable.ImmutableSortedSet;
import kala.collection.internal.CollectionHelper;
import kala.collection.internal.convert.AsJavaConvert;
import kala.collection.factory.CollectionFactory;
import kala.function.Predicates;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.function.Predicate;

public interface SortedSet<@Covariant E> extends Set<E>, OrderedTraversable<E> {

    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> param1", pure = true)
    static <E> SortedSet<E> narrow(SortedSet<? extends E> set) {
        return (SortedSet<E>) set;
    }

    <U> @NotNull CollectionFactory<U, ?, ? extends SortedSet<U>> sortedIterableFactory(Comparator<? super U> comparator);

    @DelegateBy("sortedIterableFactory(Comparator<E>)")
    default @NotNull CollectionFactory<E, ?, ? extends SortedSet<E>> sortedIterableFactory() {
        return sortedIterableFactory(comparator());
    }

    @Override
    default java.util.@NotNull SortedSet<E> asJava() {
        return new AsJavaConvert.SortedSetAsJava<>(this);
    }

    default @Nullable Comparator<? super E> comparator() {
        return null;
    }

    @Contract(pure = true)
    default E getFirst() {
        return iterator().next();
    }

    @Contract(pure = true)
    default E getLast() {
        return reverseIterator().next();
    }

    @Override
    @ApiStatus.NonExtendable
    @DelegateBy("getLast()")
    default E max() {
        return getLast();
    }

    @Override
    @ApiStatus.NonExtendable
    @DelegateBy("getFirst()")
    default E min() {
        return getFirst();
    }

    private CollectionFactory<E, ?, ? extends ImmutableSortedSet<E>> immutableSortedSetFactory() {
        if (this instanceof ImmutableSortedSet<E> immutableSortedSet) {
            return immutableSortedSet.sortedIterableFactory();
        } else {
            return ImmutableSortedArraySet.factory(comparator());
        }
    }

    @Override
    default @NotNull ImmutableSortedSet<E> added(E value) {
        if (this instanceof ImmutableSortedSet<E> immutableSet && contains(value)) {
            return immutableSet;
        }

        final var factory = immutableSortedSetFactory();
        final var builder = factory.newCollectionBuilder();
        for (E e : this) {
            builder.plusAssign(e);
        }
        builder.plusAssign(value);
        return builder.build();
    }

    @Override
    @ApiStatus.NonExtendable
    @SuppressWarnings("unchecked")
    @DelegateBy("addedAll(Iterable<E>)")
    default @NotNull ImmutableSortedSet<E> addedAll(E... values) {
        return addedAll(ArraySeq.wrap(values));
    }

    @Override
    default @NotNull ImmutableSortedSet<E> addedAll(@NotNull Iterable<? extends E> values) {
        final var factory = immutableSortedSetFactory();
        final var builder = factory.newCollectionBuilder();
        for (E e : this) {
            builder.plusAssign(e);
        }
        for (E value : values) {
            builder.plusAssign(value);
        }
        return builder.build();
    }

    @Override
    default @NotNull ImmutableSortedSet<E> removed(E value) {
        if (this instanceof ImmutableSortedSet<E> immutableSet && !contains(value)) {
            return immutableSet;
        }

        final var factory = immutableSortedSetFactory();
        final var builder = factory.newCollectionBuilder();
        builder.sizeHint(size() - 1);
        if (value == null) {
            for (E e : this) {
                if (null != e) {
                    builder.plusAssign(e);
                }
            }
        } else {
            for (E e : this) {
                if (!value.equals(e)) {
                    builder.plusAssign(e);
                }
            }
        }
        return builder.build();
    }

    @ApiStatus.NonExtendable
    @SuppressWarnings("unchecked")
    @DelegateBy("removedAll(Iterable<E>)")
    default @NotNull ImmutableSortedSet<E> removedAll(E... values) {
        return removedAll(ArraySeq.wrap(values));
    }

    @Override
    default @NotNull ImmutableSortedSet<E> removedAll(@NotNull Iterable<? extends E> values) {
        final var factory = immutableSortedSetFactory();
        final Predicate<? super E> contains = CollectionHelper.containsPredicate(values);
        final var builder = factory.newCollectionBuilder();
        for (E e : this) {
            if (!contains.test(e)) {
                builder.plusAssign(e);
            }
        }
        return builder.build();
    }

    @Override
    default @NotNull ImmutableSortedSet<E> filter(@NotNull Predicate<? super E> predicate) {
        return filter(immutableSortedSetFactory(), predicate);
    }

    @Override
    @ApiStatus.NonExtendable
    @DelegateBy("filter(Predicate<E>)")
    default @NotNull ImmutableSortedSet<E> filterNot(@NotNull Predicate<? super E> predicate) {
        return filter(predicate.negate());
    }

    @Override
    @ApiStatus.NonExtendable
    @DelegateBy("filter(Predicate<E>)")
    default @NotNull ImmutableSortedSet<@NotNull E> filterNotNull() {
        return filter(Predicates.isNotNull());
    }

    @Override
    @ApiStatus.NonExtendable
    @DelegateBy("filter(Predicate<E>)")
    default <U> @NotNull ImmutableSortedSet<U> filterIsInstance(@NotNull Class<? extends U> clazz) {
        @SuppressWarnings("unchecked")
        ImmutableSortedSet<U> result = (ImmutableSortedSet<U>) filter(Predicates.isInstance(clazz));
        return result;
    }
}
