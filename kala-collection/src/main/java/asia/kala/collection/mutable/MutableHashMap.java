package asia.kala.collection.mutable;

import asia.kala.control.Option;
import asia.kala.iterator.MapIterator;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class MutableHashMap<K, V> extends AbstractMutableMap<K, V> {
    private final HashMap<K, V> data = new HashMap<>();

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
    public final @NotNull Option<V> remove(K key) {
        if (data.containsKey(key)) {
            return Option.some(data.remove(key));
        }
        return Option.none();
    }

    @Override
    public final @NotNull MapIterator<K, V> iterator() {
        Iterator<Map.Entry<K, V>> it = data.entrySet().iterator();
        return new MapIterator<K, V>() {
            V value = null;

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

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }
        };
    }
}
