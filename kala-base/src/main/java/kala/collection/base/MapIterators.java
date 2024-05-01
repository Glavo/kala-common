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
package kala.collection.base;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;

public final class MapIterators {
    private MapIterators() {
    }

    public static final MapIterator<?, ?> EMPTY = new MapIterator<Object, Object>() {
        @Override
        public Object nextKey() {
            throw new NoSuchElementException();
        }

        @Override
        public Object getValue() {
            return null;
        }

        @Override
        public boolean hasNext() {
            return false;
        }
    };

    public static final class OfIterator<K, V> extends AbstractMapIterator<K, V> {

        private final Iterator<? extends Map.Entry<? extends K, ? extends V>> source;

        private V value;

        public OfIterator(Iterator<? extends Map.Entry<? extends K, ? extends V>> source) {
            this.source = source;
        }

        @Override
        public boolean hasNext() {
            return source.hasNext();
        }

        @Override
        public K nextKey() {
            Map.Entry<? extends K, ? extends V> n = source.next();
            this.value = n.getValue();
            return n.getKey();
        }

        @Override
        public V getValue() {
            return value;
        }
    }

    public static final class Mapped<E, K, V> extends AbstractIterator<E> {
        private final @NotNull MapIterator<? extends K, ? extends V> source;
        private final @NotNull BiFunction<? super K, ? super V, ? extends E> mapper;

        public Mapped(@NotNull MapIterator<? extends K, ? extends V> source, @NotNull BiFunction<? super K, ? super V, ? extends E> mapper) {
            this.source = source;
            this.mapper = mapper;
        }

        @Override
        public boolean hasNext() {
            return source.hasNext();
        }

        @Override
        public E next() {
            return mapper.apply(source.nextKey(), source.getValue());
        }
    }

    public static final class KeysIterator<K> extends AbstractIterator<K> {
        private final MapIterator<? extends K, ?> source;

        public KeysIterator(MapIterator<? extends K, ?> source) {
            this.source = source;
        }

        @Override
        public boolean hasNext() {
            return source.hasNext();
        }

        @Override
        public K next() {
            return source.nextKey();
        }
    }

    public static final class ValuesIterator<V> extends AbstractIterator<V> {
        private final MapIterator<?, ? extends V> source;

        public ValuesIterator(MapIterator<?, ? extends V> source) {
            this.source = source;
        }

        @Override
        public boolean hasNext() {
            return source.hasNext();
        }

        @Override
        public V next() {
            source.nextKey();
            return source.getValue();
        }
    }

    public static final class MapValues<K, V, OldV> extends AbstractMapIterator<K, V> {
        private final @NotNull MapIterator<? extends K, ? extends OldV> source;
        private final @NotNull BiFunction<? super K, ? super OldV, ? extends V> mapper;

        private K k;

        public MapValues(@NotNull MapIterator<? extends K, ? extends OldV> source, @NotNull BiFunction<? super K, ? super OldV, ? extends V> mapper) {
            this.source = source;
            this.mapper = mapper;
        }

        @Override
        public boolean hasNext() {
            return source.hasNext();
        }

        @Override
        public K nextKey() {
            return k = source.nextKey();
        }

        @Override
        public V getValue() {
            return mapper.apply(k, source.getValue());
        }
    }
}
