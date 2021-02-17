package org.glavo.kala.collection.immutable;

import org.glavo.kala.Tuple2;
import org.glavo.kala.collection.Map;
import org.glavo.kala.collection.internal.FromJavaConvert;
import org.glavo.kala.traversable.MapIterator;
import org.jetbrains.annotations.NotNull;

public final class ImmutableHashMap<K, V> extends FromJavaConvert.MapFromJava<K, V> implements ImmutableMap<K, V> {

    private static final ImmutableMap<?, ?> EMPTY = new ImmutableHashMap<>();

    ImmutableHashMap(java.util.HashMap<K, V> source) {
        super(source);

    }

    public ImmutableHashMap() {
        this(new java.util.HashMap<>());
    }

    //region Static Factories

    @SuppressWarnings("unchecked")
    public static <K, V> @NotNull ImmutableHashMap<K, V> empty() {
        return (ImmutableHashMap<K, V>) EMPTY;
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> of() {
        return empty();
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> of(K k1, V v1) {
        java.util.HashMap<K, V> m = new java.util.HashMap<>();
        m.put(k1, v1);
        return new ImmutableHashMap<>(m);
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> of(
            K k1, V v1,
            K k2, V v2
    ) {
        java.util.HashMap<K, V> m = new java.util.HashMap<>();
        m.put(k1, v1);
        m.put(k2, v2);
        return new ImmutableHashMap<>(m);
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> of(
            K k1, V v1,
            K k2, V v2,
            K k3, V v3
    ) {
        java.util.HashMap<K, V> m = new java.util.HashMap<>();
        m.put(k1, v1);
        m.put(k2, v2);
        m.put(k3, v3);
        return new ImmutableHashMap<>(m);
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> of(
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
        return new ImmutableHashMap<>(m);
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> of(
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
        return new ImmutableHashMap<>(m);
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> ofEntries() {
        return new ImmutableHashMap<>();
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1
    ) {
        java.util.HashMap<K, V> m = new java.util.HashMap<>();
        m.put(entry1.getKey(), entry1.getValue());
        return new ImmutableHashMap<>(m);
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2
    ) {
        java.util.HashMap<K, V> m = new java.util.HashMap<>();
        m.put(entry1.getKey(), entry1.getValue());
        m.put(entry2.getKey(), entry2.getValue());
        return new ImmutableHashMap<>(m);
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2,
            @NotNull Tuple2<? extends K, ? extends V> entry3
    ) {
        java.util.HashMap<K, V> m = new java.util.HashMap<>();
        m.put(entry1.getKey(), entry1.getValue());
        m.put(entry2.getKey(), entry2.getValue());
        m.put(entry3.getKey(), entry3.getValue());
        return new ImmutableHashMap<>(m);
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2,
            @NotNull Tuple2<? extends K, ? extends V> entry3,
            @NotNull Tuple2<? extends K, ? extends V> entry4
    ) {
        java.util.HashMap<K, V> m = new java.util.HashMap<>();
        m.put(entry1.getKey(), entry1.getValue());
        m.put(entry2.getKey(), entry2.getValue());
        m.put(entry3.getKey(), entry3.getValue());
        m.put(entry4.getKey(), entry4.getValue());
        return new ImmutableHashMap<>(m);
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2,
            @NotNull Tuple2<? extends K, ? extends V> entry3,
            @NotNull Tuple2<? extends K, ? extends V> entry4,
            @NotNull Tuple2<? extends K, ? extends V> entry5
    ) {
        java.util.HashMap<K, V> m = new java.util.HashMap<>();
        m.put(entry1.getKey(), entry1.getValue());
        m.put(entry2.getKey(), entry2.getValue());
        m.put(entry3.getKey(), entry3.getValue());
        m.put(entry4.getKey(), entry4.getValue());
        m.put(entry5.getKey(), entry5.getValue());
        return new ImmutableHashMap<>(m);
    }

    @SafeVarargs
    public static <K, V> @NotNull ImmutableHashMap<K, V> ofEntries(Tuple2<? extends K, ? extends V> @NotNull ... entries) {
        java.util.HashMap<K, V> m = new java.util.HashMap<>();
        for (Tuple2<? extends K, ? extends V> entry : entries) {
            m.put(entry.getKey(), entry.getValue());
        }
        return new ImmutableHashMap<>(m);
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> from(java.util.@NotNull Map<? extends K, ? extends V> values) {
        return new ImmutableHashMap<>(new java.util.HashMap<>(values));
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> from(@NotNull Map<? extends K, ? extends V> values) {
        MapIterator<? extends K, ? extends V> it = values.iterator();
        java.util.HashMap<K, V> m = new java.util.HashMap<>();
        while (it.hasNext()) {
            m.put(it.nextKey(), it.getValue());
        }
        return new ImmutableHashMap<>(m);
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> from(java.util.Map.Entry<? extends K, ? extends V> @NotNull [] values) {
        java.util.HashMap<K, V> m = new java.util.HashMap<>();
        for (java.util.Map.Entry<? extends K, ? extends V> value : values) {
            m.put(value.getKey(), value.getValue());
        }
        return new ImmutableHashMap<>(m);
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> from(@NotNull Iterable<? extends java.util.Map.Entry<? extends K, ? extends V>> values) {
        java.util.HashMap<K, V> m = new java.util.HashMap<>();
        for (java.util.Map.Entry<? extends K, ? extends V> value : values) {
            m.put(value.getKey(), value.getValue());
        }
        return new ImmutableHashMap<>(m);
    }

    //endregion

    @Override
    public final @NotNull String className() {
        return "ImmutableHashMap";
    }
}
