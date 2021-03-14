package org.glavo.kala.collection.mutable;

import org.glavo.kala.collection.Map;
import org.glavo.kala.collection.MapLike;
import org.glavo.kala.collection.factory.MapFactory;
import org.glavo.kala.collection.internal.convert.AsJavaConvert;
import org.glavo.kala.collection.internal.convert.FromJavaConvert;
import org.glavo.kala.control.Option;
import org.glavo.kala.tuple.Tuple2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public interface MutableMap<K, V> extends Map<K, V>, MutableMapLike<K, V> {

    //region Static Factories

    @SuppressWarnings({"unchecked", "rawtypes"})
    static <K, V> @NotNull MapFactory<K, V, ?, MutableMap<K, V>> factory() {
        return (MapFactory) MutableHashMap.factory();
    }

    static <K, V> @NotNull MutableMap<K, V> create() {
        return new MutableHashMap<>();
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

    @SuppressWarnings("unchecked")
    static <K, V> MutableMap<K, V> wrapJava(java.util.@NotNull Map<K, V> map) {
        Objects.requireNonNull(map);
        if (map instanceof AsJavaConvert.MutableMapAsJava<?, ?, ?>) {
            return ((MutableMap<K, V>) map);
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
