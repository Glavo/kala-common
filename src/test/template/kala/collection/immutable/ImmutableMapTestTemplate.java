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

import kala.collection.MapTestTemplate;
import kala.collection.factory.MapFactory;
import kala.tuple.Tuple2;

public interface ImmutableMapTestTemplate extends MapTestTemplate {
    @Override
    <K, V> MapFactory<K, V, ?, ? extends ImmutableMap<K, V>> factory();

    @Override
    @SuppressWarnings("unchecked")
    default <K, V> ImmutableMap<K, V> ofEntries(Tuple2<K, V>... tuples) {
        return this.<K, V>factory().ofEntries(tuples);
    }
}
