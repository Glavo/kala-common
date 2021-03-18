package org.glavo.kala.collection.internal;

import org.glavo.kala.collection.MapLike;
import org.jetbrains.annotations.NotNull;

public interface FullMapOps<K, V, CC extends MapLike<?, ?>, COLL extends MapLike<K, V>> extends MapLike<K, V> {
    @NotNull COLL updated(K key, V value);

    @NotNull COLL removed(K key);
}
