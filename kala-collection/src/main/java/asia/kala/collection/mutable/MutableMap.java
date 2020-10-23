package asia.kala.collection.mutable;

import asia.kala.collection.Map;
import asia.kala.control.Option;
import org.jetbrains.annotations.NotNull;

public interface MutableMap<K, V> extends Map<K, V> {

    @NotNull Option<V> put(K key, V value);

    @NotNull Option<V> remove(K key);

    @Override
    default @NotNull String className() {
        return "MutableMap";
    }
}
