package kala.collection.immutable;

import kala.collection.MapLike;
import kala.collection.internal.FullMapOps;

public interface ImmutableMapOps<K, V, CC extends MapLike<?, ?>, COLL extends MapLike<K, V>>
        extends FullMapOps<K, V, CC, COLL> {
}
