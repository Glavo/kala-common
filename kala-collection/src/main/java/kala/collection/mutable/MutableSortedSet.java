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
package kala.collection.mutable;

import kala.annotations.DelegateBy;
import kala.collection.SortedSet;
import kala.collection.factory.CollectionFactory;
import kala.collection.internal.convert.AsJavaConvert;
import kala.function.CheckedConsumer;
import kala.function.CheckedIndexedConsumer;
import kala.function.IndexedConsumer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.function.Consumer;

public interface MutableSortedSet<E> extends MutableSet<E>, SortedSet<E> {

    @Override
    default java.util.@NotNull SortedSet<E> asJava() {
        return new AsJavaConvert.MutableSortedSetAsJava<>(this);
    }

    @Override
    @ApiStatus.NonExtendable
    default @NotNull CollectionFactory<E, ?, ? extends MutableSortedSet<E>> sortedIterableFactory() {
        return sortedIterableFactory(null);
    }

    @Override
    @NotNull <U> CollectionFactory<U, ?, ? extends MutableSortedSet<U>> sortedIterableFactory(Comparator<? super U> comparator);

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    default @NotNull MutableSet<E> clone() {
        return sortedIterableFactory().from(this);
    }


    @Override
    @ApiStatus.NonExtendable
    @DelegateBy("forEach(Consumer<E, Ex>)")
    @Contract(value = "_ -> this", pure = true)
    default @NotNull MutableSortedSet<E> onEach(@NotNull Consumer<? super E> action) {
        forEach(action);
        return this;
    }

    @Override
    @ApiStatus.NonExtendable
    @DelegateBy("onEach(Consumer<E, Ex>)")
    @Contract(value = "_ -> this", pure = true)
    default <Ex extends Throwable> @NotNull MutableSortedSet<E> onEachChecked(@NotNull CheckedConsumer<? super E, ? extends Ex> action) throws Ex {
        return onEach(action);
    }

    @Override
    @ApiStatus.NonExtendable
    @DelegateBy("onEach(Consumer<T, Ex>)")
    @Contract(value = "_ -> this", pure = true)
    default @NotNull MutableSortedSet<E> onEachUnchecked(@NotNull CheckedConsumer<? super E, ?> action) {
        return onEach(action);
    }

    @Override
    @ApiStatus.NonExtendable
    @DelegateBy("forEachIndexed(IndexedConsumer<T, Ex>)")
    @Contract(value = "_ -> this", pure = true)
    default @NotNull MutableSortedSet<E> onEachIndexed(@NotNull IndexedConsumer<? super E> action) {
        forEachIndexed(action);
        return this;
    }

    @Override
    @ApiStatus.NonExtendable
    @DelegateBy("onEachIndexed(IndexedConsumer<E, Ex>)")
    @Contract(value = "_ -> this", pure = true)
    default <Ex extends Throwable> @NotNull MutableSortedSet<E> onEachChecked(@NotNull CheckedIndexedConsumer<? super E, ? extends Ex> action) throws Ex {
        return onEachIndexed(action);
    }

    @Override
    @ApiStatus.NonExtendable
    @DelegateBy("onEachIndexed(IndexedConsumer<E, Ex>)")
    @Contract(value = "_ -> this", pure = true)
    default @NotNull MutableSortedSet<E> onEachUnchecked(@NotNull CheckedIndexedConsumer<? super E, ?> action) {
        return onEachIndexed(action);
    }
}
