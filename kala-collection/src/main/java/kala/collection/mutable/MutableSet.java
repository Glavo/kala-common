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
package kala.collection.mutable;

import kala.annotations.DelegateBy;
import kala.annotations.ReplaceWith;
import kala.collection.ArraySeq;
import kala.collection.Collection;
import kala.collection.base.Growable;
import kala.collection.immutable.ImmutableSet;
import kala.collection.internal.CollectionHelper;
import kala.collection.internal.convert.AsJavaConvert;
import kala.collection.factory.CollectionFactory;
import kala.collection.Set;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface MutableSet<E> extends MutableCollection<E>, Set<E>, Growable<E>, MutableAnySet<E> {

    //region Static Factories

    @Contract(pure = true)
    static <E> @NotNull CollectionFactory<E, ?, MutableSet<E>> factory() {
        return CollectionFactory.narrow(MutableHashSet.factory());
    }

    @Contract(value = "-> new")
    static <E> @NotNull MutableSet<E> create() {
        return MutableHashSet.create();
    }

    @Contract(value = "-> new")
    static <E> @NotNull MutableSet<E> of() {
        return MutableHashSet.of();
    }

    @Contract(value = "_ -> new")
    static <E> @NotNull MutableSet<E> of(E value1) {
        return MutableHashSet.of(value1);
    }

    @Contract(value = "_, _ -> new")
    static <E> @NotNull MutableSet<E> of(E value1, E value2) {
        return MutableHashSet.of(value1, value2);
    }

    @Contract(value = "_, _, _ -> new")
    static <E> @NotNull MutableSet<E> of(E value1, E value2, E value3) {
        return MutableHashSet.of(value1, value2, value3);
    }

    @Contract(value = "_, _, _, _ -> new")
    static <E> @NotNull MutableSet<E> of(E value1, E value2, E value3, E value4) {
        return MutableHashSet.of(value1, value2, value3, value4);
    }

    @Contract(value = "_, _, _, _, _ -> new")
    static <E> @NotNull MutableSet<E> of(E value1, E value2, E value3, E value4, E value5) {
        return MutableHashSet.of(value1, value2, value3, value4, value5);
    }

    @SafeVarargs
    @Contract(value = "_ -> new")
    static <E> @NotNull MutableSet<E> of(E... values) {
        return from(values);
    }

    @Contract(value = "_ -> new")
    static <E> @NotNull MutableSet<E> from(E @NotNull [] values) {
        return MutableHashSet.from(values);
    }

    @Contract(value = "_ -> new")
    static <E> @NotNull MutableSet<E> from(@NotNull Iterable<? extends E> values) {
        return MutableHashSet.from(values);
    }

    @Contract(value = "_ -> new")
    static <E> @NotNull MutableSet<E> from(@NotNull Iterator<? extends E> it) {
        return MutableHashSet.from(it);
    }

    @Contract(value = "_ -> new")
    static <E> @NotNull MutableSet<E> from(@NotNull Stream<? extends E> stream) {
        return MutableHashSet.from(stream);
    }

    static <E, C extends MutableSet<E>> MutableSetEditor<E, C> edit(@NotNull C set) {
        return new MutableSetEditor<>(set);
    }

    //endregion

    @Override
    default @NotNull String className() {
        return "MutableSet";
    }

    @Override
    default <U> @NotNull CollectionFactory<U, ?, ? extends MutableSet<U>> iterableFactory() {
        return factory();
    }

    @Override
    default java.util.@NotNull Set<E> asJava() {
        return new AsJavaConvert.MutableSetAsJava<>(this);
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    default @NotNull MutableSet<E> clone() {
        return this.<E>iterableFactory().from(this);
    }

    @Contract(mutates = "this")
    boolean add(@Flow(targetIsContainer = true) E value);

    @Contract(mutates = "this")
    @SuppressWarnings("unchecked")
    default boolean addAll(@Flow(sourceIsContainer = true, targetIsContainer = true) E... values) {
        Objects.requireNonNull(values);
        return addAll(ArraySeq.wrap(values));
    }

    @Contract(mutates = "this")
    default boolean addAll(
            @NotNull @Flow(sourceIsContainer = true, targetIsContainer = true) Iterable<? extends E> values
    ) {
        if (values == this) {
            return false;
        }

        boolean m = false;
        for (E value : values) {
            if (this.add(value)) {
                m = true;
            }
        }
        return m;
    }

    @Override
    @ReplaceWith("add(E)")
    default void plusAssign(E value) {
        add(value);
    }

    @Override
    @ReplaceWith("addAll(E[])")
    default void plusAssign(E @NotNull [] values) {
        addAll(values);
    }

    @Override
    @ReplaceWith("addAll(Iterable)")
    default void plusAssign(@NotNull Iterable<? extends E> values) {
        addAll(values);
    }

    @Contract(mutates = "this")
    void clear();

    default void trimToSize() {
    }

    @Contract(mutates = "this")
    boolean remove(Object value);

    @Contract(mutates = "this")
    @DelegateBy("removeAll(Iterable<?>)")
    default boolean removeAll(Object @NotNull [] values) {
        Objects.requireNonNull(values);
        return removeAll(ArraySeq.wrap(values));
    }

    @Contract(mutates = "this")
    @DelegateBy("removeIf(Predicate<?>)")
    default boolean removeAll(@NotNull Iterable<?> values) {
        Objects.requireNonNull(values);
        if (this == values) {
            if (isEmpty()) {
                return false;
            } else {
                clear();
                return true;
            }
        }

        return switch (size()) {
            case 0 -> false;
            case 1 -> {
                if (CollectionHelper.contains(values, getAny())) {
                    clear();
                    yield true;
                } else {
                    yield false;
                }
            }
            default -> removeIf(CollectionHelper.containsPredicate(values));
        };
    }

    @Contract(mutates = "this")
    @SuppressWarnings("unchecked")
    default boolean removeIf(@NotNull Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);

        final Object[] arr = toArray();
        final int oldSize = arr.length;

        for (Object e : arr) {
            if (predicate.test((E) e)) {
                this.remove(e);
            }
        }

        return size() != oldSize;
    }

    @Contract(mutates = "this")
    @DelegateBy("retainAll(Iterable<?>)")
    default boolean retainAll(Object @NotNull [] values) {
        return retainAll(ArraySeq.wrap(values));
    }

    @Contract(mutates = "this")
    @DelegateBy("retainIf(Predicate<E>)")
    default boolean retainAll(@NotNull Iterable<?> values) {
        Objects.requireNonNull(values);
        if (this == values) {
            return false;
        }

        return switch (size()) {
            case 0 -> false;
            case 1 -> {
                if (CollectionHelper.contains(values, getAny())) {
                    yield false;
                } else {
                    clear();
                    yield true;
                }
            }
            default -> retainIf(CollectionHelper.containsPredicate(values));
        };
    }

    @Contract(mutates = "this")
    @DelegateBy("removeIf(Predicate<E>)")
    default boolean retainIf(@NotNull Predicate<? super E> predicate) {
        return removeIf(predicate.negate());
    }
}
