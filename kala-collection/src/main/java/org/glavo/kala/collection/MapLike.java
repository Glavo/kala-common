package org.glavo.kala.collection;

import org.glavo.kala.collection.base.MapIterator;
import org.glavo.kala.collection.internal.view.MapViews;
import org.glavo.kala.collection.base.Growable;
import org.glavo.kala.control.Option;
import org.glavo.kala.function.CheckedBiConsumer;
import org.glavo.kala.tuple.Tuple2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.*;

public interface MapLike<K, V> {

    default @NotNull String className() {
        return "MapLike";
    }

    @NotNull MapIterator<K, V> iterator();

    default @NotNull MapView<K, V> view() {
        return new MapViews.Of<>(this);
    }

    //region Size Info

    default boolean isEmpty() {
        int ks = knownSize();
        if (ks < 0) {
            return !iterator().hasNext();
        } else {
            return ks == 0;
        }
    }

    default int size() {
        int ks = knownSize();
        if (ks >= 0) {
            return ks;
        }

        int c = 0;
        MapIterator<K, V> it = iterator();
        while (it.hasNext()) {
            it.nextKey();
            ++c;
        }
        return c;
    }

    default int knownSize() {
        return -1;
    }

    //endregion

    default V get(K key) {
        return getOption(key).get();
    }

    default @Nullable V getOrNull(K key) {
        return getOption(key).getOrNull();
    }

    default @NotNull Option<V> getOption(K key) {
        MapIterator<K, V> it = iterator();
        if (key == null) {
            while (it.hasNext()) {
                if (null == it.nextKey()) {
                    return Option.some(it.getValue());
                }
            }
        } else {
            while (it.hasNext()) {
                if (key.equals(it.nextKey())) {
                    return Option.some(it.getValue());
                }
            }
        }
        return Option.none();
    }

    default V getOrDefault(K key, V defaultValue) {
        return getOption(key).getOrDefault(defaultValue);
    }

    default V getOrElse(K key, @NotNull Supplier<? extends V> supplier) {
        return getOption(key).getOrElse(supplier);
    }

    default <Ex extends Throwable> V getOrThrowException(K key, @NotNull Ex exception) throws Ex {
        return getOption(key).getOrThrowException(exception);
    }

    default <Ex extends Throwable> V getOrThrow(K key, @NotNull Supplier<? extends Ex> supplier) throws Ex {
        return getOption(key).getOrThrow(supplier);
    }

    default @NotNull MapView<K, V> withDefault(@NotNull Function<? super K, ? extends V> defaultFunction) {
        Objects.requireNonNull(defaultFunction);
        return new MapViews.WithDefault<>(this, defaultFunction);
    }

    //region Element Conditions

    default boolean contains(Tuple2<? extends K, ?> value) {
        return value != null && contains(value._1, value._2);
    }

    default boolean contains(K key, Object value) {
        return getOption(key).contains(value);
    }

    default boolean containsKey(K key) {
        if (knownSize() == 0) {
            return false;
        }
        return iterator().containsKey(key);
    }

    default boolean containsValue(Object value) {
        if (knownSize() == 0) {
            return false;
        }
        return iterator().containsValue(value);
    }

    default boolean anyMatch(@NotNull BiPredicate<? super K, ? super V> predicate) {
        if (knownSize() == 0) {
            return false;
        }
        return iterator().anyMatch(predicate);
    }

    default boolean allMatch(@NotNull BiPredicate<? super K, ? super V> predicate) {
        if (knownSize() == 0) {
            return true;
        }
        return iterator().allMatch(predicate);
    }

    default boolean noneMatch(@NotNull BiPredicate<? super K, ? super V> predicate) {
        if (knownSize() == 0) {
            return true;
        }
        return iterator().noneMatch(predicate);
    }

    //endregion

    default <R, G extends Growable<? super R>> @NotNull G mapTo(
            @NotNull G destination, @NotNull BiFunction<? super K, ? super V, ? extends R> mapper) {
        this.forEach((k, v) -> destination.plusAssign(mapper.apply(k, v)));
        return destination;
    }

    @SuppressWarnings("unchecked")
    default @NotNull Tuple2<K, V>[] toArray() {
        final int size = this.size();
        Tuple2<K, V>[] res = (Tuple2<K, V>[]) new Tuple2<?, ?>[size];
        MapIterator<K, V> it = iterator();
        for (int i = 0; i < size; i++) {
            res[i] = it.next();
        }
        return res;
    }

    default @NotNull String joinToString() {
        if (knownSize() == 0) {
            return "";
        }
        MapIterator<K, V> it = this.iterator();
        if (!it.hasNext()) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        builder.append(it.nextKey()).append(": ").append(it.getValue());
        while (it.hasNext()) {
            builder.append(", ")
                    .append(it.nextKey()).append(": ").append(it.getValue());
        }

        return builder.toString();
    }

    default void forEach(@NotNull BiConsumer<? super K, ? super V> consumer) {
        MapIterator<K, V> it = iterator();
        while (it.hasNext()) {
            consumer.accept(it.nextKey(), it.getValue());
        }
    }

    default <Ex extends Throwable> void forEachChecked(
            @NotNull CheckedBiConsumer<? super K, ? super V, ? extends Ex> consumer
    ) throws Ex {
        forEach(consumer);
    }

    default void forEachUnchecked(@NotNull CheckedBiConsumer<? super K, ? super V, ?> consumer) {
        forEach(consumer);
    }
}
