package kala.collection;

import kala.Equatable;
import kala.collection.factory.MapFactory;
import kala.collection.immutable.ImmutableMap;
import kala.collection.internal.convert.AsJavaConvert;
import kala.collection.internal.convert.FromJavaConvert;
import kala.tuple.Tuple2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collector;

public interface Map<K, V> extends MapLike<K, V>, Equatable {

    int HASH_MAGIC = 124549981;

    static int hashCode(@NotNull Map<?, ?> map) {
        return map.iterator().hash() + Map.HASH_MAGIC;
    }

    @SuppressWarnings("unchecked")
    static boolean equals(@NotNull Map<?, ?> map1, @NotNull Map<?, ?> map2) {
        if (!map1.canEqual(map2) || !map2.canEqual(map1)) {
            return false;
        }
        if (map1.size() != map2.size()) {
            return false;
        }

        MapIterator<?, ?> it = map1.iterator();
        try {
            while (it.hasNext()) {
                if (!((Map<Object, ?>) map2).contains(it.nextKey(), it.getValue())) {
                    return false;
                }
            }
        } catch (ClassCastException e) {
            return false;
        }
        return true;
    }

    //region Static Factories

    @SuppressWarnings({"unchecked", "rawtypes"})
    static <K, V> @NotNull MapFactory<K, V, ?, Map<K, V>> factory() {
        return (MapFactory) ImmutableMap.factory();
    }

    static <T, K, V> @NotNull Collector<T, ?, Map<K, V>> collector(
            @NotNull Function<? super T, ? extends K> keyMapper,
            @NotNull Function<? super T, ? extends V> valueMapper
    ) {
        return MapFactory.collector(factory(), keyMapper, valueMapper);
    }

    static <K, V> @NotNull Map<K, V> empty() {
        return ImmutableMap.empty();
    }

    static <K, V> @NotNull Map<K, V> of() {
        return empty();
    }

    static <K, V> @NotNull Map<K, V> of(K k1, V v1) {
        return ImmutableMap.of(k1, v1);
    }

    static <K, V> @NotNull Map<K, V> of(
            K k1, V v1,
            K k2, V v2
    ) {
        return ImmutableMap.of(k1, v1, k2, v2);
    }

    static <K, V> @NotNull Map<K, V> of(
            K k1, V v1,
            K k2, V v2,
            K k3, V v3
    ) {
        return ImmutableMap.of(k1, v1, k2, v2, k3, v3);
    }

    static <K, V> @NotNull Map<K, V> of(
            K k1, V v1,
            K k2, V v2,
            K k3, V v3,
            K k4, V v4
    ) {
        return ImmutableMap.of(k1, v1, k2, v2, k3, v3, k4, v4);
    }

    static <K, V> @NotNull Map<K, V> of(
            K k1, V v1,
            K k2, V v2,
            K k3, V v3,
            K k4, V v4,
            K k5, V v5
    ) {
        return ImmutableMap.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5);
    }

    static <K, V> @NotNull Map<K, V> of(Object ...values) {
        return ImmutableMap.of(values);
    }

    static <K, V> @NotNull Map<K, V> ofEntries() {
        return empty();
    }

    static <K, V> @NotNull Map<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1
    ) {
        return ImmutableMap.ofEntries(entry1);
    }

    static <K, V> @NotNull Map<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2
    ) {
        return ImmutableMap.ofEntries(entry1, entry2);
    }

    static <K, V> @NotNull Map<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2,
            @NotNull Tuple2<? extends K, ? extends V> entry3
    ) {
        return ImmutableMap.ofEntries(entry1, entry2, entry3);
    }

    static <K, V> @NotNull Map<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2,
            @NotNull Tuple2<? extends K, ? extends V> entry3,
            @NotNull Tuple2<? extends K, ? extends V> entry4
    ) {
        return ImmutableMap.ofEntries(entry1, entry2, entry3, entry4);
    }

    static <K, V> @NotNull Map<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2,
            @NotNull Tuple2<? extends K, ? extends V> entry3,
            @NotNull Tuple2<? extends K, ? extends V> entry4,
            @NotNull Tuple2<? extends K, ? extends V> entry5
    ) {
        return ImmutableMap.ofEntries(entry1, entry2, entry3, entry4, entry5);
    }

    @SafeVarargs
    static <K, V> @NotNull Map<K, V> ofEntries(Tuple2<? extends K, ? extends V> @NotNull ... entries) {
        return ImmutableMap.ofEntries(entries);
    }

    static <K, V> @NotNull Map<K, V> from(java.util.@NotNull Map<? extends K, ? extends V> values) {
        return ImmutableMap.from(values);
    }

    static <K, V> @NotNull Map<K, V> from(@NotNull MapLike<? extends K, ? extends V> values) {
        return ImmutableMap.from(values);
    }

    static <K, V> @NotNull Map<K, V> from(java.util.Map.Entry<? extends K, ? extends V> @NotNull [] values) {
        return ImmutableMap.from(values);
    }

    static <K, V> @NotNull Map<K, V> from(@NotNull Iterable<? extends java.util.Map.Entry<? extends K, ? extends V>> values) {
        return ImmutableMap.from(values);
    }

    @SuppressWarnings("unchecked")
    static <K, V> Map<K, V> wrapJava(java.util.@NotNull @UnmodifiableView Map<K, V> map) {
        Objects.requireNonNull(map);
        if (map instanceof AsJavaConvert.MapAsJava<?, ?, ?>) {
            return ((Map<K, V>) map);
        }
        return new FromJavaConvert.MapFromJava<>(map);
    }

    //endregion

    @Override
    default @NotNull String className() {
        return "Map";
    }

    default <NK, NV> @NotNull MapFactory<NK, NV, ?, ? extends Map<NK, NV>> mapFactory() {
        return Map.factory();
    }

    default java.util.@NotNull @UnmodifiableView Map<K, V> asJava() {
        return new AsJavaConvert.MapAsJava<>(this);
    }

    @Override
    default boolean canEqual(Object other) {
        return other instanceof Map<?, ?>;
    }
}
