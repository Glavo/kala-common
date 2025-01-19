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

import java.util.HashMap;

public final class MapFromJavaTest implements MapTestTemplate {
    @Override
    public <K, V> MapFactory<K, V, ?, ? extends Map<K, V>> factory() {
        return new Factory<>();
    }

    @Override
    public Class<?> mapType() {
        return null;
    }

    private static final class Factory<K, V> implements MapFactory<K, V, HashMap<K, V>, Map<K, V>> {

        @Override
        public HashMap<K, V> newBuilder() {
            return new java.util.HashMap<>();
        }

        @Override
        public Map<K, V> build(HashMap<K, V> builder) {
            return Map.wrapJava(builder);
        }

        @Override
        public void addToBuilder(HashMap<K, V> builder, K key, V value) {
            builder.put(key, value);
        }

        @Override
        public HashMap<K, V> mergeBuilder(HashMap<K, V> builder1, HashMap<K, V> builder2) {
            builder1.putAll(builder2);
            return builder1;
        }
    }
}
