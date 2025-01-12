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
import kala.collection.immutable.ImmutableSet;
import kala.collection.internal.view.SetViews;
import kala.function.Predicates;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Predicate;

public interface SetView<@Covariant E> extends CollectionView<E>, SetLike<E>, AnySetView<E> {

    @Override
    default @NotNull String className() {
        return "SetView";
    }

    @Override
    default @NotNull SetView<E> view() {
        return this;
    }

    @Override
    default @NotNull SetView<E> added(E value) {
        return contains(value) ? this : new SetViews.Added<>(this, value);
    }

    @Override
    default @NotNull SetView<E> addedAll(E @NotNull [] values) {
        return addedAll(ArraySeq.wrap(values));
    }

    @Override
    default @NotNull SetView<E> addedAll(@NotNull Iterable<? extends E> values) {
        ImmutableSet<E> addValues;
        if (values instanceof ImmutableSet<? extends E> immutableSet) {
            addValues = ImmutableSet.narrow(immutableSet);
        } else {
            addValues = ImmutableSet.from(values);
        }
        return addValues.isNotEmpty() ? new SetViews.AddedAll<>(this, addValues) : this;
    }

    @Override
    default @NotNull SetView<E> removed(E value) {
        return new SetViews.Filter<>(this, Predicates.isNotEqual(value));
    }

    @Override
    default @NotNull SetView<E> removedAll(E... values) {
        return removedAll(ArraySeq.wrap(values));
    }

    @Override
    default @NotNull SetView<E> removedAll(@NotNull Iterable<? extends E> values) {
        ImmutableSet<E> removedValues;
        if (values instanceof ImmutableSet<? extends E> immutableSet) {
            removedValues = ImmutableSet.narrow(immutableSet);
        } else {
            removedValues = ImmutableSet.from(values);
        }
        return removedValues.isNotEmpty() ? new SetViews.Filter<>(this, value -> !removedValues.contains(value)) : this;
    }

    @Override
    default @NotNull SetView<E> filter(@NotNull Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);
        return new SetViews.Filter<>(this, predicate);
    }

    @Override
    default @NotNull SetView<E> filterNot(@NotNull Predicate<? super E> predicate) {
        return filter(predicate.negate());
    }
}
