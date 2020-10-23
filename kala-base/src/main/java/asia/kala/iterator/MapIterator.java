package asia.kala.iterator;

import asia.kala.Tuple;
import asia.kala.Tuple2;
import asia.kala.function.CheckedBiConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

public interface MapIterator<K, V> extends Iterator<Tuple2<K, V>> {
    @SuppressWarnings("unchecked")
    static <K, V> @NotNull MapIterator<K, V> ofIterator(@NotNull Iterator<? extends Tuple2<? extends K, ? extends V>> it) {
        Objects.requireNonNull(it);
        if (it instanceof MapIterator<?, ?>) {
            return ((MapIterator<K, V>) it);
        }

        return new MapIterator<K, V>() {
            private V value = null;

            @Override
            public final Tuple2<K, V> next() {
                Tuple2<K, V> res = (Tuple2<K, V>) it.next();
                value = res._2;
                return res;
            }

            @Override
            public final K nextKey() {
                Tuple2<? extends K, ? extends V> res = it.next();
                value = res._2;
                return res._1;
            }

            @Override
            public final V getValue() {
                return value;
            }

            @Override
            public final boolean hasNext() {
                return it.hasNext();
            }
        };
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
