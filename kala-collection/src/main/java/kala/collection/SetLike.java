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

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.function.Predicate;

public interface SetLike<E> extends CollectionLike<E>, AnySetLike<E> {
    @Override
    default @NotNull String className() {
        return "SetLike";
    }

    @Override
    @NotNull SetView<E> view();

    default Predicate<E> asPredicate() {
        return (Predicate<E> & Serializable) this::contains;
    }

    @NotNull SetLike<E> added(E value);

    @SuppressWarnings("unchecked")
    @NotNull SetLike<E> addedAll(E... values);

    @NotNull SetLike<E> addedAll(@NotNull Iterable<? extends E> values);

    @NotNull SetLike<E> removed(E value);

    @SuppressWarnings("unchecked")
    @NotNull SetLike<E> removedAll(E... values);

    @NotNull SetLike<E> removedAll(@NotNull Iterable<? extends E> values);

    @Override
    @NotNull SetLike<E> filter(@NotNull Predicate<? super E> predicate);

    @Override
    @NotNull SetLike<E> filterNot(@NotNull Predicate<? super E> predicate);

    @Override
    @NotNull SetLike<@NotNull E> filterNotNull();

    @Override
    <U> @NotNull SetLike<@NotNull U> filterIsInstance(@NotNull Class<? extends U> clazz);
}
