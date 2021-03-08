package org.glavo.kala.collection.mutable;

import org.glavo.kala.collection.factory.MapFactory;

public abstract class AbstractMutableMapFactory<K, V, M extends MutableMap<K, V>> implements MapFactory<K, V, M, M> {
    @Override
    public M build(M m) {
        return m;
    }

    @Override
    public void addToBuilder(M m, K key, V value) {
        m.set(key, value);
    }

    @Override
    public M mergeBuilder(M builder1, M builder2) {
        builder1.putAll(builder2);
        return builder1;
    }
}
