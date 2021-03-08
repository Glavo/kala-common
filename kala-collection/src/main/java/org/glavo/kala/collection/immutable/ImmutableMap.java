package org.glavo.kala.collection.immutable;

import org.glavo.kala.collection.Map;
import org.jetbrains.annotations.NotNull;

public interface ImmutableMap<K, V> extends Map<K, V> {
    @Override
    default @NotNull String className() {
        return "ImmutableMap";
    }
}
