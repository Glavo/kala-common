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
package kala.collection.immutable;

import kala.annotations.StaticClass;
import kala.collection.base.Iterators;
import kala.collection.base.MapIterator;
import kala.tuple.Tuple;
import org.jetbrains.annotations.NotNull;

@StaticClass
final class ImmutableMaps {
    static sealed abstract class MapN<K, V> extends AbstractImmutableMap<K, V> {
        @Override
        public final int knownSize() {
            return size();
        }

        @Override
        public boolean isEmpty() {
            return size() == 0;
        }
    }

    static final class Map0<K, V> extends MapN<K, V> {

        static final Map0<?, ?> INSTANCE = new Map0<>();

        @Override
        public int size() {
            return 0;
        }

        @Override
        public @NotNull MapIterator<K, V> iterator() {
            return MapIterator.empty();
        }
    }

    static final class Map1<K, V> extends MapN<K, V> {

        private final K k0;
        private final V v0;

        Map1(K k0, V v0) {
            this.k0 = k0;
            this.v0 = v0;
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public @NotNull MapIterator<K, V> iterator() {
            return MapIterator.ofIterator(Iterators.of(Tuple.of(k0, v0)));
        }
    }

    static final class Map2<K, V> extends MapN<K, V> {

        private final K k0;
        private final V v0;
        private final K k1;
        private final V v1;

        Map2(K k0, V v0, K k1, V v1) {
            this.k0 = k0;
            this.v0 = v0;
            this.k1 = k1;
            this.v1 = v1;
        }

        @Override
        public int size() {
            return 2;
        }

        @Override
        public @NotNull MapIterator<K, V> iterator() {
            return MapIterator.ofIterator(Iterators.of(
                    Tuple.of(k0, v0),
                    Tuple.of(k1, v1)
            ));
        }
    }

    static final class Map3<K, V> extends MapN<K, V> {

        private final K k0;
        private final V v0;
        private final K k1;
        private final V v1;
        private final K k2;
        private final V v2;

        Map3(K k0, V v0, K k1, V v1, K k2, V v2) {
            this.k0 = k0;
            this.v0 = v0;
            this.k1 = k1;
            this.v1 = v1;
            this.k2 = k2;
            this.v2 = v2;
        }

        @Override
        public int size() {
            return 3;
        }

        @Override
        public @NotNull MapIterator<K, V> iterator() {
            return MapIterator.ofIterator(Iterators.of(
                    Tuple.of(k0, v0),
                    Tuple.of(k1, v1),
                    Tuple.of(k2, v2)
            ));
        }
    }
}
