package org.glavo.kala.collection.mutable;

import org.glavo.kala.Tuple2;
import org.glavo.kala.collection.Map;
import org.glavo.kala.collection.internal.FromJavaConvert;
import org.glavo.kala.collection.base.MapIterator;
import org.jetbrains.annotations.NotNull;

public final class MutableHashMap<K, V> extends FromJavaConvert.MutableMapFromJava<K, V> {

    MutableHashMap(java.util.HashMap<K, V> source) {
        super(source);

    }

    public MutableHashMap() {
        this(new java.util.HashMap<>());
    }

    //region Static Factories

    public static <K, V> @NotNull MutableHashMap<K, V> of() {
        return new MutableHashMap<>();
    }

    public static <K, V> @NotNull MutableHashMap<K, V> of(K k1, V v1) {
        java.util.HashMap<K, V> m = new java.util.HashMap<>();
        m.put(k1, v1);
        return new MutableHashMap<>(m);
    }

    public static <K, V> @NotNull MutableHashMap<K, V> of(
            K k1, V v1,
            K k2, V v2
    ) {
        java.util.HashMap<K, V> m = new java.util.HashMap<>();
        m.put(k1, v1);
        m.put(k2, v2);
        return new MutableHashMap<>(m);
    }

    public static <K, V> @NotNull MutableHashMap<K, V> of(
            K k1, V v1,
            K k2, V v2,
            K k3, V v3
    ) {
        java.util.HashMap<K, V> m = new java.util.HashMap<>();
        m.put(k1, v1);
        m.put(k2, v2);
        m.put(k3, v3);
        return new MutableHashMap<>(m);
    }

    public static <K, V> @NotNull MutableHashMap<K, V> of(
            K k1, V v1,
            K k2, V v2,
            K k3, V v3,
            K k4, V v4
    ) {
        java.util.HashMap<K, V> m = new java.util.HashMap<>();
        m.put(k1, v1);
        m.put(k2, v2);
        m.put(k3, v3);
        m.put(k4, v4);
        return new MutableHashMap<>(m);
    }

    public static <K, V> @NotNull MutableHashMap<K, V> of(
            K k1, V v1,
            K k2, V v2,
            K k3, V v3,
            K k4, V v4,
            K k5, V v5
    ) {
        java.util.HashMap<K, V> m = new java.util.HashMap<>();
        m.put(k1, v1);
        m.put(k2, v2);
        m.put(k3, v3);
        m.put(k4, v4);
        m.put(k5, v5);
        return new MutableHashMap<>(m);
    }

    public static <K, V> @NotNull MutableHashMap<K, V> ofEntries() {
        return new MutableHashMap<>();
    }

    public static <K, V> @NotNull MutableHashMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1
    ) {
        MutableHashMap<K, V> res = new MutableHashMap<>();
        res.put(entry1);
        return res;
    }

    public static <K, V> @NotNull MutableHashMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2
    ) {
        MutableHashMap<K, V> res = new MutableHashMap<>();
        res.put(entry1);
        res.put(entry2);
        return res;
    }

    public static <K, V> @NotNull MutableHashMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2,
            @NotNull Tuple2<? extends K, ? extends V> entry3
    ) {
        MutableHashMap<K, V> res = new MutableHashMap<>();
        res.put(entry1);
        res.put(entry2);
        res.put(entry3);
        return res;
    }

    public static <K, V> @NotNull MutableHashMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2,
            @NotNull Tuple2<? extends K, ? extends V> entry3,
            @NotNull Tuple2<? extends K, ? extends V> entry4
    ) {
        MutableHashMap<K, V> res = new MutableHashMap<>();
        res.put(entry1);
        res.put(entry2);
        res.put(entry3);
        res.put(entry4);
        return res;
    }

    public static <K, V> @NotNull MutableHashMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2,
            @NotNull Tuple2<? extends K, ? extends V> entry3,
            @NotNull Tuple2<? extends K, ? extends V> entry4,
            @NotNull Tuple2<? extends K, ? extends V> entry5
    ) {
        MutableHashMap<K, V> res = new MutableHashMap<>();
        res.put(entry1);
        res.put(entry2);
        res.put(entry3);
        res.put(entry4);
        res.put(entry5);
        return res;
    }

    @SafeVarargs
    public static <K, V> @NotNull MutableHashMap<K, V> ofEntries(Tuple2<? extends K, ? extends V> @NotNull ... entries) {
        MutableHashMap<K, V> res = new MutableHashMap<>();
        for (Tuple2<? extends K, ? extends V> entry : entries) {
            res.set(entry);
        }
        return res;
    }

    public static <K, V> @NotNull MutableHashMap<K, V> from(java.util.@NotNull Map<? extends K, ? extends V> values) {
        return new MutableHashMap<>(new java.util.HashMap<>(values));
    }

    public static <K, V> @NotNull MutableHashMap<K, V> from(@NotNull Map<? extends K, ? extends V> values) {
        MapIterator<? extends K, ? extends V> it = values.iterator();
        java.util.HashMap<K, V> m = new java.util.HashMap<>();
        while (it.hasNext()) {
            m.put(it.nextKey(), it.getValue());
        }
        return new MutableHashMap<>(m);
    }

    public static <K, V> @NotNull MutableHashMap<K, V> from(java.util.Map.Entry<? extends K, ? extends V> @NotNull [] values) {
        java.util.HashMap<K, V> m = new java.util.HashMap<>();
        for (java.util.Map.Entry<? extends K, ? extends V> value : values) {
            m.put(value.getKey(), value.getValue());
        }
        return new MutableHashMap<>(m);
    }

    public static <K, V> @NotNull MutableHashMap<K, V> from(@NotNull Iterable<? extends java.util.Map.Entry<? extends K, ? extends V>> values) {
        java.util.HashMap<K, V> m = new java.util.HashMap<>();
        for (java.util.Map.Entry<? extends K, ? extends V> value : values) {
            m.put(value.getKey(), value.getValue());
        }
        return new MutableHashMap<>(m);
    }

    //endregion

    @Override
    public final @NotNull String className() {
        return "MutableHashMap";
    }
}
