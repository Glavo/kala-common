package org.glavo.kala.collection.base;

import org.glavo.kala.tuple.Tuple;
import org.glavo.kala.tuple.Tuple2;
import org.glavo.kala.function.CheckedBiConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

@SuppressWarnings("unchecked")
public interface MapIterator<K, V> extends Iterator<Tuple2<K, V>> {
    static <K, V> @NotNull MapIterator<K, V> empty() {
        return (MapIterator<K, V>) MapIterators.EMPTY;
    }

    static <K, V> @NotNull MapIterator<K, V> ofIterator(@NotNull Iterator<? extends java.util.Map.Entry<? extends K, ? extends V>> it) {
        if (!it.hasNext()) { // implicit null check of it
            return empty();
        }
        return new MapIterators.OfIterator<>(it);
    }

    K nextKey();

    V getValue();

    boolean hasNext();

    @Override
    default Tuple2<K, V> next() {
        return Tuple.of(nextKey(), getValue());
    }

    default boolean containsKey(K key) {
        if (key == null) {
            while (hasNext()) {
                if (null == nextKey()) {
                    return true;
                }
            }
        } else {
            while (hasNext()) {
                if (key.equals(nextKey())) {
                    return true;
                }
            }
        }
        return false;
    }

    default boolean containsValue(V value) {
        if (value == null) {
            while (hasNext()) {
                nextKey();
                if (null == getValue()) {
                    return true;
                }
            }
        } else {
            while (hasNext()) {
                nextKey();
                if (value.equals(getValue())) {
                    return true;
                }
            }
        }
        return false;
    }

    default boolean anyMatch(@NotNull BiPredicate<? super K, ? super V> predicate) {
        while (hasNext()) {
            if (predicate.test(nextKey(), getValue())) {
                return true;
            }
        }
        return false;
    }

    default boolean allMatch(@NotNull BiPredicate<? super K, ? super V> predicate) {
        while (hasNext()) {
            if (!predicate.test(nextKey(), getValue())) {
                return false;
            }
        }
        return true;
    }

    default boolean noneMatch(@NotNull BiPredicate<? super K, ? super V> predicate) {
        while (hasNext()) {
            if (predicate.test(nextKey(), getValue())) {
                return false;
            }
        }
        return true;
    }

    default int hash() {
        int hash = 0;
        while (hasNext()) {
            hash *= 31;
            hash += Objects.hashCode(nextKey()) * 31 + Objects.hashCode(getValue());
        }
        return hash;
    }

    default void forEach(@NotNull BiConsumer<? super K, ? super V> action) {
        while (hasNext()) {
            action.accept(nextKey(), getValue());
        }
    }

    default <Ex extends Throwable> void forEachChecked(
            @NotNull CheckedBiConsumer<? super K, ? super V, ? extends Ex> action) throws Ex {
        forEach(action);
    }

    default void forEachUnchecked(@NotNull CheckedBiConsumer<? super K, ? super V, ?> action) {
        forEach(action);
    }
}
