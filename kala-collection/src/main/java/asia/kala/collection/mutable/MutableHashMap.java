package asia.kala.collection.mutable;

import asia.kala.control.Option;
import asia.kala.iterator.MapIterator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

public final class MutableHashMap<K, V> extends AbstractMutableMap<K, V> {
    private final HashMap<K, V> data = new HashMap<>();

    @Override
    public final @NotNull MapIterator<K, V> iterator() {
        Iterator<Map.Entry<K, V>> it = data.entrySet().iterator();
        return new MapIterator<K, V>() {
            V value = null;

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public K nextKey() {
                Map.Entry<K, V> next = it.next();
                value = next.getValue();
                return next.getKey();
            }

            @Override
            public V getValue() {
                return value;
            }
        };
    }

    //region Size Info

    @Override
    public final boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public final int size() {
        return data.size();
    }

    @Override
    public final int knownSize() {
        return data.size();
    }

    //endregion

    @Override
    public final V get(K key) {
        if (!data.containsKey(key)) {
            throw new NoSuchElementException();
        }
        return data.get(key);
    }

    @Override
    public final @Nullable V getOrNull(K key) {
        return data.get(key);
    }

    @Override
    public @NotNull Option<V> getOption(K key) {
        return data.containsKey(key)
                ? Option.some(data.get(key))
                : Option.none();
    }

    @Override
    public final @NotNull Option<V> put(K key, V value) {
        if (data.containsKey(key)) {
            return Option.some(data.put(key, value));
        } else {
            data.put(key, value);
            return Option.none();
        }
    }

    @Override
    public final void set(K key, V value) {
        data.put(key, value);
    }

    @Override
    public final @NotNull Option<V> remove(K key) {
        if (data.containsKey(key)) {
            return Option.some(data.remove(key));
        }
        return Option.none();
    }

    @Override
    public final boolean containsKey(K key) {
        return data.containsKey(key);
    }

    @Override
    public final boolean containsValue(V value) {
        return data.containsValue(value);
    }
}
