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

import kala.annotations.DelegateBy;
import kala.collection.factory.CollectionFactory;
import kala.collection.immutable.ImmutableSet;
import kala.collection.internal.CollectionHelper;
import kala.collection.internal.convert.AsJavaConvert;
import kala.collection.internal.convert.FromJavaConvert;
import kala.collection.internal.view.SetViews;
import kala.function.Predicates;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Iterator;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface Set<E> extends Collection<E>, SetLike<E>, AnySet<E> {

    //region Static Factories

    @SuppressWarnings("unchecked")
    static <E> Set<E> narrow(Set<? extends E> set) {
        return (Set<E>) set;
    }

    static <E> CollectionFactory<E, ?, Set<E>> factory() {
        return CollectionFactory.narrow(ImmutableSet.factory());
    }

    static <E> @NotNull Set<E> wrapJava(java.util.@NotNull Set<E> source) {
        return new FromJavaConvert.SetFromJava<>(source);
    }

    static <E> @NotNull Set<E> empty() {
        return ImmutableSet.empty();
    }

    static <E> @NotNull Set<E> of() {
        return ImmutableSet.of();
    }

    static <E> @NotNull Set<E> of(E value1) {
        return ImmutableSet.of(value1);
    }

    static <E> @NotNull Set<E> of(E value1, E value2) {
        return ImmutableSet.of(value1, value2);
    }

    static <E> @NotNull Set<E> of(E value1, E value2, E value3) {
        return ImmutableSet.of(value1, value2, value3);
    }

    static <E> @NotNull Set<E> of(E value1, E value2, E value3, E value4) {
        return ImmutableSet.of(value1, value2, value3, value4);
    }

    static <E> @NotNull Set<E> of(E value1, E value2, E value3, E value4, E value5) {
        return ImmutableSet.of(value1, value2, value3, value4, value5);
    }

    @SafeVarargs
    static <E> @NotNull Set<E> of(E... values) {
        return ImmutableSet.of(values);
    }

    static <E> @NotNull Set<E> from(E @NotNull [] values) {
        return ImmutableSet.from(values);
    }

    static <E> @NotNull Set<E> from(@NotNull Iterable<? extends E> values) {
        return ImmutableSet.from(values);
    }

    static <E> @NotNull Set<E> from(@NotNull Iterator<? extends E> it) {
        return ImmutableSet.from(it);
    }

    static <E> @NotNull Set<E> from(@NotNull Stream<? extends E> stream) {
        return ImmutableSet.from(stream);
    }


    //endregion

    static int hashCode(@NotNull Set<?> set) {
        int h = SET_HASH_MAGIC;
        for (Object e : set) {
            if (e != null) {
                h += e.hashCode();
            }
        }
        return h;
    }

    static boolean equals(@NotNull Set<?> set1, @NotNull AnySet<?> set2) {
        if (!set1.canEqual(set2) || !set2.canEqual(set1)) {
            return false;
        }
        return set1.size() == set2.size() && set1.containsAll(set2.asGeneric());
    }

    @Override
    default @NotNull String className() {
        return "Set";
    }

    @Override
    default <U> @NotNull CollectionFactory<U, ?, ? extends Set<U>> iterableFactory() {
        return factory();
    }

    @Override
    default @NotNull SetView<E> view() {
        return new SetViews.Of<>(this);
    }

    @Override
    default java.util.@NotNull Set<E> asJava() {
        return new AsJavaConvert.SetAsJava<>(this);
    }

    @Override
    default @NotNull ImmutableSet<E> added(E value) {
        if (this instanceof ImmutableSet<E> immutableSet && contains(value)) {
            return immutableSet;
        }

        var factory = CollectionHelper.immutableSetFactoryBy(this);
        var builder = factory.newCollectionBuilder();
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
    default @NotNull ImmutableSet<E> addedAll(E... values) {
        return addedAll(ArraySeq.wrap(values));
    }

    @Override
    default @NotNull ImmutableSet<E> addedAll(@NotNull Iterable<? extends E> values) {
        var factory = CollectionHelper.immutableSetFactoryBy(this);
        var builder = factory.newCollectionBuilder();
        for (E e : this) {
            builder.plusAssign(e);
        }
        for (E value : values) {
            builder.plusAssign(value);
        }
        return builder.build();
    }

    default @NotNull ImmutableSet<E> removed(E value) {
        if (this instanceof ImmutableSet<E> immutableSet && !contains(value)) {
            return immutableSet;
        }

        var factory = CollectionHelper.immutableSetFactoryBy(this);
        var builder = factory.newCollectionBuilder();
        builder.sizeHint(size() -1);
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
    default @NotNull ImmutableSet<E> removedAll(E... values) {
        return removedAll(ArraySeq.wrap(values));
    }

    default @NotNull ImmutableSet<E> removedAll(@NotNull Iterable<? extends E> values) {
        final var factory = CollectionHelper.immutableSetFactoryBy(this);
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
    default @NotNull ImmutableSet<E> filter(@NotNull Predicate<? super E> predicate) {
        return filter(CollectionHelper.immutableSetFactoryBy(this), predicate);
    }

    @Override
    @ApiStatus.NonExtendable
    @DelegateBy("filter(Predicate<E>)")
    default @NotNull ImmutableSet<E> filterNot(@NotNull Predicate<? super E> predicate) {
        return filter(predicate.negate());
    }

    @Override
    @ApiStatus.NonExtendable
    @DelegateBy("filter(Predicate<E>)")
    default @NotNull ImmutableSet<@NotNull E> filterNotNull() {
        return filter(Predicates.isNotNull());
    }

    @Override
    @ApiStatus.NonExtendable
    @DelegateBy("filter(Predicate<E>)")
    default <U> @NotNull ImmutableSet<U> filterIsInstance(@NotNull Class<? extends U> clazz) {
        @SuppressWarnings("unchecked")
        ImmutableSet<U> result = (ImmutableSet<U>) filter(Predicates.isInstance(clazz));
        return result;
    }
}
