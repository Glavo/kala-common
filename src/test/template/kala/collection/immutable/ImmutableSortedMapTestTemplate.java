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

import kala.collection.SortedMapTestTemplate;
import kala.collection.factory.MapFactory;
import kala.tuple.Tuple2;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

@SuppressWarnings("unchecked")
public interface ImmutableSortedMapTestTemplate extends ImmutableMapTestTemplate, SortedMapTestTemplate {
    @Override
    default <K, V> MapFactory<K, V, ?, ? extends ImmutableSortedMap<K, V>> factory() {
        return factory(null);
    }

    <K, V> MapFactory<K, V, ?, ? extends ImmutableSortedMap<K, V>> factory(Comparator<? super K> comparator);

    @Override
    default <K, V> ImmutableSortedMap<K, V> ofEntries(@NotNull Tuple2<K, V>... tuples) {
        return this.<K, V>factory().ofEntries(tuples);
    }

    default <K, V> ImmutableSortedMap<K, V> ofEntries(Comparator<? super K> comparator, @NotNull Tuple2<K, V>... tuples) {
        return this.<K, V>factory(comparator).ofEntries(tuples);
    }
}
