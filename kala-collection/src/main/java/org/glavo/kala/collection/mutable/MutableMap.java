package org.glavo.kala.collection.mutable;

import org.glavo.kala.tuple.Tuple2;
import org.glavo.kala.collection.Map;
import org.glavo.kala.control.Option;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public interface MutableMap<K, V> extends Map<K, V> {

    @Override
    default @NotNull String className() {
        return "MutableMap";
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

    default void putAll(@NotNull java.util.Map<? extends K, ? extends V> m) {
        m.forEach(this::set);
    }

    default void putAll(@NotNull Map<? extends K, ? extends V> m) {
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
