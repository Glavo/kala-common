package org.glavo.kala.collection.immutable;

import org.glavo.kala.collection.MapLike;
import org.glavo.kala.collection.internal.FullMapOps;

public interface ImmutableMapOps<K, V, CC extends MapLike<?, ?>, COLL extends MapLike<K, V>>
        extends FullMapOps<K, V, CC, COLL> {
}
