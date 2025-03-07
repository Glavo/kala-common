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
import kala.collection.immutable.ImmutableCollection;
import kala.collection.immutable.ImmutableSet;
import kala.function.CheckedConsumer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.function.Consumer;
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

    @Override
    @ApiStatus.NonExtendable
    default @NotNull ImmutableSet<E> collect() {
        return toSet();
    }

    @Override
    default @NotNull ImmutableCollection<E> toCollection() {
        return toSet();
    }

    @Override
    @ApiStatus.NonExtendable
    @DelegateBy("forEach(Consumer<E, Ex>)")
    @Contract(value = "_ -> this", pure = true)
    default @NotNull SetLike<E> onEach(@NotNull Consumer<? super E> action) {
        forEach(action);
        return this;
    }

    @Override
    @ApiStatus.NonExtendable
    @DelegateBy("onEach(Consumer<E, Ex>)")
    @Contract(value = "_ -> this", pure = true)
    default <Ex extends Throwable> @NotNull SetLike<E> onEachChecked(@NotNull CheckedConsumer<? super E, ? extends Ex> action) throws Ex {
        return onEach(action);
    }

    @Override
    @ApiStatus.NonExtendable
    @DelegateBy("onEach(Consumer<T, Ex>)")
    @Contract(value = "_ -> this", pure = true)
    default @NotNull SetLike<E> onEachUnchecked(@NotNull CheckedConsumer<? super E, ?> action) {
        return onEach(action);
    }
}
