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

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@StaticClass
final class ImmutableMaps {
    static sealed abstract class MapN<K, V> extends AbstractImmutableMap<K, V> {
        @Override
        public final int knownSize() {
            return size();
        }

        @Override
        public final boolean isEmpty() {
            return size() == 0;
        }
    }

    static final class Map0<K, V> extends MapN<K, V> implements Serializable {
        @Serial
        private static final long serialVersionUID = 0L;

        static final Map0<?, ?> INSTANCE = new Map0<>();

        @Override
        public int size() {
            return 0;
        }

        @Override
        public @NotNull MapIterator<K, V> iterator() {
            return MapIterator.empty();
        }

        @Override
        public @NotNull ImmutableMap<K, V> putted(K key, V value) {
            return new Map1<>(key, value);
        }

        @Serial
        private Object readResolve() {
            return INSTANCE;
        }
    }

    static final class Map1<K, V> extends MapN<K, V> implements Serializable {
        @Serial
        private static final long serialVersionUID = 0L;

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

        @Override
        public boolean containsKey(K key) {
            return Objects.equals(key, k0);
        }

        @Override
        public boolean containsValue(Object value) {
            return Objects.equals(value, v0);
        }

        @Override
        public @NotNull ImmutableMap<K, V> putted(K key, V value) {
            if (Objects.equals(key, k0)) {
                if (value == v0) {
                    return this;
                } else {
                    return new Map1<>(key, value);
                }
            }

            return new Map2<>(k0, v0, key, value);
        }
    }

    static final class Map2<K, V> extends MapN<K, V> implements Serializable {
        @Serial
        private static final long serialVersionUID = 0L;

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

        @Override
        public boolean containsKey(K key) {
            if (key != null) {
                return key.equals(k0) || key.equals(k1);
            } else {
                return k0 == null || k1 == null;
            }
        }

        @Override
        public boolean containsValue(Object value) {
            if (value != null) {
                return value.equals(v0) || value.equals(v1);
            } else {
                return v0 == null || v1 == null;
            }
        }

        @Override
        public @NotNull ImmutableMap<K, V> putted(K key, V value) {
            if (Objects.equals(key, k0)) {
                if (value == v0) {
                    return this;
                } else {
                    return new Map2<>(key, value, k1, v1);
                }
            }

            if (Objects.equals(key, k1)) {
                if (value == v1) {
                    return this;
                } else {
                    return new Map2<>(k0, v0, key, value);
                }
            }

            return new Map3<>(k0, v0, k1, v1, key, value);
        }
    }

    static final class Map3<K, V> extends MapN<K, V> implements Serializable {
        @Serial
        private static final long serialVersionUID = 0L;

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

        @Override
        public boolean containsKey(K key) {
            if (key != null) {
                return key.equals(k0) || key.equals(k1) || key.equals(k2);
            } else {
                return k0 == null || k1 == null || k2 == null;
            }
        }

        @Override
        public boolean containsValue(Object value) {
            if (value != null) {
                return value.equals(v0) || value.equals(v1) || value.equals(v2);
            } else {
                return v0 == null || v1 == null || v2 == null;
            }
        }

        @Override
        public @NotNull ImmutableMap<K, V> putted(K key, V value) {
            if (Objects.equals(key, k0)) {
                if (value == v0) {
                    return this;
                } else {
                    return new Map3<>(key, value, k1, v1, k2, v2);
                }
            }

            if (Objects.equals(key, k1)) {
                if (value == v1) {
                    return this;
                } else {
                    return new Map3<>(k0, v0, key, value, k2, v2);
                }
            }

            if (Objects.equals(key, k2)) {
                if (value == v2) {
                    return this;
                } else {
                    return new Map3<>(k0, v0, k1, v1, key, value);
                }
            }

            return super.putted(key, value);
        }
    }
}
