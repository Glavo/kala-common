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

public interface MutableMap<K, V> extends Map<K, V> {

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

    @NotNull Option<V> put(K key, V value);

    default @NotNull Option<V> put(@NotNull Tuple2<? extends K, ? extends V> kv) {
        return put(kv.getKey(), kv.getValue());
    }

    default void set(K key, V value) {
        put(key, value);
    }

    default void set(@NotNull Tuple2<? extends K, ? extends V> kv) {
        set(kv.getKey(), kv.getValue());
    }

    default @NotNull Option<V> putIfAbsent(K key, V value) {
        Option<V> v = getOption(key);
        if (v.isEmpty()) {
            v = put(key, value);
        }
        return v;
    }

    @SuppressWarnings("unchecked")
    default void putAll(java.util.@NotNull Map<? extends K, ? extends V> m) {
        if (m instanceof AsJavaConvert.MapAsJava<?, ?, ?>) {
            putAll(((Map<K, V>) m));
            return;
        }
        m.forEach(this::set);
    }

    default void putAll(@NotNull MapLike<? extends K, ? extends V> m) {
        if (m == this) {
            return;
        }
        m.forEach(this::set);
    }

    @NotNull Option<V> remove(K key);

    @Contract(mutates = "this")
    void clear();

    default Option<V> replace(K key, V value) {
        Option<V> v = getOption(key);
        if (v.isDefined()) {
            return put(key, value);
        } else {
            return Option.none();
        }
    }

    default boolean replace(K key, V oldValue, V newValue) {
        if (getOption(key).contains(oldValue)) {
            set(key, newValue);
            return true;
        } else {
            return false;
        }
    }

    void replaceAll(@NotNull BiFunction<? super K, ? super V, ? extends V> function);

    default @NotNull MutableSet<Tuple2<K, V>> asMutableSet() {
        return new MutableSet<Tuple2<K, V>>() {
            @Override
            public final boolean add(@NotNull Tuple2<K, V> value) {

                return !put(value._1, value._2).isEmpty();
            }

            @Override
            @SuppressWarnings("unchecked")
            public final boolean remove(Object value) {
                if (!(value instanceof Tuple2<?, ?>)) {
                    return false;
                }
                Tuple2<K, V> tuple = (Tuple2<K, V>) value;
                if (MutableMap.this.getOption(tuple._1).contains(tuple._2)) {
                    MutableMap.this.remove(tuple._1);
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public void clear() {

            }

            @Override
            public @NotNull Iterator<Tuple2<K, V>> iterator() {
                return MutableMap.this.iterator();
            }
        };
    }
}
