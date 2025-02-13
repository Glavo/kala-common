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

import kala.collection.Map;
import kala.collection.MapLike;
import kala.collection.factory.MapBuilder;
import kala.collection.factory.MapFactory;
import kala.tuple.Tuple2;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.stream.Collector;

public interface ImmutableMap<K, V> extends Map<K, V> {

    //region Static Factories

    @SuppressWarnings({"unchecked", "rawtypes"})
    static <K, V> @NotNull MapFactory<K, V, ?, ImmutableMap<K, V>> factory() {
        return (MapFactory) ImmutableHashMap.factory();
    }

    static <T, K, V> @NotNull Collector<T, ?, ImmutableMap<K, V>> collector(
            @NotNull Function<? super T, ? extends K> keyMapper,
            @NotNull Function<? super T, ? extends V> valueMapper
    ) {
        return MapFactory.collector(factory(), keyMapper, valueMapper);
    }

    static <K, V> @NotNull MapBuilder<K, V, ImmutableMap<K, V>> newMapBuilder() {
        return MapBuilder.narrow(ImmutableHashMap.<K, V>factory().newMapBuilder());
    }

    static <K, V> @NotNull ImmutableMap<K, V> empty() {
        return ImmutableHashMap.empty();
    }

    static <K, V> @NotNull ImmutableMap<K, V> of() {
        return empty();
    }

    static <K, V> @NotNull ImmutableMap<K, V> of(K k1, V v1) {
        return ImmutableHashMap.of(k1, v1);
    }

    static <K, V> @NotNull ImmutableMap<K, V> of(
            K k1, V v1,
            K k2, V v2
    ) {
        return ImmutableHashMap.of(k1, v1, k2, v2);
    }

    static <K, V> @NotNull ImmutableMap<K, V> of(
            K k1, V v1,
            K k2, V v2,
            K k3, V v3
    ) {
        return ImmutableHashMap.of(k1, v1, k2, v2, k3, v3);
    }

    static <K, V> @NotNull ImmutableMap<K, V> of(
            K k1, V v1,
            K k2, V v2,
            K k3, V v3,
            K k4, V v4
    ) {
        return ImmutableHashMap.of(k1, v1, k2, v2, k3, v3, k4, v4);
    }

    static <K, V> @NotNull ImmutableMap<K, V> of(
            K k1, V v1,
            K k2, V v2,
            K k3, V v3,
            K k4, V v4,
            K k5, V v5
    ) {
        return ImmutableHashMap.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5);
    }

    static <K, V> @NotNull ImmutableMap<K, V> of(Object ...values) {
        return ImmutableHashMap.of(values);
    }

    static <K, V> @NotNull ImmutableMap<K, V> ofEntries() {
        return empty();
    }

    static <K, V> @NotNull ImmutableMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1
    ) {
        return ImmutableHashMap.ofEntries(entry1);
    }

    static <K, V> @NotNull ImmutableMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2
    ) {
        return ImmutableHashMap.ofEntries(entry1, entry2);
    }

    static <K, V> @NotNull ImmutableMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2,
            @NotNull Tuple2<? extends K, ? extends V> entry3
    ) {
        return ImmutableHashMap.ofEntries(entry1, entry2, entry3);
    }

    static <K, V> @NotNull ImmutableMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2,
            @NotNull Tuple2<? extends K, ? extends V> entry3,
            @NotNull Tuple2<? extends K, ? extends V> entry4
    ) {
        return ImmutableHashMap.ofEntries(entry1, entry2, entry3, entry4);
    }

    static <K, V> @NotNull ImmutableMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2,
            @NotNull Tuple2<? extends K, ? extends V> entry3,
            @NotNull Tuple2<? extends K, ? extends V> entry4,
            @NotNull Tuple2<? extends K, ? extends V> entry5
    ) {
        return ImmutableHashMap.ofEntries(entry1, entry2, entry3, entry4, entry5);
    }

    @SafeVarargs
    static <K, V> @NotNull ImmutableMap<K, V> ofEntries(Tuple2<? extends K, ? extends V> @NotNull ... entries) {
        return ImmutableHashMap.ofEntries(entries);
    }

    static <K, V> @NotNull ImmutableMap<K, V> from(java.util.@NotNull Map<? extends K, ? extends V> values) {
        return ImmutableHashMap.from(values);
    }

    static <K, V> @NotNull ImmutableMap<K, V> from(@NotNull MapLike<? extends K, ? extends V> values) {
        return ImmutableHashMap.from(values);
    }

    static <K, V> @NotNull ImmutableMap<K, V> from(java.util.Map.Entry<? extends K, ? extends V> @NotNull [] values) {
        return ImmutableHashMap.from(values);
    }

    static <K, V> @NotNull ImmutableMap<K, V> from(@NotNull Iterable<? extends java.util.Map.Entry<? extends K, ? extends V>> values) {
        return ImmutableHashMap.from(values);
    }

    //endregion

    @Override
    default @NotNull String className() {
        return "ImmutableMap";
    }

    @Override
    default @NotNull <NK, NV> MapFactory<NK, NV, ?, ? extends ImmutableMap<NK, NV>> mapFactory() {
        return ImmutableMap.factory();
    }

    @Override
    default @NotNull ImmutableMap<K, V> toImmutableMap() {
        return this;
    }
}
