package org.glavo.kala.collection.immutable;

import org.glavo.kala.collection.MapLike;
import org.glavo.kala.collection.factory.MapFactory;
import org.glavo.kala.collection.mutable.MutableHashMap;
import org.glavo.kala.tuple.Tuple2;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@SuppressWarnings("unchecked")
public final class ImmutableHashMap<K, V> extends MutableHashMap.Frozen<K, V>
        implements ImmutableMapOps<K, V, ImmutableHashMap<?, ?>, ImmutableHashMap<K, V>> {

    private static final ImmutableHashMap<?, ?> EMPTY = new ImmutableHashMap<>(new MutableHashMap<>());
    private static final Factory<?, ?> FACTORY = new Factory<>();

    private ImmutableHashMap(MutableHashMap<K, V> source) {
        super(source);
    }

    //region Static Factories

    public static <K, V> @NotNull MapFactory<K, V, ?, ImmutableHashMap<K, V>> factory() {
        return (Factory<K, V>) FACTORY;
    }

    @SuppressWarnings("unchecked")
    public static <K, V> @NotNull ImmutableHashMap<K, V> empty() {
        return (ImmutableHashMap<K, V>) EMPTY;
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> of() {
        return empty();
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> of(K k1, V v1) {
        return new ImmutableHashMap<>(MutableHashMap.of(k1, v1));
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> of(
            K k1, V v1,
            K k2, V v2
    ) {
        return new ImmutableHashMap<>(MutableHashMap.of(k1, v1, k2, v2));
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> of(
            K k1, V v1,
            K k2, V v2,
            K k3, V v3
    ) {
        return new ImmutableHashMap<>(MutableHashMap.of(k1, v1, k2, v2, k3, v3));
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> of(
            K k1, V v1,
            K k2, V v2,
            K k3, V v3,
            K k4, V v4
    ) {
        return new ImmutableHashMap<>(MutableHashMap.of(k1, v1, k2, v2, k3, v3, k4, v4));
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> of(
            K k1, V v1,
            K k2, V v2,
            K k3, V v3,
            K k4, V v4,
            K k5, V v5
    ) {
        return new ImmutableHashMap<>(MutableHashMap.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> ofEntries() {
        return empty();
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1
    ) {
        return new ImmutableHashMap<>(MutableHashMap.ofEntries(entry1));
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2
    ) {
        return new ImmutableHashMap<>(MutableHashMap.ofEntries(entry1, entry2));
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2,
            @NotNull Tuple2<? extends K, ? extends V> entry3
    ) {
        return new ImmutableHashMap<>(MutableHashMap.ofEntries(entry1, entry2, entry3));
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2,
            @NotNull Tuple2<? extends K, ? extends V> entry3,
            @NotNull Tuple2<? extends K, ? extends V> entry4
    ) {
        return new ImmutableHashMap<>(MutableHashMap.ofEntries(entry1, entry2, entry3, entry4));
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2,
            @NotNull Tuple2<? extends K, ? extends V> entry3,
            @NotNull Tuple2<? extends K, ? extends V> entry4,
            @NotNull Tuple2<? extends K, ? extends V> entry5
    ) {
        return new ImmutableHashMap<>(MutableHashMap.ofEntries(entry1, entry2, entry3, entry4, entry5));
    }

    @SafeVarargs
    public static <K, V> @NotNull ImmutableHashMap<K, V> ofEntries(Tuple2<? extends K, ? extends V> @NotNull ... entries) {
        return new ImmutableHashMap<>(MutableHashMap.ofEntries(entries));
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> from(java.util.@NotNull Map<? extends K, ? extends V> values) {
        return new ImmutableHashMap<>(MutableHashMap.from(values));
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> from(@NotNull MapLike<? extends K, ? extends V> values) {
        return new ImmutableHashMap<>(MutableHashMap.from(values));
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> from(java.util.Map.Entry<? extends K, ? extends V> @NotNull [] values) {
        return new ImmutableHashMap<>(MutableHashMap.from(values));
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> from(@NotNull Iterable<? extends java.util.Map.Entry<? extends K, ? extends V>> values) {
        return new ImmutableHashMap<>(MutableHashMap.from(values));
    }

    //endregion

    @Override
    public final @NotNull String className() {
        return "ImmutableHashMap";
    }

    @Override
    public final @NotNull ImmutableHashMap<K, V> updated(K key, V value) {
        if (source.contains(key, value)) {
            return this;
        }
        MutableHashMap<K, V> nm = source.clone();
        nm.put(key, value);
        return new ImmutableHashMap<>(nm);
    }

    @Override
    public final @NotNull ImmutableHashMap<K, V> removed(K key) {
        if (!source.containsKey(key)) {
            return this;
        }
        MutableHashMap<K, V> nm = source.clone();
        nm.remove(key);
        return nm.isEmpty() ? empty() : new ImmutableHashMap<>(nm);
    }

    private static final class Factory<K, V> implements MapFactory<K, V, MutableHashMap<K, V>, ImmutableHashMap<K, V>> {
        @Override
        public final MutableHashMap<K, V> newBuilder() {
            return new MutableHashMap<>();
        }

        @Override
        public final ImmutableHashMap<K, V> build(MutableHashMap<K, V> builder) {
            return new ImmutableHashMap<>(Objects.requireNonNull(builder)); // Node: Unsafe operation, may need clone?
        }

        @Override
        public final void addToBuilder(MutableHashMap<K, V> builder, K key, V value) {
            builder.set(key, value);
        }

        @Override
        public final MutableHashMap<K, V> mergeBuilder(MutableHashMap<K, V> builder1, MutableHashMap<K, V> builder2) {
            builder1.putAll(builder2);
            return builder1;
        }
    }
}