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
import kala.tuple.Tuple2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public class MutableMapEditor<K, V, M extends MutableMap<K, V>> {
    protected final @NotNull M source;

    public MutableMapEditor(@NotNull M source) {
        this.source = source;
    }

    public final @NotNull M done() {
        return source;
    }

    @Contract("_, _ -> this")
    public @NotNull MutableMapEditor<K, V, M> set(K key, V value) {
        source.set(key, value);
        return this;
    }

    @Contract("_, _ -> this")
    public @NotNull MutableMapEditor<K, V, M> put(K key, V value) {
        source.put(key, value);
        return this;
    }

    @Contract("_ -> this")
    public @NotNull MutableMapEditor<K, V, M> put(@NotNull Tuple2<? extends K, ? extends V> kv) {
        source.put(kv);
        return this;
    }

    @Contract("_, _ -> this")
    public @NotNull MutableMapEditor<K, V, M> putIfAbsent(K key, V value) {
        source.putIfAbsent(key, value);
        return this;
    }

    @Contract("_ -> this")
    public @NotNull MutableMapEditor<K, V, M> putAll(@NotNull java.util.Map<? extends K, ? extends V> m) {
        source.putAll(m);
        return this;
    }

    @Contract("_ -> this")
    public @NotNull MutableMapEditor<K, V, M> putAll(@NotNull MapLike<? extends K, ? extends V> m) {
        source.putAll(m);
        return this;
    }

    @Contract("_, _ -> this")
    public @NotNull MutableMapEditor<K, V, M> replace(K key, V value) {
        source.replace(key, value);
        return this;
    }

    @Contract("_, _, _ -> this")
    public @NotNull MutableMapEditor<K, V, M> replace(K key, V oldValue, V newValue) {
        source.replace(key, oldValue, newValue);
        return this;
    }

    @Contract("_ -> this")
    public @NotNull MutableMapEditor<K, V, M> replaceAll(@NotNull BiFunction<? super K, ? super V, ? extends V> function) {
        source.replaceAll(function);
        return this;
    }
}
