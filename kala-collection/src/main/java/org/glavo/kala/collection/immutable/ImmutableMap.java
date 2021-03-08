package org.glavo.kala.collection.immutable;

import org.glavo.kala.collection.Map;
import org.glavo.kala.collection.factory.MapFactory;
import org.jetbrains.annotations.NotNull;

public interface ImmutableMap<K, V> extends Map<K, V> {
    @Override
    default @NotNull String className() {
        return "ImmutableMap";
    }

    @Override
    default @NotNull <NK, NV> MapFactory<NK, NV, ?, ? extends ImmutableMap<NK, NV>> mapFactory() {
        throw new UnsupportedOperationException(); // TODO
    }
}
