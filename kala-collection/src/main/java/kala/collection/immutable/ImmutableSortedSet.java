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
package kala.collection.immutable;

import kala.collection.SortedSet;
import kala.collection.factory.CollectionFactory;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.function.Function;

public interface ImmutableSortedSet<E> extends ImmutableSet<E>, SortedSet<E> {

    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> param1", pure = true)
    static <E> ImmutableSortedSet<E> narrow(ImmutableSortedSet<? extends E> set) {
        return (ImmutableSortedSet<E>) set;
    }

    @Override
    @ApiStatus.NonExtendable
    default @NotNull CollectionFactory<E, ?, ? extends ImmutableSortedSet<E>> sortedIterableFactory() {
        return sortedIterableFactory(null);
    }

    @Override
    <U> @NotNull CollectionFactory<U, ?, ? extends ImmutableSortedSet<U>> sortedIterableFactory(Comparator<? super U> comparator);
}
