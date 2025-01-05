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

import kala.collection.factory.MapFactory;

public abstract class AbstractMutableMapFactory<K, V, M extends MutableMap<K, V>> implements MapFactory<K, V, M, M> {
    @Override
    public M build(M m) {
        return m;
    }

    @Override
    public void addToBuilder(M m, K key, V value) {
        m.set(key, value);
    }

    @Override
    public M mergeBuilder(M builder1, M builder2) {
        builder1.putAll(builder2);
        return builder1;
    }
}
