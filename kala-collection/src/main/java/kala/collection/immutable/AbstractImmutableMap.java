package kala.collection.immutable;

import kala.collection.AbstractMap;
import kala.collection.factory.MapFactory;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unchecked")
public abstract class AbstractImmutableMap<K, V> extends AbstractMap<K, V> implements ImmutableMap<K, V> {
    static <K, V, T extends ImmutableMap<K, ? extends V>, Builder> T putted(
            @NotNull ImmutableMap<K, ? extends V> map,
            K key, V value,
            MapFactory<K, V, Builder, ? extends T> factory
    ) {
        if (map.contains(key, value)) {
            return (T) map;
        }

        Builder builder = factory.newBuilder();
        int ks = map.knownSize();
        if (ks >= 0) {
            factory.sizeHint(builder, ks + 1);
        }
        map.forEach((k, v) -> factory.addToBuilder(builder, k, v));
        factory.addToBuilder(builder, key, value);
        return factory.build(builder);
    }

    static <K, V, T extends ImmutableMap<K, ? extends V>, Builder> T removed(
            @NotNull ImmutableMap<K, ? extends V> map,
            K key,
            MapFactory<K, V, Builder, ? extends T> factory
    ) {
        if (!map.containsKey(key)) {
            return (T) map;
        }

        Builder builder = factory.newBuilder();
        int ks = map.knownSize();
        if (ks > 0) {
            factory.sizeHint(builder,ks - 1);
        }
        if (key == null) {
            map.forEach((k, v) -> {
                if (null != k) {
                    factory.addToBuilder(builder, k, v);
                }
            });
        } else {
            map.forEach((k, v) -> {
                if (!key.equals(k)) {
                    factory.addToBuilder(builder, k, v);
                }
            });
        }
        return factory.build(builder);
    }
}
