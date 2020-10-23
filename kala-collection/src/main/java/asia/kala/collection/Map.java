package asia.kala.collection;

import asia.kala.Equals;
import asia.kala.control.Option;
import asia.kala.function.CheckedBiConsumer;
import asia.kala.iterator.MapIterator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

public interface Map<K, V> extends Equals {
    int HASH_MAGIC = 124549981;

    @NotNull MapIterator<K, V> iterator();

    default int size() {
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

    @NotNull
    default String className() {
        return "Map";
    }

    @NotNull
    default Option<V> getOption(K key) {
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

    default V get(K key) {
        return getOption(key).get();
    }

    @Nullable
    default V getOrNull(K key) {
        return getOption(key).getOrNull();
    }

    default V getOrDefault(K key, V defaultValue) {
        return getOption(key).getOrElse(defaultValue);
    }

    default V getOrElseGet(K key, @NotNull Supplier<? extends V> supplier) {
        return getOption(key).getOrElseGet(supplier);
    }

    default <Ex extends Throwable> V getOrThrowException(K key, @NotNull Ex exception) throws Ex {
        return getOption(key).getOrThrowException(exception);
    }

    default <Ex extends Throwable> V getOrThrow(K key, @NotNull Supplier<? extends Ex> supplier) throws Ex {
        return getOption(key).getOrThrow(supplier);
    }

    default boolean containsKey(K key) {
        if (knownSize() == 0) {
            return false;
        }
        return iterator().containsKey(key);
    }

    default boolean containsValue(V value) {
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

    @Override
    default boolean canEqual(Object other) {
        return other instanceof Map<?, ?>;
    }
}
