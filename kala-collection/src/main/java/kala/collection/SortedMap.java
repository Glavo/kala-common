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
package kala.collection;

import kala.collection.factory.MapFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;

public interface SortedMap<K, V> extends Map<K, V> {
    default @NotNull MapFactory<K, V, ?, ? extends SortedMap<K, V>> sortedMapFactory() {
        return sortedMapFactory(comparator());
    }

    @NotNull <NK, NV> MapFactory<NK, NV, ?, ? extends SortedMap<NK, NV>> sortedMapFactory(Comparator<? super NK> comparator);

    @Nullable Comparator<? super K> comparator();

    K firstKey();

    K lastKey();
}
