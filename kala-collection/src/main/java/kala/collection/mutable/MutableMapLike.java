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

import kala.collection.MapLike;
import kala.collection.base.MapIterator;
import kala.collection.internal.convert.AsJavaConvert;
import kala.control.Option;
import kala.tuple.Tuple2;
import kala.collection.Map;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public interface MutableMapLike<K, V> extends MapLike<K, V> {

    @Override
    default @NotNull String className() {
        return "MutableMapLike";
    }

    default V getOrPut(K key, @NotNull Supplier<? extends V> defaultValue) {
        Option<V> res = getOption(key);
        if (res.isDefined()) {
            return res.get();
        } else {
            V v = defaultValue.get();
            set(key, v);
            return v;
        }
    }

    @Contract(mutates = "this")
    @NotNull Option<V> put(K key, V value);

    @Contract(mutates = "this")
    default @NotNull Option<V> put(@NotNull Tuple2<? extends K, ? extends V> kv) {
        return put(kv.getKey(), kv.getValue());
    }

    @Contract(mutates = "this")
    default void set(K key, V value) {
        put(key, value);
    }

    @Contract(mutates = "this")
    default void set(@NotNull Tuple2<? extends K, ? extends V> kv) {
        set(kv.getKey(), kv.getValue());
    }

    @Contract(mutates = "this")
    default @NotNull Option<V> putIfAbsent(K key, V value) {
        Option<V> v = getOption(key);
        if (v.isEmpty()) {
            v = put(key, value);
        }
        return v;
    }

    @Contract(mutates = "this")
    @SuppressWarnings("unchecked")
    default void putAll(java.util.@NotNull Map<? extends K, ? extends V> m) {
        if (m instanceof AsJavaConvert.MapAsJava<?, ?, ?>) {
            putAll(((Map<K, V>) m));
            return;
        }
        m.forEach(this::set);
    }

    @Contract(mutates = "this")
    default void putAll(@NotNull MapLike<? extends K, ? extends V> m) {
        if (m == this) {
            return;
        }
        m.forEach(this::set);
    }

    @Contract(mutates = "this")
    @NotNull Option<V> remove(K key);

    @Contract(mutates = "this")
    default boolean removeAll(@NotNull Iterable<? extends K> keys) {
        boolean changed = false;
        for (K key : keys) {
            if (remove(key).isDefined()) {
                changed = true;
            }
        }
        return changed;
    }

    @Contract(mutates = "this")
    default boolean removeAll(@NotNull MapLike<K, V> values) {
        boolean changed = false;

        MapIterator<K, V> it = values.iterator();
        while (it.hasNext()) {
            K k = it.nextKey();
            V v = it.getValue();

            if (getOption(k).contains(v)) {
                remove(k);
                changed = true;
            }
        }

        return changed;
    }

    @Contract(mutates = "this")
    void clear();

    default @NotNull Option<V> replace(K key, V value) {
        if (containsKey(key)) {
            return put(key, value);
        } else {
            return Option.none();
        }
    }

    default boolean replace(K key, V oldValue, V newValue) {
        if (contains(key, oldValue)) {
            set(key, newValue);
            return true;
        } else {
            return false;
        }
    }

    void replaceAll(@NotNull BiFunction<? super K, ? super V, ? extends V> function);

    default @NotNull MutableSet<Tuple2<K, V>> asMutableSet() {
        return new AbstractMutableSet<Tuple2<K, V>>() {
            @Override
            public final boolean add(@NotNull Tuple2<K, V> value) {

                return !put(value.component1(), value.component2()).isEmpty();
            }

            @Override
            @SuppressWarnings("unchecked")
            public final boolean remove(Object value) {
                if (!(value instanceof Tuple2<?, ?>)) {
                    return false;
                }
                Tuple2<K, V> tuple = (Tuple2<K, V>) value;
                if (MutableMapLike.this.contains(tuple)) {
                    MutableMapLike.this.remove(tuple.component1());
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public void clear() {
                MutableMapLike.this.clear();
            }

            @Override
            public @NotNull Iterator<Tuple2<K, V>> iterator() {
                return MutableMapLike.this.iterator();
            }
        };
    }
}
