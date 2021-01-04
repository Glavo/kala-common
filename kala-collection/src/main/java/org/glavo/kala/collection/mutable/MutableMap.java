package org.glavo.kala.collection.mutable;

import org.glavo.kala.Tuple2;
import org.glavo.kala.collection.Map;
import org.glavo.kala.control.Option;
import org.jetbrains.annotations.NotNull;

public interface MutableMap<K, V> extends Map<K, V> {

    @Override
    default @NotNull String className() {
        return "MutableMap";
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

    default void putAll(@NotNull java.util.Map<? extends K, ? extends V> m) {
        m.forEach(this::set);
    }

    default void putAll(@NotNull Map<? extends K, ? extends V> m) {
        m.forEach(this::set);
    }

    @NotNull Option<V> remove(K key);
}
