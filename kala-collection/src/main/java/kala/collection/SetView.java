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
import kala.collection.internal.view.SetViews;
import kala.collection.mutable.MutableHashSet;
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
    default @NotNull SetLike<E> added(E value) {
        return contains(value) ? this : new SetViews.Added<>(this, value);
    }

    @Override
    default @NotNull SetLike<E> addedAll(@NotNull Iterable<? extends E> values) {
        MutableHashSet<E> addValues = new MutableHashSet<>();
        for (E value : values) {
            if (!contains(value)) {
                addValues.add(value);
            }
        }
        return addValues.isNotEmpty() ? new SetViews.AddedAll<>(this, addValues.toImmutableSet()) : this;
    }

    @Override
    default @NotNull SetLike<E> addedAll(E @NotNull [] values) {
        MutableHashSet<E> addValues = new MutableHashSet<>();
        for (E value : values) {
            if (!contains(value)) {
                addValues.add(value);
            }
        }
        return addValues.isNotEmpty() ? new SetViews.AddedAll<>(this, addValues.toImmutableSet()) : this;
    }

    @Override
    default @NotNull SetView<E> filter(@NotNull Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);
        return new SetViews.Filter<>(this, predicate);
    }

    @Override
    default @NotNull SetView<E> filterNot(@NotNull Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);
        return new SetViews.Filter<>(this, predicate.negate());
    }
}
