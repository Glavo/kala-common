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
package kala.collection;

import kala.collection.internal.view.MapViews;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface MapView<K, V> extends MapLike<K, V> {

    interface WithDefault<K, V> extends MapView<K, V> {
        @NotNull Function<? super K, ? extends V> getDefaultFunction();
    }

    @SuppressWarnings("unchecked")
    static <K, V> @NotNull MapView<K, V> empty() {
        return (MapView<K, V>) MapViews.Empty.INSTANCE;
    }

    @Override
    default @NotNull String className() {
        return "MapView";
    }

    @Override
    default @NotNull MapView<K, V> view() {
        return this;
    }

    default <U> @NotNull CollectionView<U> map(@NotNull BiFunction<? super K, ? super V, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        return new MapViews.Mapped<>(this, mapper);
    }

    default <NV> @NotNull MapView<K, NV> mapValues(@NotNull BiFunction<? super K, ? super V, ? extends NV> mapper) {
        Objects.requireNonNull(mapper);
        return new MapViews.MapValues<>(this, mapper);
    }

    @Override
    default @NotNull MapView<K, V> putted(K key, V value) {
        return new MapViews.Putted<>(this, key, value);
    }

    @Override
    default @NotNull MapView<K, V> removed(K key) {
        return new MapViews.Removed<>(this, key);
    }
}
