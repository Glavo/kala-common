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
import kala.collection.internal.convert.AsJavaConvert;
import kala.collection.internal.convert.FromJavaConvert;
import kala.tuple.Tuple2;
import kala.collection.Map;
import kala.collection.factory.MapFactory;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collector;

public interface MutableMap<K, V> extends Map<K, V>, MutableMapLike<K, V> {

    //region Static Factories

    @SuppressWarnings({"unchecked", "rawtypes"})
    static <K, V> @NotNull MapFactory<K, V, ?, MutableMap<K, V>> factory() {
        return (MapFactory) MutableHashMap.factory();
    }

    static <T, K, V> @NotNull Collector<T, ?, MutableMap<K, V>> collector(
            @NotNull Function<? super T, ? extends K> keyMapper,
            @NotNull Function<? super T, ? extends V> valueMapper
    ) {
        return MapFactory.collector(factory(), keyMapper, valueMapper);
    }

    static <K, V> @NotNull MutableMap<K, V> create() {
        return MutableHashMap.create();
    }

    static <K, V> @NotNull MutableMap<K, V> of() {
        return MutableHashMap.of();
    }

    static <K, V> @NotNull MutableMap<K, V> of(K k1, V v1) {
        return MutableHashMap.of(k1, v1);
    }

    static <K, V> @NotNull MutableMap<K, V> of(
            K k1, V v1,
            K k2, V v2
    ) {
        return MutableHashMap.of(k1, v1, k2, v2);
    }

    static <K, V> @NotNull MutableMap<K, V> of(
            K k1, V v1,
            K k2, V v2,
            K k3, V v3
    ) {
        return MutableHashMap.of(k1, v1, k2, v2, k3, v3);
    }

    static <K, V> @NotNull MutableMap<K, V> of(
            K k1, V v1,
            K k2, V v2,
            K k3, V v3,
            K k4, V v4
    ) {
        return MutableHashMap.of(k1, v1, k2, v2, k3, v3, k4, v4);
    }

    static <K, V> @NotNull MutableMap<K, V> of(
            K k1, V v1,
            K k2, V v2,
            K k3, V v3,
            K k4, V v4,
            K k5, V v5
    ) {
        return MutableHashMap.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5);
    }

    static <K, V> @NotNull MutableMap<K, V> of(Object ...values) {
        return MutableHashMap.of(values);
    }

    static <K, V> @NotNull MutableMap<K, V> ofEntries() {
        return MutableHashMap.ofEntries();
    }

    static <K, V> @NotNull MutableMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1
    ) {
        return MutableHashMap.ofEntries(entry1);
    }

    static <K, V> @NotNull MutableMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2
    ) {
        return MutableHashMap.ofEntries(entry1, entry2);
    }

    static <K, V> @NotNull MutableMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2,
            @NotNull Tuple2<? extends K, ? extends V> entry3
    ) {
        return MutableHashMap.ofEntries(entry1, entry2, entry3);
    }

    static <K, V> @NotNull MutableMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2,
            @NotNull Tuple2<? extends K, ? extends V> entry3,
            @NotNull Tuple2<? extends K, ? extends V> entry4
    ) {
        return MutableHashMap.ofEntries(entry1, entry2, entry3, entry4);
    }

    static <K, V> @NotNull MutableMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2,
            @NotNull Tuple2<? extends K, ? extends V> entry3,
            @NotNull Tuple2<? extends K, ? extends V> entry4,
            @NotNull Tuple2<? extends K, ? extends V> entry5
    ) {
        return MutableHashMap.ofEntries(entry1, entry2, entry3, entry4, entry5);
    }

    @SafeVarargs
    static <K, V> @NotNull MutableMap<K, V> ofEntries(Tuple2<? extends K, ? extends V> @NotNull ... entries) {
        return MutableHashMap.from(entries);
    }

    static <K, V> @NotNull MutableMap<K, V> from(java.util.@NotNull Map<? extends K, ? extends V> values) {
        return MutableHashMap.from(values);
    }

    static <K, V> @NotNull MutableMap<K, V> from(@NotNull MapLike<? extends K, ? extends V> values) {
        return MutableHashMap.from(values);
    }

    static <K, V> @NotNull MutableMap<K, V> from(java.util.Map.Entry<? extends K, ? extends V> @NotNull [] values) {
        return MutableHashMap.from(values);
    }

    static <K, V> @NotNull MutableMap<K, V> from(@NotNull Iterable<? extends java.util.Map.Entry<? extends K, ? extends V>> values) {
        return MutableHashMap.from(values);
    }

    static <K, V> MutableMap<K, V> wrapJava(java.util.@NotNull Map<K, V> map) {
        Objects.requireNonNull(map);
        if (map instanceof AsJavaConvert.MutableMapAsJava) {
            return ((AsJavaConvert.MutableMapAsJava<K, V, ?>) map).source;
        }
        return new FromJavaConvert.MutableMapFromJava<>(map);
    }

    //endregion

    @Override
    default @NotNull String className() {
        return "MutableMap";
    }

    @Override
    default <NK, NV> @NotNull MapFactory<NK, NV, ?, ? extends MutableMap<NK, NV>> mapFactory() {
        return MutableMap.factory();
    }

    default @NotNull MutableMapEditor<K, V, ? extends MutableMap<K, V>> edit() {
        return new MutableMapEditor<>(this);
    }

    @Override
    default java.util.@NotNull Map<K, V> asJava() {
        return new AsJavaConvert.MutableMapAsJava<>(this);
    }


}
